package application.model.dao;

import java.util.ArrayList;

import application.model.vo.NaverOptionVO;

public class NaverOptionInfo {
	ArrayList<NaverOptionVO> optionList = new ArrayList<NaverOptionVO>();
	
	public void setNaverOptionInfo(NaverOptionVO optionVO) {
		optionList.add(optionVO);
	}
	
	public ArrayList<NaverOptionVO> getNaverOptionInfo() {
		return optionList;
	}
}
