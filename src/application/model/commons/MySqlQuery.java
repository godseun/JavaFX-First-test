package application.model.commons;

import java.util.ArrayList;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import application.Main;
import application.model.service.LoginAccountInfoService;
import application.model.service.TranopenProdInfoService;
import application.model.vo.LoginVO;
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
}
