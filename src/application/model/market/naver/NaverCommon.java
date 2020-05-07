package application.model.market.naver;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.ScriptTimeoutException;
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
	
	public void login() {
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
	
	public String createProdName(String prodName) {
		
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
	
	public void closeAfterModalCheck() {
		if(Main.driver.findElement(By.xpath("/html/body")).getAttribute("class").contains("modal-open")) {
			if(Main.driver.findElement(By.xpath("//div[@class=\'modal-body\']")).getText().contains("이전에 작성하던 내용이 존재합니다")) {
				Commons.xpathClickAfterElementToBeClickable("/html/body/div[1]/div/div/div[2]/div/span[1]/button");
				wait.until(ExpectedConditions.attributeToBe(By.xpath("/html/body"), "class", "pace-done"));
			}
		}
	}
	
	public void categoryInput(String prodId) {
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
			
			waitForNaverAjax();
		}
		
		try {
			
			Commons.xpathClickAfterElementToBeClickable("/html/body/div[1]/div/div/div[3]/div/button");
		} catch (TimeoutException e) {
		}
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[contains(text(),\'카테고리명 선택\')]")));
		Commons.xpathClickAfterElementToBeClickable("//label[contains(text(),\'카테고리명 선택\')]");
	}
	
	public void prodNameInput(String prodName) {
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("product.name")));
		
		String s = "var ch2=document.getElementsByName('product.name')[0];"
				+ "var event2 = document.createEvent('HTMLEvents');" 
				+ "event2.initEvent('click', false, true);"
				+ "ch2.dispatchEvent(event2);";
		Main.js.executeScript(s);
		
		Main.driver.findElement(By.name("product.name")).sendKeys(prodName);
	}
	
	public void prodPriceInput(String prodPrice) {
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("prd_price2")));
		
		String s = "var ch2=document.getElementById('prd_price2');"
				+ "var event2 = document.createEvent('HTMLEvents');"
				+ "event2.initEvent('click', false, true);"
				+ "ch2.dispatchEvent(event2);";
		Main.js.executeScript(s);
		
		Main.driver.findElement(By.id("prd_price2")).sendKeys(prodPrice);
		Main.driver.findElement(By.id("prd_price2")).sendKeys(Keys.ENTER);
	}
	
	public void prodStockInput(String prodStock) {
		Commons.jsScrollIntoView(Main.driver.findElement(By.id("stock")));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("stock")));
		Main.driver.findElement(By.id("stock")).sendKeys(prodStock);
		Main.driver.findElement(By.id("stock")).sendKeys(Keys.ENTER);
	}
	
	public void prodOptionInput(String prodId) {
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
	
	public void prodImgInput(String fileP) {
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@id=\'productForm\']/ng-include/ui-view[9]/div/div/div/div/a")));
		
		String s = "var ch2=document.querySelector('#representImage > div > div.seller-product-img.add-img > div > ul > li > div > a');"
				+ "var event2 = document.createEvent('HTMLEvents');" 
				+ "event2.initEvent('click', false, true);"
				+ "ch2.dispatchEvent(event2);";
		Main.js.executeScript(s);
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//button[@type=\'button\'])[2]")));
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//button[@type=\'button\'])[2]")));
		
		Main.driver.findElement(By.cssSelector("body > label > input[type=file]")).sendKeys(fileP);
	}
	
	public void prodContentInput(String content) {
		WebElement elementDe = Main.driver.findElement(By.xpath("//span[contains(text(),\'HTML 작성\')]"));
		Commons.jsScrollIntoView(elementDe);
		
		String s2 = "var ch=document.querySelector('.seller-product-detail > ul:nth-child(1) > li:nth-child(2) > a:nth-child(1)');"
				+ "var event = document.createEvent('HTMLEvents');"
				+ "event.initEvent('click', false, true);"
				+ "ch.dispatchEvent(event);";
		Main.js.executeScript(s2);
		
		WebElement elementDe2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
				"//div[@class=\'content write-html\']/div/textarea")));
		Commons.jsScrollIntoView(elementDe2);
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/ui-view[1]/div[3]/div/div[3]/div/ui-view/div[2]/fieldset/form/ng-include/ui-view[11]/div/div[1]/label")));
		
		elementDe2.sendKeys(content);
		
		s2 = "var ch=document.querySelector('#productForm > ng-include > ui-view:nth-child(15) > div > div');"
				+ "var event = document.createEvent('HTMLEvents');" 
				+ "event.initEvent('click', false, true);"
				+ "ch.dispatchEvent(event);";
		Main.js.executeScript(s2);
	}
	
	public void prodBrandInput(String brandNm) {
		WebElement target = Main.driver.findElement(By.xpath("/html/body/ui-view[1]/div[3]/div/div[3]/div/ui-view/div[2]/fieldset/form/ng-include/ui-view[14]/div[2]/div[1]"));
		Commons.jsScrollIntoView(target);
		
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/ui-view[1]/div[3]/div/div[3]/div/ui-view/div[2]/fieldset/form/ng-include/ui-view[14]/div[2]/div[1]")));
		
		Actions builder = new Actions(Main.driver);
		Action clickEvnt = builder.click(target).build();
		
		clickEvnt.perform();
		
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/ui-view[1]/div[3]/div/div[3]/div/ui-view/div[2]/fieldset/form/ng-include/ui-view[14]/div[2]/div[2]/div/ncp-naver-shopping-search-info/div[2]/div/div[1]/div/ncp-brand-manufacturer-input/div/div/div/div/div/div[1]/input")));
		Main.driver.findElement(By.xpath("/html/body/ui-view[1]/div[3]/div/div[3]/div/ui-view/div[2]/fieldset/form/ng-include/ui-view[14]/div[2]/div[2]/div/ncp-naver-shopping-search-info/div[2]/div/div[1]/div/ncp-brand-manufacturer-input/div/div/div/div/div/div[1]/input")).clear();
		Main.driver.findElement(By.xpath("/html/body/ui-view[1]/div[3]/div/div[3]/div/ui-view/div[2]/fieldset/form/ng-include/ui-view[14]/div[2]/div[2]/div/ncp-naver-shopping-search-info/div[2]/div/div[1]/div/ncp-brand-manufacturer-input/div/div/div/div/div/div[1]/input")).sendKeys(brandNm);
		Main.driver.findElement(By.xpath("/html/body/ui-view[1]/div[3]/div/div[3]/div/ui-view/div[2]/fieldset/form/ng-include/ui-view[14]/div[2]/div[2]/div/ncp-naver-shopping-search-info/div[2]/div/div[1]/div/ncp-brand-manufacturer-input/div/div/div/div/div/div[1]/input")).sendKeys(Keys.ENTER);

		waitForNaverAjax();
		Main.driver.findElement(By.xpath("/html/body/ui-view[1]/div[3]/div/div[3]/div/ui-view/div[2]/fieldset/form/ng-include/ui-view[14]/div[2]/div[2]/div/ncp-naver-shopping-search-info/div[2]/div/div[2]/div/ncp-brand-manufacturer-input/div/div/div/div/div/div[1]/input")).clear();
		Main.driver.findElement(By.xpath("/html/body/ui-view[1]/div[3]/div/div[3]/div/ui-view/div[2]/fieldset/form/ng-include/ui-view[14]/div[2]/div[2]/div/ncp-naver-shopping-search-info/div[2]/div/div[2]/div/ncp-brand-manufacturer-input/div/div/div/div/div/div[1]/input")).sendKeys(brandNm);
		Main.driver.findElement(By.xpath("/html/body/ui-view[1]/div[3]/div/div[3]/div/ui-view/div[2]/fieldset/form/ng-include/ui-view[14]/div[2]/div[2]/div/ncp-naver-shopping-search-info/div[2]/div/div[2]/div/ncp-brand-manufacturer-input/div/div/div/div/div/div[1]/input")).sendKeys(Keys.ENTER);
		
		waitForNaverAjax();
	}
	
	public void prodEtcInput(String callNum) {
		
		try {
			Commons.xpathClickAfterElementToBeClickable("//label[contains(text(),\'KC인증 없음\')]");
		} catch (Exception e) {
		}
		
		Commons.xpathClickAfterElementToBeClickable("/html/body/ui-view[1]/div[3]/div/div[3]/div/ui-view/div[2]/fieldset/form/ng-include/ui-view[14]/div[2]/div[2]/div/div[3]/div/div/div/div/div[2]/label");
		
		WebElement target = Main.driver.findElement(By.xpath("/html/body/ui-view[1]/div[3]/div/div[3]/div/ui-view/div[2]/fieldset/form/ng-include/ui-view[15]/div/fieldset/div/div/div[2]/div/div/div[1]/div/div[1]"));
		
		Actions builder = new Actions(Main.driver);
		Action clickEvnt = builder.click(target).build();
		
		Commons.jsScrollIntoView(target);
		
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/ui-view[1]/div[3]/div/div[3]/div/ui-view/div[2]/fieldset/form/ng-include/ui-view[15]/div/fieldset/div/div/div[2]/div/div/div[1]/div/div[1]")));
		
		clickEvnt = builder.click(target).build();
		
		clickEvnt.perform();
		
		
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(text(),\'기타 재화\')]")));
		
		target = Main.driver.findElement(By.xpath("//div[contains(text(),\'기타 재화\')]"));
		Commons.jsScrollIntoView(target);
		
		clickEvnt = builder.click(target).build();
		clickEvnt.perform();
		
		Main.driver.findElement(By.xpath("//div[contains(text(),\'기타 재화\')]")).click();
		
		Commons.xpathClickAfterElementToBeClickable("//label[contains(text(),\'상품상세 참조로 전체 입력\')]");
		
		Commons.xpathClickAfterElementToBeClickable("//label[contains(text(),\'해당사항 없음\')]");
		
		Commons.xpathClickAfterElementToBeClickable("/html/body/ui-view[1]/div[3]/div/div[3]/div/ui-view/div[2]/fieldset/form/ng-include/ui-view[15]/div/fieldset/div/div/div[3]/ng-include/div[5]/div/div[1]/label[1]");
		
		Commons.xpathClickAfterElementToBeClickable("/html/body/ui-view[1]/div[3]/div/div[3]/div/ui-view/div[2]/fieldset/form/ng-include/ui-view[15]/div/fieldset/div/div/div[3]/ng-include/div[5]/div/div[1]/label[2]");
		
		wait.until(ExpectedConditions.attributeContains(By.xpath("/html/body/ui-view[1]/div[3]/div/div[3]/div/ui-view/div[2]/fieldset/form/ng-include/ui-view[15]/div/fieldset/div/div/div[3]/ng-include/div[5]/div/div[1]/label[2]/input"), "class", "ng-valid-parse"));
		Main.driver.findElement(By.xpath("/html/body/ui-view[1]/div[3]/div/div[3]/div/ui-view/div[2]/fieldset/form/ng-include/ui-view[15]/div/fieldset/div/div/div[3]/ng-include/div[5]/div/div[3]/input")).clear();
		Main.driver.findElement(By.xpath("/html/body/ui-view[1]/div[3]/div/div[3]/div/ui-view/div[2]/fieldset/form/ng-include/ui-view[15]/div/fieldset/div/div/div[3]/ng-include/div[5]/div/div[3]/input")).sendKeys(callNum);
		
		WebElement details = Main.driver.findElement(By.cssSelector("#productForm > ng-include > ui-view:nth-child(15) > div > fieldset > div > div"));
		
		for(int i=0; i<details.findElements(By.tagName("label")).size(); i++) {
			
			String s = details.findElements(By.tagName("label")).get(i).getText();
			
			if(s.equals("상품상세 참조")) {
				target = details.findElements(By.tagName("label")).get(i);
				
				Commons.jsScrollIntoView(target);
				details.findElements(By.tagName("label")).get(i).click();
			}
		}
		Commons.xpathClickAfterElementToBeClickable("/html/body/ui-view[1]/div[3]/div/div[3]/div/ui-view/div[2]/fieldset/form/ng-include/ui-view[16]/div[1]/div/div/div/a");
		
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/ui-view[1]/div[3]/div/div[3]/div/ui-view/div[2]/fieldset/form/ng-include/ui-view[16]/div[1]/div[2]/div/div[7]/div[1]/div/div/div/div/div/div[1]")));
		
		target = Main.driver.findElement(By.xpath("/html/body/ui-view[1]/div[3]/div/div[3]/div/ui-view/div[2]/fieldset/form/ng-include/ui-view[16]/div[1]/div[2]/div/div[7]/div[1]/div/div/div/div/div/div[1]"));
		Commons.jsScrollIntoView(target);
		
		clickEvnt = builder.click(target).build();
		clickEvnt.perform();
	}
	
	public void prodDeliveryConditionsInput(String delFee, String prodId, String remoteDelFee, String callNum) {
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(text(),\'조건부 무료\')]")));
		
		WebElement target = Main.driver.findElement(By.xpath("//div[contains(text(),\'유료\')]"));
		Commons.jsScrollIntoView(target);
		
		Actions builder = new Actions(Main.driver);
		Action clickEvnt = builder.click(target).build();
		
		clickEvnt = builder.click(target).build();
		clickEvnt.perform();
		
//		driver.findElement(By.xpath("//div[contains(text(),\'조건부 무료\')]")).click();
		
		Main.driver.findElement(By.xpath("/html/body/ui-view[1]/div[3]/div/div[3]/div/ui-view/div[2]/fieldset/form/ng-include/ui-view[16]/div[1]/div[2]/div/div[7]/div[2]/div[1]/div/div/div/div/div/input")).clear();
		Main.driver.findElement(By.xpath("/html/body/ui-view[1]/div[3]/div/div[3]/div/ui-view/div[2]/fieldset/form/ng-include/ui-view[16]/div[1]/div[2]/div/div[7]/div[2]/div[1]/div/div/div/div/div/input")).sendKeys(delFee);
		
//		driver.findElement(By.xpath("/html/body/ui-view[1]/div[3]/div/div[3]/div/ui-view/div[2]/fieldset/form/ng-include/ui-view[16]/div[1]/div[2]/div/div[7]/div[2]/div[2]/div/div[2]/div[1]/div/div[1]/input")).clear();
//		driver.findElement(By.xpath("/html/body/ui-view[1]/div[3]/div/div[3]/div/ui-view/div[2]/fieldset/form/ng-include/ui-view[16]/div[1]/div[2]/div/div[7]/div[2]/div[2]/div/div[2]/div[1]/div/div[1]/input")).sendKeys(delFee2);

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[contains(text(),\'착불 또는 선결제\')]")));
		Main.driver.findElement(By.xpath("/html/body/ui-view[1]/div[3]/div/div[3]/div/ui-view/div[2]/fieldset/form/ng-include/ui-view[16]/div[1]/div[2]/div/div[7]/div[2]/div[4]/div/div/div/label[2]")).click();
		
		Commons.clearAndSendKeyAfterElementToBeClickable(Main.driver.findElement(By.xpath("/html/body/ui-view[1]/div[3]/div/div[3]/div/ui-view/div[2]/fieldset/form/ng-include/ui-view[16]/div[1]/div[2]/div/div[7]/div[2]/div[6]/div/div/div/input")), remoteDelFee);
		
		Commons.xpathClickAfterElementToBeClickable("/html/body/ui-view[1]/div[3]/div/div[3]/div/ui-view/div[2]/fieldset/form/ng-include/ui-view[16]/div[2]/div/div/div/a");
		
		wait.until(ExpectedConditions.elementToBeClickable(By.id("return_price")));
		target = Main.driver.findElement(By.id("return_price"));
		Commons.jsScrollIntoView(target);
		
		wait.until(ExpectedConditions.elementToBeClickable(By.id("return_price")));
		Main.driver.findElement(By.xpath("/html/body/ui-view[1]/div[3]/div/div[3]/div/ui-view/div[2]/fieldset/form/ng-include/ui-view[16]/div[2]/div[2]/div/ng-include/div/div[2]/div/div/div/div/div/input")).clear();
		Main.driver.findElement(By.xpath("/html/body/ui-view[1]/div[3]/div/div[3]/div/ui-view/div[2]/fieldset/form/ng-include/ui-view[16]/div[2]/div[2]/div/ng-include/div/div[2]/div/div/div/div/div/input")).sendKeys(delFee);
		
		target = Main.driver.findElement(By.id("exchange_price"));
		Commons.jsScrollIntoView(target);
		Main.driver.findElement(By.xpath("/html/body/ui-view[1]/div[3]/div/div[3]/div/ui-view/div[2]/fieldset/form/ng-include/ui-view[16]/div[2]/div[2]/div/ng-include/div/div[3]/div/div/div/div/div/input")).clear();
		Main.driver.findElement(By.xpath("/html/body/ui-view[1]/div[3]/div/div[3]/div/ui-view/div[2]/fieldset/form/ng-include/ui-view[16]/div[2]/div[2]/div/ng-include/div/div[3]/div/div/div/div/div/input")).sendKeys(Integer.toString(Integer.parseInt(delFee)*2));
		
		// A/S, 특이사항 메뉴 펼치기
		int cnt = 0;
		while(true) {
			try {
				Commons.xpathClickAfterElementToBeClickable("//*[@id=\"productForm\"]/ng-include/ui-view[17]/div/div/div/div/a");
				wait.until(ExpectedConditions.attributeContains(By.xpath("//*[@id=\"productForm\"]/ng-include/ui-view[17]/div/div/div/div/a"), "class", "active"));
				if(cnt>5||Main.driver.findElement(By.xpath("//*[@id=\"productForm\"]/ng-include/ui-view[17]/div/div/div/div/a")).getAttribute("class").contains("active")) {
					break;
				}
			} catch (Exception e) {
				cnt++;
			}
		}

		Commons.clearAndSendKeyAfterElementToBeClickable(Main.driver.findElement(By.xpath("/html/body/ui-view[1]/div[3]/div/div[3]/div/ui-view/div[2]/fieldset/form/ng-include/ui-view[17]/div/div[2]/div/div[1]/div/div[1]/div/div/input")), callNum);
		
		Main.driver.findElement(By.xpath("/html/body/ui-view[1]/div[3]/div/div[3]/div/ui-view/div[2]/fieldset/form/ng-include/ui-view[17]/div/div[2]/div/div[2]/div/textarea")).clear();
		Main.driver.findElement(By.xpath("/html/body/ui-view[1]/div[3]/div/div[3]/div/ui-view/div[2]/fieldset/form/ng-include/ui-view[17]/div/div[2]/div/div[2]/div/textarea")).sendKeys("판매자 문의");
		
		Commons.xpathClickAfterElementToBeClickable("/html/body/ui-view[1]/div[3]/div/div[3]/div/ui-view/div[2]/fieldset/form/ng-include/ui-view[17]/div/div/div/div/a");
	}
	
	public void prodSellerCodeInput(String prodId) {
		Commons.jsScrollIntoView("/html/body/ui-view[1]/div[3]/div/div[3]/div/ui-view/div[2]/fieldset/form/ng-include/ui-view[21]/div/div/label");
		Commons.xpathClickAfterElementToBeClickable("/html/body/ui-view[1]/div[3]/div/div[3]/div/ui-view/div[2]/fieldset/form/ng-include/ui-view[21]/div/div/div/div/a");
		
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("seller-code1")));
		Main.driver.findElement(By.id("seller-code1")).clear();
		Main.driver.findElement(By.id("seller-code1")).sendKeys(prodId);
	}
	
	public String getOitemcdAfterSaveButtonClick(String prodId) {
		
		Commons.jsScrollIntoView("//div[@id=\'seller-content\']/ui-view/div[2]/div[2]/button[2]/span");
		
		String s2 = "var ch=document.querySelector('#seller-content > ui-view > div.seller-sub-content > div.seller-btn-area.btn-group-xlg.hidden-xs > button.btn.btn-primary.progress-button.progress-button-dir-horizontal.progress-button-style-top-line');"
				+ "var event = document.createEvent('HTMLEvents');"
				+ "event.initEvent('click', false, true);"
				+ "ch.dispatchEvent(event);";
		Main.js.executeScript(s2);
		
		WebElement elementEx2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div/div/div[2]/div/button[2]")));
		Commons.jsScrollIntoView(elementEx2);
		Commons.xpathClickAfterElementToBeClickable("/html/body/div[1]/div/div/div[2]/div/button[2]");
		
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),\'검색\')]")));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
				"/html/body/ui-view[1]/div[3]/div/div[3]/div/ui-view/div/ui-view[1]/div[2]/form/div[1]/div/ul/li[1]/div/div/div[2]/textarea")));

		String OITEMCD = Main.driver.findElement(By.xpath(
				"/html/body/ui-view[1]/div[3]/div/div[3]/div/ui-view/div/ui-view[1]/div[2]/form/div[1]/div/ul/li[1]/div/div/div[2]/textarea"))
				.getAttribute("value");
		System.out.println("id" + OITEMCD);
		
		return OITEMCD;
	}
	
	public String getProdInfoAfterProdInput(String oitemcd) {

		waitForProdSearchPage("이동");
		
		String s2 = "var data = {\r\n" + "searchKeywordType:'CHANNEL_PRODUCT_NO',\r\n" + "searchKeyword:'" + oitemcd
				+ "',\r\n" + "productName:'',\r\n" + "modelName:'',\r\n" + "manufacturerName:'',\r\n"
				+ "brandName:'',\r\n" + "searchPaymentType:'ALL',\r\n" + "searchPeriodType:'PROD_REG_DAY',\r\n"
				+ "deliveryAttributeType:'',\r\n" + "productKindType:'',\r\n" + "fromDate:'',\r\n" + "toDate:'',\r\n"
				+ "viewData:{\r\n" + "productStatusTypes:[\r\n" + "'SALE'\r\n" + "],\r\n" + "channelServiceTypes:[\r\n"
				+ "'STOREFARM',\r\n" + "'WINDOW',\r\n" + "'AFFILIATE',\r\n" + "''\r\n" + "],\r\n" + "pageSize:100\r\n"
				+ "},\r\n" + "searchOrderType:'REG_DATE',\r\n" + "productStatusTypes:[\r\n" + "'SALE'\r\n" + "],\r\n"
				+ "channelServiceTypes:[\r\n" + "'STOREFARM',\r\n" + "'WINDOW',\r\n" + "'AFFILIATE'\r\n" + "],\r\n"
				+ "page:0,\r\n" + "size:100,\r\n" + "sort:[\r\n" + "]\r\n" + "};" + "var st='';"
				+ "var element = document.createElement('div');\r\n" + "  element.id = \"interceptedResponse\";\r\n"
				+ "  element.appendChild(document.createTextNode(\"\"));\r\n"
				+ "  document.body.appendChild(element);\r\n" + "var xhr = new XMLHttpRequest();"
				+ "xhr.open('POST', '/api/products/list/search', true);"
				+ "xhr.setRequestHeader(\"Content-Type\", \"application/json;charset=UTF-8\");"
				+ "xhr.onreadystatechange = function() {" + "  if (xhr.readyState == 4) {"
				+ "  document.getElementById(\"interceptedResponse\").innerHTML =\r\n"
				+ "           xhr.responseText;\r\n" + "" + "  }" + "};" + "xhr.send(JSON.stringify(data));";

		// System.out.println(s2);
		Main.driver.manage().timeouts().setScriptTimeout(3, TimeUnit.SECONDS);
		try {
			Thread.sleep(1000);
			((JavascriptExecutor) Main.driver).executeAsyncScript(s2);
		} catch (ScriptTimeoutException e) {
			System.out.println("prodInfo suc ");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		WebElement sizeEle = Main.driver.findElement(By.id("interceptedResponse"));
		String beforeCount = sizeEle.getText();
		System.out.println("content:" + beforeCount);
		// wait.until(ExpectedConditions.not(ExpectedConditions.textMatches(
		// By.id("interceptedResponse"),
		// Pattern.compile(beforeCount))));
		return beforeCount;
	}
	
	public String GetPhoneNum() {
		
		String getUrl = "https://sell.smartstore.naver.com/api/sellers/account?maskApplyTypes=MEMBER&maskApplyTypes=SETTLEMENT";
		
		String jsQuery = "var xhr = new XMLHttpRequest();" 
				+ "xhr.open('GET', arguments[0], false);" 
				+ "xhr.send(null);"
				+ "return xhr.response";
		
		String response = (String) Main.js.executeScript(jsQuery, getUrl);
		
		
		JSONParser JP = new JSONParser();
		JSONObject jObj;
		String phoneNo = "";
		try {
			jObj = (JSONObject) JP.parse(response);
			JSONArray jArr = (JSONArray) jObj.get("addressBooks");
			JSONObject jObj2 = (JSONObject) jArr.get(0);
			JSONObject jObj3 = (JSONObject) jObj2.get("phoneNumber1");
			phoneNo = (String) jObj3.get("phoneNo");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(phoneNo.equals("")) {
			System.out.println("GetPhoneNum : "+phoneNo);
		}
		return phoneNo;
	}
	
	public void waitForProdInsertPage(String prodId) {
		int cnt = 0;
		try {
			while(true) {
				try {
					if(Main.driver.getCurrentUrl().contains("products/create") || cnt > 5) break;
					Main.driver.navigate().to(PRODUCT_INSERT_URL);
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),\'상품관리\')]")));
				} catch (Exception e) {
					cnt++;
				}
			}
			waitForNaverAjax();
		} catch (Exception e) {
			Commons.setTextAreaWrite("itemcd :["+prodId+"] 상품등록 페이지 이동 중 에러");
		}
	}
	
	public void waitForProdSearchPage(String prodId) {
		int cnt = 0;
		try {
			while(true) {
				try {
					if(Main.driver.getCurrentUrl().contains("products/origin-list") || cnt > 5) break;
					Main.driver.navigate().to(PRODUCT_SEARCH_URL);
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),\'상품관리\')]")));
				} catch (Exception e) {
					cnt++;
				}
			}
			try {
				waitForNaverAjax();
				Commons.xpathClickAfterElementToBeClickable("/html/body/ui-view[1]/div[3]/div/div[3]/div/ui-view/div/ui-view[1]/div[1]/ul/li[1]/a");
				waitForNaverAjax();
			} catch (Exception e) {
				
			}
		} catch (Exception e) {
			Commons.setTextAreaWrite("itemcd :["+prodId+"] 상품검색 페이지 이동 중 에러");
		}
	}
	
	public void waitForProdUpdatePage(String prodId, String naverProdId) {
		int cnt = 0;
		try {
			while(true) {
				try {
					if(Main.driver.getCurrentUrl().contains("products/edit") || cnt > 5) break;
					Main.driver.navigate().to(PRODUCT_UPDATE_URL+naverProdId);
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),\'상품관리\')]")));
				} catch (Exception e) {
					cnt++;
				}
			}
			waitForNaverAjax();
		} catch (Exception e) {
			Commons.setTextAreaWrite("itemcd :["+prodId+"] 상품수정 페이지 이동 중 에러");
		}
	}
	
	private void waitForNaverAjax() {
		wait.until(ExpectedConditions.attributeContains(By.xpath("/html/body"), "class", "pace-running"));
		wait.until(ExpectedConditions.attributeContains(By.xpath("/html/body"), "class", "pace-done"));
	}
}
