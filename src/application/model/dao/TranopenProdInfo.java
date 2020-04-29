package application.model.dao;

import java.util.ArrayList;

import application.model.vo.ProductVO;

public class TranopenProdInfo {
	ArrayList<ProductVO> tranopenList = new ArrayList<ProductVO>();
	
	public void setTranopenProdInfo(ProductVO prodVO) {
		tranopenList.add(prodVO);
	}
	
	public ArrayList<ProductVO> getTranopenProdInfo() {
		return tranopenList;
	}

}
