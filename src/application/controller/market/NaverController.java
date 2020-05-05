package application.controller.market;

import java.util.ArrayList;

import application.model.Commons;
import application.model.market.naver.NaverCommon;
import application.model.vo.ProductVO;

public class NaverController {

	
	static public void individualProductRegistration(ArrayList<ProductVO> prodList, String marketName) {
		Commons.newTab(NaverCommon.PRODUCT_INSERT_URL, marketName);
		if(Commons.waitUrlCheck(NaverCommon.LOGIN_URL)) {
			System.out.println("로그인필요");
			NaverCommon.login();
		}
		
		for(ProductVO prodVO : prodList) {
			if(!Commons.waitUrlCheckAndGo(NaverCommon.PRODUCT_INSERT_URL)) {
				System.out.println("NaverController URL 찾기실패");
			} else {
				// TODO 상품등록로직
			}
		}
	}
}
