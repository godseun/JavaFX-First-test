package application.model.commons;

import java.util.ArrayList;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import application.Main;
import application.model.service.DeliveryCodeInfoService;
import application.model.service.LoginAccountInfoService;
import application.model.service.NaverOptionInfoService;
import application.model.service.TranopenProdInfoService;
import application.model.vo.DeliveryCodeVO;
import application.model.vo.LoginVO;
import application.model.vo.NaverOptionVO;
import application.model.vo.ProductVO;

public class MySqlQuery {
	
	static public Map<String, LoginVO> selectLoginInfoMap() {
		
		LoginAccountInfoService loginInfoService = new LoginAccountInfoService();
		
		String sql = "";
		sql = "SELECT `쇼핑몰명` AS MARKETNAME"
				+ ",`아이디` AS LOGINID"
				+ ",`PW` AS LOGINPW"
				+ ",`판매자URL` AS SELLERURL"
				+ ",`사용여부` AS USEYN"
				+ ",`일시정지` AS PAUSEYN"
				+ ",`메모` AS MEMO "
				+ " FROM "+Main.DB+".쇼핑몰로그인정보"
				+ " WHERE `사용여부` = 'Y' ; ";

		String resultSet = "";
		resultSet = ConnectServerInterface.ExecuteSql(sql);
		
		JSONParser jsonParser = new JSONParser();
		try {
			JSONArray jArr = (JSONArray) jsonParser.parse(resultSet);
			for (int i = 0; i < jArr.size(); i++) {
				
				JSONObject jObj = (JSONObject) jArr.get(i);
				
				LoginVO loginVO = new LoginVO();
				loginVO.setMarketName(jObj.get("MARKETNAME").toString());
				loginVO.setId(jObj.get("LOGINID").toString());
				loginVO.setPw(jObj.get("LOGINPW").toString());
				loginVO.setSellerURL(jObj.get("SELLERURL").toString());
				loginVO.setUseYN(jObj.get("USEYN").toString());
				loginVO.setPauseYN((jObj.get("PAUSEYN")!=null)?jObj.get("PAUSEYN").toString():"");
				loginVO.setMemo(jObj.get("MEMO").toString());
				
				loginInfoService.setLoginAccountInfoMap(jObj.get("MARKETNAME").toString(), loginVO);
				
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return loginInfoService.getLoginAccountInfoMap();
	}
	
	static public ArrayList<ProductVO> selectProdInfoArrayMap(String marketName, String status) {
		
		String market = "";
		switch (marketName) {
			case "네이버": market = "naver"; break;
			case "쿠팡": market = "coopang"; break;
			case "티몬": market = "tmon"; break;
			case "위메프": market = "wemef"; break;
			case "11번가": market = "11st"; break;
			case "인터파크": market = "interpark"; break;
			case "지마켓": market = "gmarket"; break;
				
			default:break;
		}
		
		TranopenProdInfoService tranopenService = new TranopenProdInfoService();
		
		String sql = "";
		sql = "SELECT ITEMCD,"
					+ "ITEMNM,"
					+ "OPENNMAE,"
					+ "BNDNM,"
					+ "STDCO,"
					+ "STDPRICE,"
					+ "OPENMARKET,"
					+ "OPENSTATUS,"
					+ "OPENSALEPRICE,"
					+ "OPENOPTONNM,"
					+ "IMAGAES,"
					+ "IMAGAED,"
					+ "SENDYN,"
					+ "RESULTYN,"
					+ "TTIME,"
					+ "RTIME,"
					+ "DTIME,"
					+ "ERMSG  "
				+ " FROM "+Main.DB+".TRANOPEN "
				+ " WHERE  OPENMARKET='"+marketName+"' "
						+ " AND SENDYN='Y' "
						+ " AND RESULTYN='' "
						+ " AND OPENSTATUS='"+status+"' "
						+ " AND ITEMCD NOT IN (SELECT ITEMCD FROM "+Main.DB+".`"+market+"pro`) LIMIT 100; ";
		
		String resultSet = "";
		resultSet = ConnectServerInterface.ExecuteSql(sql);
		
		JSONParser jsonParser = new JSONParser();
		try {
			if(!resultSet.equals("[]")) {
				if(!resultSet.contains("CHK")) {
					
					JSONArray jArr = (JSONArray) jsonParser.parse(resultSet);
					for (int i = 0; i < jArr.size(); i++) {
						
						JSONObject jObj = (JSONObject) jArr.get(i);
						
						ProductVO prodVO = new ProductVO();
						prodVO.setITEMCD(jObj.get("ITEMCD").toString());
						prodVO.setITEMNM(jObj.get("ITEMNM").toString());
						prodVO.setOPENNMAE(jObj.get("OPENNMAE").toString());
						prodVO.setBNDNM(jObj.get("BNDNM").toString());
						prodVO.setSTDCO(jObj.get("STDCO").toString());
						prodVO.setSTDPRICE(jObj.get("STDPRICE").toString());
						prodVO.setOPENMARKET(jObj.get("OPENMARKET").toString());
						prodVO.setOPENSTATUS(jObj.get("OPENSTATUS").toString());
						prodVO.setOPENSALEPRICE(jObj.get("OPENSALEPRICE").toString());
						prodVO.setOPENOPTONNM(jObj.get("OPENOPTONNM").toString());
						prodVO.setIMAGAES(jObj.get("IMAGAES").toString());
						prodVO.setIMAGAED(jObj.get("IMAGAED").toString());
						prodVO.setSENDYN(jObj.get("SENDYN").toString());
						prodVO.setRESULTYN(jObj.get("RESULTYN").toString());
						prodVO.setTTIME(jObj.get("TTIME").toString());
						prodVO.setRTIME(jObj.get("RTIME").toString());
						prodVO.setDTIME(jObj.get("DTIME").toString());
						prodVO.setERMSG(jObj.get("ERMSG").toString());
						
						tranopenService.setTranopenProdInfoList(prodVO);
					}
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return tranopenService.getTranopenProdInfoList();
	}
	
	static public Map<String, DeliveryCodeVO> selectDelcompcodeInfoArrayMap() {

		String sql = "";
		sql = "SELECT COMPCODE,COMPNAME,MARKETNAME,DELCODE,"
				+ " DELCOMPNAME,CRETECDELCODE,CRETECDELCOMPNAME,"
				+ " DEFUALTYN,DELFEE,REMOTEFEE"
				+ " FROM "+Main.DB+".delcompCode WHERE defualtYn = 'Y' ; ";
		
		String resultSet = "";
		resultSet = ConnectServerInterface.ExecuteSql(sql);
		
		DeliveryCodeInfoService deliveryCodeService = new DeliveryCodeInfoService();
		
		JSONParser jsonParser = new JSONParser();
		try {
			if(!resultSet.equals("[]")) {
				if(!resultSet.contains("CHK")) {
					JSONArray jArr = (JSONArray) jsonParser.parse(resultSet);
					for (int i = 0; i < jArr.size(); i++) {
						JSONObject jObj = (JSONObject) jArr.get(i);
						
						DeliveryCodeVO delVO = new DeliveryCodeVO();
						delVO.setCompCode(jObj.get("COMPCODE").toString());
						delVO.setCompName(jObj.get("COMPNAME").toString());
						delVO.setMarketName(jObj.get("MARKETNAME").toString());
						delVO.setDelCode((jObj.get("DELCODE") != null)?jObj.get("DELCODE").toString():"");
						delVO.setDelcompName((jObj.get("DELCOMPNAME") != null)?jObj.get("DELCOMPNAME").toString():"");
						delVO.setCretecDelCode((jObj.get("CRETECDELCODE") != null)?jObj.get("CRETECDELCODE").toString():"");
						delVO.setCretecDelcompName((jObj.get("CRETECDELCOMPNAME") != null)?jObj.get("CRETECDELCOMPNAME").toString():"");
						delVO.setDefaultYn(jObj.get("DEFUALTYN").toString());
						delVO.setDelFee(jObj.get("DELFEE").toString());
						delVO.setRemoteFee(jObj.get("REMOTEFEE").toString());
						
						deliveryCodeService.setDeliveryCodeInfoMap(jObj.get("MARKETNAME").toString(), delVO);
					}
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return deliveryCodeService.getDeliveryCodeInfoMap();
	}
	
	static public ArrayList<NaverOptionVO> naverSearchOption(String itemcd) {
		
		String sql = "";
		sql = "SELECT MITEMCD,ITEMCD,OPTTYPE,OPTNM,OPTPRICE,SALEYN"
				+ " FROM "+Main.DB+".naverOption WHERE MITEMCD = '"+itemcd+"' ; ";
		
		String resultSet = "";
		resultSet = ConnectServerInterface.ExecuteSql(sql);
		
		NaverOptionInfoService naverOptionService = new NaverOptionInfoService();
		
		JSONParser jsonParser = new JSONParser();
		try {
			if(!resultSet.equals("[]")) {
				if(!resultSet.contains("CHK")) {
					JSONArray jArr = (JSONArray) jsonParser.parse(resultSet);
					for (int i = 0; i < jArr.size(); i++) {
						JSONObject jObj = (JSONObject) jArr.get(i);
						
						NaverOptionVO optionVO = new NaverOptionVO();
						optionVO.setmItemCd(jObj.get("MITEMCD").toString());
						optionVO.setItemCd(jObj.get("ITEMCD").toString());
						optionVO.setOptType(jObj.get("OPTTYPE").toString());
						optionVO.setOptNm(jObj.get("OPTNM").toString());
						optionVO.setOptPrice(jObj.get("OPTPRICE").toString());
						optionVO.setSaleYn(jObj.get("SALEYN").toString());
						
						naverOptionService.setNaverOptionInfoVO(optionVO);
					}
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return naverOptionService.getNaverOptionInfoVO();
	}
	
	static public void tranopenUpdateSend(String RESULTYN, String ttime, String itemcd, String rtime, String marketName) {

		String sql = "UPDATE "+Main.DB+".TRANOPEN SET OPENSTATUS='등록완료', SENDYN='I',RESULTYN='" + RESULTYN
				+ "',RTIME=getdate() WHERE ITEMCD='" + itemcd + "' and TTIME='" + ttime + "' and OPENMARKET='"+marketName+"' and SENDYN='Y' ;";
		System.out.println("tranopenUpdateSend "+itemcd);
//		ConnectServerInterface.ExecuteSql(sql);
	}
	
	static public void naverProSelectReg(String itemcd, String oitemcd, String ttime, String rtime, String dttime, String status,
			String errmsg, String pprice) {

		String sql = "SELECT * FROM "+Main.DB+".NAVERPRO WHERE ITEMCD='" + itemcd + "' ;";

		String sqlResult = "";

		sqlResult = ConnectServerInterface.ExecuteSql(sql);
		
		JSONParser jsonParse = new JSONParser();
		JSONArray jArr;
		
		try {

			jArr = (JSONArray) jsonParse.parse(sqlResult);

			String resultCheck = jArr.toString();
			
			if(resultCheck.equals("[]")) {
				System.out.println("insertReg");
				naverProInsertReg(itemcd, oitemcd, ttime, rtime, dttime, status, errmsg, pprice);
			} else {
				System.out.println("updateReg");
				naverProUpdateReg(itemcd, oitemcd, ttime, rtime, dttime, status, errmsg, pprice);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		// System.out.println("json"+json);
	}
	
	public static void naverProInsertReg(String itemcd, String oitemcd, String ttime, String rtime, String dttime, String status,
			String errmsg, String pprice) {

		String sql = "";
		sql = "INSERT INTO "+Main.DB+".NAVERPRO\n" + "(ITEMCD\n" + ",OITEMCD\n" + ",TTIME\n" + ",RTIME\n"
				+ ",DTTIME\n" + ",STATUS\n" + ",ERRMSG\n" + ",PPRICE)\n" + "VALUES\n" + "('" + itemcd + "'\n"
				+ ",'" + oitemcd + "'\n" + ",'" + ttime + "'\n" + ",getdate()\n" + ",'" + dttime + "'\n" + ",'" + status
				+ "'\n" + ",'" + errmsg + "'\n" + "," + pprice + ")\n";
		System.out.println("naverProInsertReg "+itemcd);
//		ConnectServerInterface.ExecuteSql(sql);
	}
	
	public static void naverProUpdateReg(String itemcd, String oitemcd, String ttime, String rtime, String dttime, String status,
			String errmsg, String pprice) {

		String sql = "";
		sql = "UPDATE "+Main.DB+".NAVERPRO " + " SET TTIME = '" + ttime + "' " + ",RTIME = getdate() "
				+ ",DTTIME = '" + dttime + "' " + ",STATUS = '" + status + "' " + ",ERRMSG = '" + errmsg + "' "
				+ ",PPRICE = " + pprice + " " + " WHERE ITEMCD='" + itemcd + "';";
		System.out.println("naverProUpdateReg "+itemcd);
//		ConnectServerInterface.ExecuteSql(sql);
	}
	
	static public void naverProUpdateSendOitemcd(String oitemcd, String jResult) {

		try {

			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObj = (JSONObject) jsonParser.parse(jResult);
			JSONArray jr = (JSONArray) jsonObj.get("content");
			JSONObject jsonObject = (JSONObject) jr.get(0);
			long salePrice = 0;
			String productName = "";
			String representImageUrl = "";
			long discountedSalePrice = 0;
			long sellerImmediateDiscountAmount = 0;
			long mobileDiscountedSalePrice = 0;
			long mobileSellerImmediateDiscountAmount = 0;
			String mobileSellerImmediateDiscountText = "";
			String bundleGroupName = "";
			String category1Name = "";
			String category2Name = "";
			String category3Name = "";
			String category4Name = "";
			String wholeCategoryId = "";
			
			if (true == jsonObject.containsKey("salePrice")) {
				salePrice = (Long) jsonObject.get("salePrice");
			}
			if (true == jsonObject.containsKey("productName")) {
				productName = (String) jsonObject.get("productName");
			}
			if (true == jsonObject.containsKey("representImageUrl")) {
				representImageUrl = (String) jsonObject.get("representImageUrl");
			}
			if (true == jsonObject.containsKey("discountedSalePrice")) {
				discountedSalePrice = (Long) jsonObject.get("discountedSalePrice");
			}
			if (true == jsonObject.containsKey("sellerImmediateDiscountAmount")) {
				sellerImmediateDiscountAmount = (Long) jsonObject.get("sellerImmediateDiscountAmount");
			}
			if (true == jsonObject.containsKey("mobileDiscountedSalePrice")) {
				mobileDiscountedSalePrice = (Long) jsonObject.get("mobileDiscountedSalePrice");
			}
			if (true == jsonObject.containsKey("mobileSellerImmediateDiscountAmount")) {
				mobileSellerImmediateDiscountAmount = (Long) jsonObject.get("mobileSellerImmediateDiscountAmount");
			}
			if (true == jsonObject.containsKey("mobileSellerImmediateDiscountText")) {
				mobileSellerImmediateDiscountText = (String) jsonObject.get("mobileSellerImmediateDiscountText");
			}
			if (true == jsonObject.containsKey("bundleGroupName")) {
				bundleGroupName = (String) jsonObject.get("bundleGroupName");
			}
			if (true == jsonObject.containsKey("category1Name")) {
				category1Name = (String) jsonObject.get("category1Name");
			}
			if (true == jsonObject.containsKey("category2Name")) {
				category2Name = (String) jsonObject.get("category2Name");
			}
			if (true == jsonObject.containsKey("category3Name")) {
				category3Name = (String) jsonObject.get("category3Name");
			}
			if (true == jsonObject.containsKey("category4Name")) {
				category4Name = (String) jsonObject.get("category4Name");
			}
			if (true == jsonObject.containsKey("wholeCategoryId")) {
				wholeCategoryId = (String) jsonObject.get("wholeCategoryId");
			}

			String sql = "UPDATE " + Main.DB + ".NAVERPRO\n" + "   SET PPRICE = " + salePrice + "\n"
					+ "      ,productName = '" + productName + "'\n" + "      ,representImageUrl = '"
					+ representImageUrl + "'\n" + "      ,discountedSalePrice = " + discountedSalePrice + "\n"
					+ "      ,sellerImmediateDiscountAmount = " + sellerImmediateDiscountAmount + "\n"
					+ "      ,mobileDiscountedSalePrice = " + mobileDiscountedSalePrice + "\n"
					+ "      ,mobileSellerImmediateDiscountAmount = " + mobileSellerImmediateDiscountAmount + "\n"
					+ "      ,mobileSellerImmediateDiscountText = '" + mobileSellerImmediateDiscountText + "'\n"
					+ "      ,bundleGroupName = '" + bundleGroupName + "'\n" + "      ,category1Name = '"
					+ category1Name + "'\n" + "      ,category2Name = '" + category2Name + "'\n"
					+ "      ,category3Name = '" + category3Name + "'\n" + "      ,category4Name = '"
					+ category4Name + "'\n" + "      ,wholeCategoryId = '" + wholeCategoryId + "'\n"
					+ " WHERE OITEMCD='" + oitemcd + "';";
			System.out.println("naverProUpdateSendOitemcd "+oitemcd);
//			ConnectServerInterface.ExecuteSql(sql);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static public void naverProUpdateSendItemcd(String itemcd, String jResult) {

		try {

			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObj = (JSONObject) jsonParser.parse(jResult);
			JSONArray jr = (JSONArray) jsonObj.get("content");
			JSONObject jsonObject = (JSONObject) jr.get(0);
			long oitemcd = 0;
			long salePrice = 0;
			String productName = "";
			String representImageUrl = "";
			long discountedSalePrice = 0;
			long sellerImmediateDiscountAmount = 0;
			long mobileDiscountedSalePrice = 0;
			long mobileSellerImmediateDiscountAmount = 0;
			String mobileSellerImmediateDiscountText = "";
			String bundleGroupName = "";
			String category1Name = "";
			String category2Name = "";
			String category3Name = "";
			String category4Name = "";
			String wholeCategoryId = "";
			
			if (true == jsonObject.containsKey("storefarmChannelProductNo")) {
				oitemcd = (Long) jsonObject.get("storefarmChannelProductNo");
			}
			if (true == jsonObject.containsKey("salePrice")) {
				salePrice = (Long) jsonObject.get("salePrice");
			}
			if (true == jsonObject.containsKey("productName")) {
				productName = (String) jsonObject.get("productName");
			}
			if (true == jsonObject.containsKey("representImageUrl")) {
				representImageUrl = (String) jsonObject.get("representImageUrl");
			}
			if (true == jsonObject.containsKey("discountedSalePrice")) {
				discountedSalePrice = (Long) jsonObject.get("discountedSalePrice");
			}
			if (true == jsonObject.containsKey("sellerImmediateDiscountAmount")) {
				sellerImmediateDiscountAmount = (Long) jsonObject.get("sellerImmediateDiscountAmount");
			}
			if (true == jsonObject.containsKey("mobileDiscountedSalePrice")) {
				mobileDiscountedSalePrice = (Long) jsonObject.get("mobileDiscountedSalePrice");
			}
			if (true == jsonObject.containsKey("mobileSellerImmediateDiscountAmount")) {
				mobileSellerImmediateDiscountAmount = (Long) jsonObject.get("mobileSellerImmediateDiscountAmount");
			}
			if (true == jsonObject.containsKey("mobileSellerImmediateDiscountText")) {
				mobileSellerImmediateDiscountText = (String) jsonObject.get("mobileSellerImmediateDiscountText");
			}
			if (true == jsonObject.containsKey("bundleGroupName")) {
				bundleGroupName = (String) jsonObject.get("bundleGroupName");
			}
			if (true == jsonObject.containsKey("category1Name")) {
				category1Name = (String) jsonObject.get("category1Name");
			}
			if (true == jsonObject.containsKey("category2Name")) {
				category2Name = (String) jsonObject.get("category2Name");
			}
			if (true == jsonObject.containsKey("category3Name")) {
				category3Name = (String) jsonObject.get("category3Name");
			}
			if (true == jsonObject.containsKey("category4Name")) {
				category4Name = (String) jsonObject.get("category4Name");
			}
			if (true == jsonObject.containsKey("wholeCategoryId")) {
				wholeCategoryId = (String) jsonObject.get("wholeCategoryId");
			}

			String sql = "UPDATE " + Main.DB + ".NAVERPRO\n" + "   SET OITEMCD = '"+oitemcd+"', PPRICE = " + salePrice + "\n"
					+ "      ,productName = '" + productName + "'\n" + "      ,representImageUrl = '"
					+ representImageUrl + "'\n" + "      ,discountedSalePrice = " + discountedSalePrice + "\n"
					+ "      ,sellerImmediateDiscountAmount = " + sellerImmediateDiscountAmount + "\n"
					+ "      ,mobileDiscountedSalePrice = " + mobileDiscountedSalePrice + "\n"
					+ "      ,mobileSellerImmediateDiscountAmount = " + mobileSellerImmediateDiscountAmount + "\n"
					+ "      ,mobileSellerImmediateDiscountText = '" + mobileSellerImmediateDiscountText + "'\n"
					+ "      ,bundleGroupName = '" + bundleGroupName + "'\n" + "      ,category1Name = '"
					+ category1Name + "'\n" + "      ,category2Name = '" + category2Name + "'\n"
					+ "      ,category3Name = '" + category3Name + "'\n" + "      ,category4Name = '"
					+ category4Name + "'\n" + "      ,wholeCategoryId = '" + wholeCategoryId + "'\n"
					+ " WHERE ITEMCD='" + itemcd + "';";
			System.out.println("naverProUpdateSendItemcd "+itemcd);
//			System.out.println(sql);
//			ConnectServerInterface.ExecuteSql(sql);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
