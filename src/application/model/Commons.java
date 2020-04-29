package application.model;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import application.Main;

public class Commons {

	static public void newTab(String url) {
		Main.driver.switchTo().window(url);
//		Main.js.executeScript("window.open('"+url+"')");
//		WebDriverWait wait = new WebDriverWait(Main.driver, 10);
//		wait.until(ExpectedConditions.urlContains(""));
	}
}
