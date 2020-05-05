package application.model.service;

import java.util.Map;

import application.model.dao.DeliveryCodeInfo;
import application.model.vo.DeliveryCodeVO;

public class DeliveryCodeInfoService {
	DeliveryCodeInfo delCodeInfo = new DeliveryCodeInfo();
	
	public Map<String, DeliveryCodeVO> getDeliveryCodeInfoMap() {
		return delCodeInfo.getDeliveryCodeInfo();
	}
	
	public void setDeliveryCodeInfoMap(String marketName, DeliveryCodeVO delVO) {
		delCodeInfo.setDeliveryCodeInfo(marketName, delVO);
	}
}
