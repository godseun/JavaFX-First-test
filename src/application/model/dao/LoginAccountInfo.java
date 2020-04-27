package application.model.dao;

import java.util.HashMap;
import java.util.Map;

import application.model.vo.LoginVO;

public class LoginAccountInfo {
	Map<String, LoginVO> accountMap = new HashMap<String, LoginVO>();
	
	public void setLoginAccountInfo(String marketName, LoginVO loginVO) {
		accountMap.put(marketName, loginVO);
	}
	
	public Map<String, LoginVO> getLoginAccountInfo() {
		return accountMap;
	}
}
