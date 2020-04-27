package application.model.vo;

public class LoginVO {

	private String marketName;
	private String id;
	private String pw;
	private String sellerURL;
	private String useYN;
	private String pauseYN;
	private String memo;
	
	public String getMarketName() {
		return marketName;
	}
	public void setMarketName(String marketName) {
		this.marketName = marketName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPw() {
		return pw;
	}
	public void setPw(String pw) {
		this.pw = pw;
	}
	public String getSellerURL() {
		return sellerURL;
	}
	public void setSellerURL(String sellerURL) {
		this.sellerURL = sellerURL;
	}
	public String getUseYN() {
		return useYN;
	}
	public void setUseYN(String useYN) {
		this.useYN = useYN;
	}
	public String getPauseYN() {
		return pauseYN;
	}
	public void setPauseYN(String pauseYN) {
		this.pauseYN = pauseYN;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	@Override
	public String toString() {
		return "LoginVO [marketName=" + marketName + ", id=" + id + ", pw=" + pw + ", sellerURL=" + sellerURL
				+ ", useYN=" + useYN + ", pauseYN=" + pauseYN + ", memo=" + memo + "]";
	}
	
}
