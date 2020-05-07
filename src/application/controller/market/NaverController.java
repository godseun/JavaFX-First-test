package application.controller.market;

import java.util.ArrayList;

import application.Main;
import application.model.Commons;
import application.model.market.naver.NaverCommon;
import application.model.vo.DeliveryCodeVO;
import application.model.vo.ProductVO;

public class NaverController {

	// TODO 상품등록후 URL 바꿔야 다음돌때 충돌안남. 등록실패시 URL 경로 옮겨가게하자
	public void individualProductRegistration(ArrayList<ProductVO> prodList, String marketName) {
		
		NaverCommon naverCommon = new NaverCommon();
		
		if(!Commons.focusAfterCheckOpenTab(marketName))
			Commons.newTab(NaverCommon.PRODUCT_INSERT_URL, marketName);
		Main.driver.navigate().refresh();
		
		if(Commons.waitUrlCheck(NaverCommon.LOGIN_URL))
			naverCommon.login();
		
		for(ProductVO prodVO : prodList) {
			if(!Main.startBtnStat) break;
			
			String parentsId = "";
			
			if(!Commons.waitUrlCheckAndGo(NaverCommon.PRODUCT_INSERT_URL)) {
				System.out.println("NaverController URL 찾기실패");
			} else {
				// TODO 상품등록로직
				try {
					naverCommon.closeAfterModalCheck();
					
					DeliveryCodeVO deliVO = Main.delCodeMap.get("네이버");
					
					String itemcd = prodVO.getITEMCD();
					parentsId = itemcd;
					String imagaes = prodVO.getIMAGAES();
					String prodName = "";
					prodName = naverCommon.createProdName(prodVO.getITEMNM());
					String fileP = "";
					fileP = Commons.getmImagePhoneartAtti(imagaes);
					String content = "";
					content = Commons.getContentImage(prodVO.getIMAGAED(), imagaes);
					String callNum = "";
					callNum = naverCommon.GetPhoneNum();
					String oitemcd = "";
					
					naverCommon.categoryInput(itemcd);
					
					naverCommon.prodNameInput(prodName);
					
					naverCommon.prodPriceInput(prodVO.getOPENSALEPRICE());
					
					naverCommon.prodStockInput("999");
					
					if(prodVO.getOPENOPTONNM().equals("Y")) {
						naverCommon.prodOptionInput(itemcd);
					}
					
					naverCommon.prodImgInput(fileP);
					
					naverCommon.prodContentInput(content);

					naverCommon.prodBrandInput(prodVO.getBNDNM());
					
					naverCommon.prodEtcInput(callNum);
					
					naverCommon.prodDeliveryConditionsInput(deliVO.getDelFee(), itemcd, deliVO.getRemoteFee(), callNum);
					
					naverCommon.prodSellerCodeInput(itemcd);
					
					oitemcd = naverCommon.getOitemcdAfterSaveButtonClick(itemcd);
					
					if(!oitemcd.equals("") && oitemcd != null) {
						
						Main.setTextArea = "itemcd :["+itemcd+"] 상품등록완료";
						try {
							// TODO: DB업데이트로직
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
					
				} catch (Exception e) {
					e.printStackTrace();
					Main.setTextArea = "itemcd :["+parentsId+"] 상품등록 중 에러";
					
					Commons.windowInit();
				} finally {
					// TODO: handle finally clause
				}
			}
		}
	}
}
