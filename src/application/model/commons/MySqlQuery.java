package application.model.commons;

import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import application.Main;
import application.model.service.LoginAccountInfoService;
import application.model.vo.LoginVO;

public class MySqlQuery {
	
	static public Map<String, LoginVO> SelectLoginInfo() {
		
		LoginAccountInfoService loginInfoService = new LoginAccountInfoService();
		
		String sql = "";
		sql = "SELECT `쇼핑몰명` AS MARKETNAME"
				+ ",`아이디` AS LOGINID"
				+ ",`PW` AS LOGINPW"
				+ ",`판매자URL` AS SELLERURL"
				+ ",`사용여부` AS USEYN"
				+ ",`일시정지` AS PAUSEYN"
				+ ",`메모` AS MEMO "
				+ " FROM "+Main.DB+".쇼핑몰로그인정보";

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
				loginVO.setPauseYN(jObj.get("PAUSEYN").toString());
				loginVO.setMemo(jObj.get("MEMO").toString());
				
				loginInfoService.setLoginAccountInfo(jObj.get("MARKETNAME").toString(), loginVO);
				
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return loginInfoService.getLoginAccountInfo();
	}
}
