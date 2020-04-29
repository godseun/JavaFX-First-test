package application.model.service;

import java.util.ArrayList;

import application.model.dao.TranopenProdInfo;
import application.model.vo.ProductVO;

public class TranopenProdInfoService {
	TranopenProdInfo tranopenProdInfo = new TranopenProdInfo();
	
	public ArrayList<ProductVO> getTranopenProdInfoList() {
		return tranopenProdInfo.getTranopenProdInfo();
	}
	
	public void setTranopenProdInfoList(ProductVO prodVO) {
		tranopenProdInfo.setTranopenProdInfo(prodVO);
	}
}
