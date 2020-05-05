package application.model.market.naver;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import application.Main;

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
	    	wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(),\'네이버 아이디 로그인\')]")));
	    	Main.driver.findElement(By.xpath("//a[contains(text(),\'네이버 아이디 로그인\')]")).click();
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
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[contains(text(),\'카테고리명 선택\')]")));
		Main.driver.findElement(By.xpath("//label[contains(text(),\'카테고리명 선택\')]")).click();
		
		for(int i=0; i<stCategory.length; i++) {
			
			String deCategory = stCategory[i];
			
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),\'"+deCategory+"\')]")));
			Main.driver.findElement(By.xpath("//a[contains(text(),\'"+deCategory+"\')]")).click();
			
			wait.until(ExpectedConditions.attributeContains(By.xpath("/html/body"), "class", "pace-running"));
			wait.until(ExpectedConditions.attributeContains(By.xpath("/html/body"), "class", "pace-done"));
		}
		
		try {
			
			new WebDriverWait(Main.driver, 2).until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div/div/div[3]/div/button")));
			Main.driver.findElement(By.xpath("/html/body/div[1]/div/div/div[3]/div/button")).click();
			System.out.println("안전관리대상");
		} catch (TimeoutException e) {
			System.out.println("안전관리대상아님");
		}
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[contains(text(),\'카테고리명 선택\')]")));
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[contains(text(),\'카테고리명 선택\')]")));
		Main.driver.findElement(By.xpath("//label[contains(text(),\'카테고리명 선택\')]")).click();
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
		Main.js.executeScript("arguments[0].scrollIntoView()", Main.driver.findElement(By.id("stock")));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("stock")));
		Main.driver.findElement(By.id("stock")).sendKeys(prodStock);
		Main.driver.findElement(By.id("stock")).sendKeys(Keys.ENTER);
	}
	// TODO 옵션 구현중
	public static void prodOptionInput(String prodId) {
		String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";
	}
}
