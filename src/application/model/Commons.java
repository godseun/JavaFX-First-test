package application.model;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import application.Main;

public class Commons {

	private static WebDriverWait wait = new WebDriverWait(Main.driver, 5);

	static public boolean focusAfterCheckOpenTab(String marketName) {
		try {
			Main.driver.switchTo().window(marketName);
			return true;
		} catch (NoSuchWindowException e) {
			return false;
		}
	}
	
	static public void jsScrollIntoView(String xpath) {
		Main.js.executeScript("arguments[0].scrollIntoView()", Main.driver.findElement(By.xpath(xpath)));
	}
	
	static public void jsScrollIntoView(WebElement webEle) {
		Main.js.executeScript("arguments[0].scrollIntoView()", webEle);
	}
	
	static public void xpathClickAfterElementToBeClickable(String xpath) {
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath))).click();
	}
	
	static public void clearAndSendKeyAfterElementToBeClickable(WebElement webEle, String st) {
		wait.until(ExpectedConditions.elementToBeClickable(webEle));
		webEle.clear();
		webEle.sendKeys(st);
	}
	
	static public void newTab(String url, String marketName) {
		Main.js.executeScript("window.open('"+url+"','"+marketName+"')");
		Main.driver.switchTo().window(marketName);
	}
	
	static public void windowInit() {
		for(String window : Main.driver.getWindowHandles()) {
			if(!window.equals(Main.defaultWindow)) {
				Main.driver.close();
			}
		}
		Main.driver.switchTo().window(Main.defaultWindow);
	}
	
	public static boolean waitUrlCheck(String url) {
		boolean urlCheck = true;
		try {
			wait.until(ExpectedConditions.urlToBe(url));
		} catch (TimeoutException e) {
			urlCheck = false;
		}
		return urlCheck;
	}
	
	public static boolean waitUrlCheckAndGo(String url) {
		int cnt = 0;
		while(true) {
			
			if(cnt>5) {
				return false;
			}
			try {
				wait.until(ExpectedConditions.urlToBe(url));
				return true;
			} catch (TimeoutException e) {
				Main.driver.navigate().to(url);
			}
			cnt++;
		}
	}
	
	public static String getmImagePhoneartAtti(String url) {
		InputStream is = null;
		FileOutputStream fos = null;
		String filePath = "";
		CloseableHttpClient client = HttpClients.createDefault();
		try {
			String mkFolder = "";

			HttpGet httpget = new HttpGet(url);
			HttpResponse response = client.execute(httpget);
			HttpEntity entity = response.getEntity();

			String[] sP = url.split("/");
			for (int i = 0; i < sP.length - 1; i++) {
				if (!(sP[i].startsWith("https:")) && !(sP[i].startsWith("http:")) && !(sP[i].startsWith("www"))
						&& !(sP[i].contains(".")) && !(sP[i].trim().equals(""))) {

					mkFolder = mkFolder.concat(sP[i]) + "\\";
				}
			}
//			System.out.println("mkFolder:" + mkFolder);
			mkFolder = "D:\\openMarketImg\\" + mkFolder;
			File desti = new File(mkFolder);
			// 해당 디렉토리의 존재여부를 확인

			if (!desti.exists()) {
				// 없다면 생성
				desti.mkdirs();
			}

			is = entity.getContent();
			// System.out.println("is:"+is);
			filePath = mkFolder;

			filePath = filePath + sP[sP.length - 1];
//			System.out.println("filePath:" + filePath);

			fos = new FileOutputStream(new File(filePath));
			int inByte;

			while ((inByte = is.read()) != -1) {
				// System.out.println("inByte:"+inByte);
				fos.write(inByte);
			}

			File input = new File(filePath);
			BufferedImage image = ImageIO.read(input);

			BufferedImage resized = resize(image, 640, 640);
			File output = new File(filePath);
			ImageIO.write(resized, "jpg", output);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
			if (fos != null) {
				try {

					fos.close();
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}
		return filePath;
	}

	private static BufferedImage resize(BufferedImage img, int height, int width) {
		Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g2d = resized.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();
		return resized;
	}
}
