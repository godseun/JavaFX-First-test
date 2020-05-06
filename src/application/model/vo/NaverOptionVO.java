package application.model.vo;

public class NaverOptionVO {

	private String mItemCd;
	private String itemCd;
	private String optType;
	private String optNm;
	private String optPrice;
	private String saleYn;
	
	public String getmItemCd() {
		return mItemCd;
	}
	public void setmItemCd(String mItemCd) {
		this.mItemCd = mItemCd;
	}
	public String getItemCd() {
		return itemCd;
	}
	public void setItemCd(String itemCd) {
		this.itemCd = itemCd;
	}
	public String getOptType() {
		return optType;
	}
	public void setOptType(String optType) {
		this.optType = optType;
	}
	public String getOptNm() {
		return optNm;
	}
	public void setOptNm(String optNm) {
		this.optNm = optNm;
	}
	public String getOptPrice() {
		return optPrice;
	}
	public void setOptPrice(String optPrice) {
		this.optPrice = optPrice;
	}
	public String getSaleYn() {
		return saleYn;
	}
	public void setSaleYn(String saleYn) {
		this.saleYn = saleYn;
	}
	
	@Override
	public String toString() {
		return "NaverOptionVO [mItemCd=" + mItemCd + ", itemCd=" + itemCd + ", optType=" + optType + ", optNm=" + optNm
				+ ", optPrice=" + optPrice + ", saleYn=" + saleYn + "]";
	}
	
}
