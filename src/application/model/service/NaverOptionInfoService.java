package application.model.service;

import java.util.ArrayList;

import application.model.dao.NaverOptionInfo;
import application.model.vo.NaverOptionVO;

public class NaverOptionInfoService {
	NaverOptionInfo optionInfo = new NaverOptionInfo();
	
	public ArrayList<NaverOptionVO> getNaverOptionInfoVO() {
		return optionInfo.getNaverOptionInfo();
	}
	
	public void setNaverOptionInfoVO(NaverOptionVO optionVO) {
		optionInfo.setNaverOptionInfo(optionVO);
	}
}
