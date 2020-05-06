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
	
	static public Map<String, LoginVO> SelectLoginInfoMap() {
		
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
	
	static public ArrayList<ProductVO> SelectProdInfoArrayMap(String marketName, String status) {
		
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
						+ " AND ITEMCD NOT IN (SELECT ITEMCD FROM "+Main.DB+".`"+market+"pro`) LIMIT 1; ";
		// TODO limit 해제하자
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
	
	static public Map<String, DeliveryCodeVO> SelectDelcompcodeInfoArrayMap() {

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
	
	static public ArrayList<NaverOptionVO> searchOption(String itemcd) {
		
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
}
