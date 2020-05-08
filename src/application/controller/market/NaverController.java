package application.controller.market;

import java.util.ArrayList;

import application.Main;
import application.model.Commons;
import application.model.commons.MySqlQuery;
import application.model.market.naver.NaverCommon;
import application.model.vo.DeliveryCodeVO;
import application.model.vo.ProductVO;

public class NaverController {
	// TODO: DB업데이트 주석처리 풀어야함
	public void individualProductRegistration(ArrayList<ProductVO> prodList, String marketName) {
		
		NaverCommon naverCommon = new NaverCommon();
		
		if(!Commons.focusAfterCheckOpenTab(marketName))
			Commons.newTab(NaverCommon.PRODUCT_INSERT_URL, marketName);
		Main.driver.navigate().refresh();
		
		if(Commons.waitUrlCheck(NaverCommon.LOGIN_URL))
			naverCommon.login();
		
		for(ProductVO prodVO : prodList) {
			if(!Main.startBtnStat) break;
			
			if(!Commons.waitUrlCheckAndGo(NaverCommon.PRODUCT_INSERT_URL)) {
				System.out.println("NaverController URL 찾기실패");
			} else {
				String itemcd = prodVO.getITEMCD();
				try {
					boolean marketRegCheck;
					try {
						marketRegCheck = naverCommon.marketForItemcdCheck(itemcd);
					} catch (Exception e) {
						Commons.setTextAreaWrite(marketName+" 상품등록 중 에러 itemcd :["+itemcd+"] ");
						continue;
					}
					
					String price = prodVO.getOPENSALEPRICE();
					String ttime = prodVO.getTTIME();
					String rtime = prodVO.getRTIME();
					String dtime = prodVO.getDTIME();
					
					if(marketRegCheck) {
						naverCommon.closeAfterModalCheck();
						
						DeliveryCodeVO deliVO = Main.delCodeMap.get("네이버");
						String oitemcd = "";
						String callNum = "";
						callNum = naverCommon.GetPhoneNum();
						String imagaes = prodVO.getIMAGAES();
						String prodName = "";
						prodName = naverCommon.createProdName(prodVO.getITEMNM());
						String fileP = "";
						fileP = Commons.getmImagePhoneartAtti(imagaes);
						String content = "";
						content = Commons.getContentImage(prodVO.getIMAGAED(), imagaes);
						
						naverCommon.categoryInput(itemcd);
						
						naverCommon.prodNameInput(prodName);
						
						naverCommon.prodPriceInput(price);
						
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
							Commons.setTextAreaWrite(marketName+" 상품등록완료 itemcd :["+itemcd+"] ");
							
							MySqlQuery.tranopenUpdateSend("Y", ttime, itemcd, rtime, marketName);
							MySqlQuery.naverProSelectReg(itemcd, oitemcd, ttime, rtime, dtime, "등록완료", "", price);
							
							naverCommon.waitForProdSearchPage(marketName);
							MySqlQuery.naverProUpdateSendOitemcd(oitemcd, naverCommon.getProdInfoAfterProdInput("oitemcd",oitemcd));
						}
					} else {
						Commons.setTextAreaWrite(marketName+" 이미 등록 된 상품 itemcd :["+itemcd+"] ");
						
						MySqlQuery.tranopenUpdateSend("Y", ttime, itemcd, rtime, marketName);
						MySqlQuery.naverProSelectReg(itemcd, "", ttime, rtime, dtime, "등록완료", "", price);
						naverCommon.waitForProdSearchPage(marketName);
						MySqlQuery.naverProUpdateSendItemcd(itemcd, naverCommon.getProdInfoAfterProdInput("itemcd",itemcd));
					}
				} catch (Exception e) {
					e.printStackTrace();
					Commons.setTextAreaWrite(marketName+" 상품등록 중 에러 itemcd :["+itemcd+"] ");
					Commons.windowInit();
				}
			}
		}
	}
	
	public void individualProductModification(ArrayList<ProductVO> prodList, String marketName) {
		
		NaverCommon naverCommon = new NaverCommon();
		
		if(!Commons.focusAfterCheckOpenTab(marketName))
			Commons.newTab(NaverCommon.PRODUCT_SEARCH_URL, marketName);
		Main.driver.navigate().refresh();
		
		if(Commons.waitUrlCheck(NaverCommon.LOGIN_URL))
			naverCommon.login();
		
		for(ProductVO prodVO : prodList) {
			if(!Main.startBtnStat) break;
			naverCommon.waitForProdSearchPage(marketName);
		}
	}
}
