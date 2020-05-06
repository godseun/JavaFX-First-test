package application.model.vo;

public class DeliveryCodeVO {

	private String compCode;
	private String compName;
	private String marketName;
	private String delCode;
	private String delcompName;
	private String cretecDelCode;
	private String cretecDelcompName;
	private String defaultYn;
	private String delFee;
	private String remoteFee;
	
	public String getCompCode() {
		return compCode;
	}
	public void setCompCode(String compCode) {
		this.compCode = compCode;
	}
	public String getCompName() {
		return compName;
	}
	public void setCompName(String compName) {
		this.compName = compName;
	}
	public String getMarketName() {
		return marketName;
	}
	public void setMarketName(String marketName) {
		this.marketName = marketName;
	}
	public String getDelCode() {
		return delCode;
	}
	public void setDelCode(String delCode) {
		this.delCode = delCode;
	}
	public String getDelcompName() {
		return delcompName;
	}
	public void setDelcompName(String delcompName) {
		this.delcompName = delcompName;
	}
	public String getCretecDelCode() {
		return cretecDelCode;
	}
	public void setCretecDelCode(String cretecDelCode) {
		this.cretecDelCode = cretecDelCode;
	}
	public String getCretecDelcompName() {
		return cretecDelcompName;
	}
	public void setCretecDelcompName(String cretecDelcompName) {
		this.cretecDelcompName = cretecDelcompName;
	}
	public String getDefaultYn() {
		return defaultYn;
	}
	public void setDefaultYn(String defaultYn) {
		this.defaultYn = defaultYn;
	}
	public String getDelFee() {
		return delFee;
	}
	public void setDelFee(String delFee) {
		this.delFee = delFee;
	}
	public String getRemoteFee() {
		return remoteFee;
	}
	public void setRemoteFee(String remoteFee) {
		this.remoteFee = remoteFee;
	}
	
	@Override
	public String toString() {
		return "DeliveryCodeVO [compCode=" + compCode + ", compName=" + compName + ", marketName=" + marketName
				+ ", delCode=" + delCode + ", delcompName=" + delcompName + ", cretecDelCode=" + cretecDelCode
				+ ", cretecDelcompName=" + cretecDelcompName + ", defaultYn=" + defaultYn + ", delFee=" + delFee
				+ ", remoteFee=" + remoteFee + "]";
	}
	
}
