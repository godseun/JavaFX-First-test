package application.controller.market;

import application.Main;
import application.model.Commons;
import application.model.market.naver.NaverCommon;

public class NaverController {

	public void login() {
		
	}
	
	static public void individualProductRegistration() {
		Commons.newTab(NaverCommon.PRODUCT_INSERT_URL);
		System.out.println(Main.driver.getCurrentUrl());
	}
}
