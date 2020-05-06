package application.model.market.naver;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import application.Main;
import application.controller.market.TransferController;
import application.model.Commons;
import application.model.commons.MySqlQuery;
import application.model.vo.NaverOptionVO;

public class NaverCommon {

	public static final String LOGIN_URL = "https://sell.smartstore.naver.com/#/login";
	public static final String PRODUCT_SEARCH_URL = "https://sell.smartstore.naver.com/#/products/origin-list";
	public static final String PRODUCT_INSERT_URL = "https://sell.smartstore.naver.com/#/products/create";
	public static final String PRODUCT_UPDATE_URL = "https://sell.smartstore.naver.com/#/products/edit/";
	
	private static WebDriverWait wait = new WebDriverWait(Main.driver, 5);
	
	public static void login() {
	    wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("네이버 아이디 로그인")));
	    
	    String loginId = Main.loginMap.get("네이버").getId();
	    String loginPw = Main.loginMap.get("네이버").getPw();
	    String memo = Main.loginMap.get("네이버").getMemo();
	    
	    if(loginId.equals(memo)) {
	    	Commons.xpathClickAfterElementToBeClickable("//a[contains(text(),\'네이버 아이디 로그인\')]");
	    	wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("id")));
	    	wait.until(ExpectedConditions.elementToBeClickable(By.id("id")));
	    	Main.driver.findElement(By.id("id")).sendKeys(loginId);
	    	Main.driver.findElement(By.id("pw")).click();
	    	Main.driver.findElement(By.id("pw")).sendKeys(loginPw);
	    	Main.driver.findElement(By.id("log.login")).click();
	    } else {
	    	Main.driver.findElement(By.id("loginId")).sendKeys(loginId);
	    	Main.driver.findElement(By.id("loginPassword")).click();
	    	Main.driver.findElement(By.id("loginPassword")).sendKeys(loginPw);
	    	Main.driver.findElement(By.id("loginButton")).click();
	    }
	}
	
	public static String createProdName(String prodName) {
		
		String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";

		String OPENNMAE = "";
		OPENNMAE = prodName.replace(match, " ");
		OPENNMAE = OPENNMAE.replace("~", " ");
		OPENNMAE = OPENNMAE.replace("|", " ");
		OPENNMAE = OPENNMAE.replace("_", " ");
		String namest[] = OPENNMAE.split(" ");
		String reName = "";
		
		for(int x=0; x<namest.length; x++) {
			
			String s = namest[x].trim();
			
			for(int y=0; y<namest.length; y++) {
				
				if(x != y) {
					
					if(namest[y].contains(s)) {
						
						namest[x] = "";
					}
				}
			}
		}
		
		for(int y=0; y<namest.length; y++) {
			
			reName = reName.concat(namest[y]);
			reName = reName.concat(" ");
		}
		
		reName = reName.replace("  ", " ");
		
		OPENNMAE = reName;
		
		if(OPENNMAE.length() > 100) {
			
			OPENNMAE = OPENNMAE.substring(0,100);
		}
		
		return OPENNMAE;
	}
	
	public static void categoryInput(String prodId) {
		// TODO searchCata 구현해야함
//		String reCata = searchCata(prodId, "네이버");
		String reCata = "생활/건강,공구,수작업공구,기타수작업공구";
		
		String stCategory[] = reCata.split(",");
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[contains(text(),\'카테고리명 선택\')]")));
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/ui-view[1]/div[3]/div/div[3]/div/ui-view/div[2]/fieldset/form/ng-include/ui-view[7]/div/div[2]/div/div[1]/div/div/div[1]/div/div/input")));
		Commons.xpathClickAfterElementToBeClickable("//label[contains(text(),\'카테고리명 선택\')]");
		
		for(int i=0; i<stCategory.length; i++) {
			
			String deCategory = stCategory[i];
			
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),\'"+deCategory+"\')]")));
			Main.driver.findElement(By.xpath("//a[contains(text(),\'"+deCategory+"\')]")).click();
			
			waitForAjax();
		}
		
		try {
			
			Commons.xpathClickAfterElementToBeClickable("/html/body/div[1]/div/div/div[3]/div/button");
			System.out.println("안전관리대상");
		} catch (TimeoutException e) {
			System.out.println("안전관리대상아님");
		}
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[contains(text(),\'카테고리명 선택\')]")));
		Commons.xpathClickAfterElementToBeClickable("//label[contains(text(),\'카테고리명 선택\')]");
	}
	
	public static void prodNameInput(String prodName) {
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("product.name")));
		
		String s = "var ch2=document.getElementsByName('product.name')[0];"
				+ "var event2 = document.createEvent('HTMLEvents');" 
				+ "event2.initEvent('click', false, true);"
				+ "ch2.dispatchEvent(event2);";
		Main.js.executeScript(s);
		
		Main.driver.findElement(By.name("product.name")).sendKeys(prodName);
	}
	
	public static void prodPriceInput(String prodPrice) {
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("prd_price2")));
		
		String s = "var ch2=document.getElementById('prd_price2');"
				+ "var event2 = document.createEvent('HTMLEvents');"
				+ "event2.initEvent('click', false, true);"
				+ "ch2.dispatchEvent(event2);";
		Main.js.executeScript(s);
		
		Main.driver.findElement(By.id("prd_price2")).sendKeys(prodPrice);
		Main.driver.findElement(By.id("prd_price2")).sendKeys(Keys.ENTER);
	}
	
	public static void prodStockInput(String prodStock) {
		Commons.jsScrollIntoView(Main.driver.findElement(By.id("stock")));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("stock")));
		Main.driver.findElement(By.id("stock")).sendKeys(prodStock);
		Main.driver.findElement(By.id("stock")).sendKeys(Keys.ENTER);
	}
	// TODO 옵션 구현중
	public static void prodOptionInput(String prodId) {
		String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";
		ArrayList<NaverOptionVO> returnOp = MySqlQuery.searchOption(prodId);
		
		String optionQty = "999";
		try {
			if(returnOp.size()>0) {
				Commons.jsScrollIntoView("/html/body/ui-view[1]/div[3]/div/div[3]/div/ui-view/div[2]/fieldset/form/ng-include/ui-view[9]/div/div/div/div/a");
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/ui-view[1]/div[3]/div/div[3]/div/ui-view/div[2]/fieldset/form/ng-include/ui-view[9]/div/div/div/div/a")));
				Commons.xpathClickAfterElementToBeClickable("/html/body/ui-view[1]/div[3]/div/div[3]/div/ui-view/div[2]/fieldset/form/ng-include/ui-view[9]/div/div/div/div/a");
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/ui-view[1]/div[3]/div/div[3]/div/ui-view/div[2]/fieldset/form/ng-include/ui-view[9]/div/fieldset/div/div/div[1]/div/div/div/div/label[1]")));
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/ui-view[1]/div[3]/div/div[3]/div/ui-view/div[2]/fieldset/form/ng-include/ui-view[9]/div/fieldset/div/div/div[1]/div/div/div/div/label[1]")));

				String opNm = "";
				String opVal = "";

				for (NaverOptionVO optionVO : returnOp) {
					opNm = optionVO.getOptType();

					String opValue = optionVO.getOptNm();

					opVal = opVal.concat(",");

					opValue = opValue.replaceAll(match, "");

					if (opValue.length() > 25) {

						opValue = opValue.substring(0, 25);
					}
					opValue = opValue.trim();
					opVal = opVal.concat(opValue);
				}
				opVal = opVal.substring(1, opVal.length());
				
				// 옵션명
				Commons.clearAndSendKeyAfterElementToBeClickable(Main.driver.findElement(By.xpath(
						"/html/body/ui-view[1]/div[3]/div/div[3]/div/ui-view/div[2]/fieldset/form/ng-include/ui-view[9]/div/fieldset/div/div/div[2]/div[2]/div[4]/div/div/div[1]/div[1]/div/input")), opNm);
				// 옵션값
				Commons.clearAndSendKeyAfterElementToBeClickable(Main.driver.findElement(By.xpath(
						"/html/body/ui-view[1]/div[3]/div/div[3]/div/ui-view/div[2]/fieldset/form/ng-include/ui-view[9]/div/fieldset/div/div/div[2]/div[2]/div[4]/div/div/div[1]/div[2]/div/input")), opVal);
				Commons.xpathClickAfterElementToBeClickable("//a[contains(text(),\'옵션목록으로 적용 \')]");
				
				Commons.clearAndSendKeyAfterElementToBeClickable(Main.driver.findElement(By.xpath(
						"/html/body/ui-view[1]/div[3]/div/div[3]/div/ui-view/div[2]/fieldset/form/ng-include/ui-view[9]/div/fieldset/div/div/div[2]/div[3]/div[2]/div[1]/div[2]/div/div[4]/div/div/input")), optionQty);
				Commons.xpathClickAfterElementToBeClickable("/html/body/ui-view[1]/div[3]/div/div[3]/div/ui-view/div[2]/fieldset/form/ng-include/ui-view[9]/div/fieldset/div/div/div[2]/div[3]/div[2]/div[2]/div/div/div/div[1]/div[2]/div/div[2]/div[1]/div[2]/div/label/span");
				Commons.xpathClickAfterElementToBeClickable("/html/body/ui-view[1]/div[3]/div/div[3]/div/ui-view/div[2]/fieldset/form/ng-include/ui-view[9]/div/fieldset/div/div/div[2]/div[3]/div[2]/div[1]/div[2]/div/div[7]/a");
				for (NaverOptionVO optionVO : returnOp) {

					String optPrice = optionVO.getOptPrice();
					String itemcd = optionVO.getItemCd();
					if (optPrice.contains(".")) {
						optPrice = optPrice.substring(0, optPrice.indexOf("."));
					}

					
					Actions builder = new Actions(Main.driver);
					Action clickEvnt = null;
					WebElement target = null;
					WebElement optarget = null;
					String opValue = optionVO.getOptNm();

					opValue = opValue.replaceAll(match, "");

					if (opValue.length() > 25) {

						opValue = opValue.substring(0, 25);
					}
					opValue = opValue.trim();
					if(opValue.equals("")) {
						continue;
					}

					optarget = Main.driver.findElement(By.xpath("/html/body/ui-view[1]/div[3]/div/div[3]/div/ui-view/div[2]/fieldset/form/ng-include/ui-view[9]/div/fieldset/div/div/div[2]/div[3]/div[2]/div[2]/div/div/div/div[3]/div[2]/div/div"));
					optarget = optarget.findElement(By.xpath("//div[text()=\'"+opValue+"\']"));
					
					int compid = Integer.parseInt(optarget.findElement(By.xpath("//div[text()=\'"+opValue+"\']")).getAttribute("comp-id"));
					
					target = optarget.findElement(By.xpath("//div[@comp-id=\'"+(compid+1)+"\']"));
					
					clickEvnt = builder.click(target).sendKeys(target, optPrice).build();
					clickEvnt.perform();
					
					if (!optionVO.getSaleYn().equals("Y")) {
						target = optarget.findElement(By.xpath("//div[@comp-id=\'"+(compid+2)+"\']"));
						wait.until(ExpectedConditions.elementToBeClickable(target));
						clickEvnt = builder.doubleClick(target).sendKeys(target, "0").build();
						clickEvnt.perform();
					}
					
					target = optarget.findElement(By.xpath("//div[@comp-id=\'"+(compid+4)+"\']"));
					
					wait.until(ExpectedConditions.elementToBeClickable(target));
					clickEvnt = builder.click(target).sendKeys(target, itemcd).build();
					clickEvnt.perform();
					
					Commons.jsScrollIntoView(optarget);
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static public void waitForProdInsertPage(String prodId) {
		int cnt = 0;
		try {
			while(true) {
				if(Main.driver.getCurrentUrl().contains("products/create") || cnt > 5) {
					break;
				}
				Main.driver.navigate().to(PRODUCT_INSERT_URL);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),\'상품관리\')]")));
			}
			
			waitForAjax();
		} catch (Exception e) {
			TransferController.setTextAreaWrite("itemcd :["+prodId+"] 상품등록 페이지 이동 중 에러");
		}
	}
	
	static public void waitForProdSearchPage(String prodId) {
		int cnt = 0;
		try {
			while(true) {
				
				if(Main.driver.getCurrentUrl().contains("products/origin-list") || cnt > 5) {
					break;
				}
				Main.driver.navigate().to(PRODUCT_SEARCH_URL);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),\'상품관리\')]")));
			}
			try {
				waitForAjax();
				Commons.xpathClickAfterElementToBeClickable("/html/body/ui-view[1]/div[3]/div/div[3]/div/ui-view/div/ui-view[1]/div[1]/ul/li[1]/a");
				waitForAjax();
			} catch (Exception e) {
				
			}
		} catch (Exception e) {
			TransferController.setTextAreaWrite("itemcd :["+prodId+"] 상품검색 페이지 이동 중 에러");
		}
	}
	
	static public void waitForProdUpdatePage(String prodId, String naverProdId) {
		int cnt = 0;
		try {
			while(true) {
				
				if(Main.driver.getCurrentUrl().contains("products/edit") || cnt > 5) {
					break;
				}
				Main.driver.navigate().to(PRODUCT_UPDATE_URL+naverProdId);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),\'상품관리\')]")));
			}
			
			waitForAjax();
		} catch (Exception e) {
			TransferController.setTextAreaWrite("itemcd :["+prodId+"] 상품수정 페이지 이동 중 에러");
		}
	}
	
	private static void waitForAjax() {
		wait.until(ExpectedConditions.attributeContains(By.xpath("/html/body"), "class", "pace-running"));
		wait.until(ExpectedConditions.attributeContains(By.xpath("/html/body"), "class", "pace-done"));
	}
}
