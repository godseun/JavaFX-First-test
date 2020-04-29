package application.model.service;

import java.util.Map;

import application.model.dao.LoginAccountInfo;
import application.model.vo.LoginVO;

public class LoginAccountInfoService {
	LoginAccountInfo loginAI = new LoginAccountInfo();
	
	public Map<String, LoginVO> getLoginAccountInfoMap() {
		return loginAI.getLoginAccountInfo();
	}
	
	public void setLoginAccountInfoMap(String marketName, LoginVO loginVO) {
		loginAI.setLoginAccountInfo(marketName, loginVO);
	}
}
