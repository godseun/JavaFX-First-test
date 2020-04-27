package application.model.service;

import java.util.Map;

import application.model.dao.LoginAccountInfo;
import application.model.vo.LoginVO;

public class LoginAccountInfoService {
	LoginAccountInfo loginAI = new LoginAccountInfo();
	
	public Map<String, LoginVO> getLoginAccountInfo() {
		return loginAI.getLoginAccountInfo();
	}
	
	public void setLoginAccountInfo(String marketName, LoginVO loginVO) {
		loginAI.setLoginAccountInfo(marketName, loginVO);
	}
}
