package application.controller.market;

import java.util.ArrayList;

import application.model.Commons;
import application.model.market.naver.NaverCommon;
import application.model.vo.ProductVO;

public class NaverController {

	// TODO 상품등록후 URL 바꿔야 다음돌때 충돌안남. 등록실패시 URL 경로 옮겨가게하자
	static public void individualProductRegistration(ArrayList<ProductVO> prodList, String marketName) {
		
		if(!Commons.focusAfterCheckOpenTab(marketName))
			Commons.newTab(NaverCommon.PRODUCT_INSERT_URL, marketName);
		
		if(Commons.waitUrlCheck(NaverCommon.LOGIN_URL))
			NaverCommon.login();
		
		for(ProductVO prodVO : prodList) {
			if(!Commons.waitUrlCheckAndGo(NaverCommon.PRODUCT_INSERT_URL)) {
				System.out.println("NaverController URL 찾기실패");
			} else {
				// TODO 상품등록로직
			}
		}
	}
}
