package application.model.dao;

import java.util.HashMap;
import java.util.Map;

import application.model.vo.DeliveryCodeVO;

public class DeliveryCodeInfo {
	Map<String, DeliveryCodeVO> delCodeMap = new HashMap<String, DeliveryCodeVO>();
	
	public void setDeliveryCodeInfo(String marketName, DeliveryCodeVO delVO) {
		delCodeMap.put(marketName, delVO);
	}
	
	public Map<String, DeliveryCodeVO> getDeliveryCodeInfo() {
		return delCodeMap;
	}
}
