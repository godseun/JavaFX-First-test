package application.controller.market;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import application.Main;
import application.model.Commons;
import application.model.commons.MySqlQuery;
import application.model.vo.ProductVO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class TransferController implements Initializable{

	private Thread thread;
	
	private Main main;
	public void setMainApp(Main main) {
		this.main = main;
	}
	@FXML
	public Button marketUpBtn,marketDownBtn,startBtn,pauseBtn,stopBtn;
	@FXML
	public TextArea textArea;
	@FXML
	public ListView<String> useingMarketListView,notUsingMarketListView;
	@FXML
	public CheckBox prodInsert,prodUpdate,prodStopSale,prodStartSale;
	
	public Object listViewSelectTarget;
	
	ArrayList<String> marketNameList() {
		ArrayList<String> marketNameList = new ArrayList<String>();
		for(String marketName : Main.loginMap.keySet()) {
			marketNameList.add(marketName);
		}
		return marketNameList;
	}
	
	ObservableList<String> useingMarketList = FXCollections.observableArrayList(marketNameList());
	ObservableList<String> notUsingMarketList = FXCollections.observableArrayList();
	
	
	@FXML
	private void StartBtnClick() {
		startBtn.setDisable(true);
		checkBoxDisabled();
		if(thread!=null) return;
		startLogic();
	}
	@FXML
	private void PauseBtnClick() throws InterruptedException {
		String pauseBtnStat = pauseBtn.getText();
		if(thread==null) return;
		if(pauseBtnStat.equals("일시중지")) {
			pauseBtn.setText("재시작");
		} else {
			pauseBtn.setText("일시중지");
		}
	}
	
	@FXML
	private void StopBtnClick() {
		startBtn.setDisable(false);
		checkBoxEnabled();
		if(thread==null) return;
		stopLogic();
	}
	@FXML
	private void marketUpBtn() {
		notUsingMarketList.remove(listViewSelectTarget);
		if(!useingMarketList.contains(listViewSelectTarget)) {
			useingMarketList.add(listViewSelectTarget.toString());
		}
		notUsingMarketListView.getSelectionModel().clearSelection();
	}
	@FXML
	private void marketDownBtn() {
		useingMarketList.remove(listViewSelectTarget);
		if(!notUsingMarketList.contains(listViewSelectTarget)) {
			notUsingMarketList.add(listViewSelectTarget.toString());
		}
		useingMarketListView.getSelectionModel().clearSelection();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		if (Main.DB.equals("") || Main.DB == null) {
			textArea.setText("DB 서치실패 \n");
		}
		
		useingMarketListView.setItems(useingMarketList);
		notUsingMarketListView.setItems(notUsingMarketList);
		
		useingMarketListView.setOnMouseClicked(event ->{
			listViewSelectTarget = useingMarketListView.getSelectionModel().getSelectedItem();
			notUsingMarketListView.getSelectionModel().clearSelection();
		});
		
		notUsingMarketListView.setOnMouseClicked(event ->{
			listViewSelectTarget = notUsingMarketListView.getSelectionModel().getSelectedItem();
			useingMarketListView.getSelectionModel().clearSelection();
		});
		
	}
	
	private void checkBoxDisabled() { checkBoxChange(true); }
	private void checkBoxEnabled() { checkBoxChange(false); }
	
	private void checkBoxChange(boolean able) {
		prodInsert.setDisable(able);
		prodUpdate.setDisable(able);
		prodStopSale.setDisable(able);
		prodStartSale.setDisable(able);
		useingMarketListView.setDisable(able);
		notUsingMarketListView.setDisable(able);
		listViewSelectTarget = "";
		marketUpBtn.setDisable(able);
		marketDownBtn.setDisable(able);
	}
	
	private void startLogic() {
		Main.openDriver();
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					if(!startBtn.isDisable()) break;
					marketAndFunctionTransfer();
				}
			}
		});

		thread.setDaemon(true);
		thread.start();
	}
	
	private void stopLogic() {
		thread.interrupt();
		thread = null;
		Main.closeDriver();
	}
	
	private void marketAndFunctionTransfer() {
		for(String marketName : useingMarketList) {
			if(prodInsert.isSelected()) prodRegForMarket(marketName);
			else if(prodUpdate.isSelected()) prodUpdateForMarket(marketName);
			else if(prodStopSale.isSelected()) prodStopSaleForMarket(marketName);
			else if(prodStartSale.isSelected()) prodStartSaleForMarket(marketName);
		}
		// 한바퀴 돌고 초기상태로 탭 전부 닫기.
		// TODO 탭별로 자바스크립트 메모리 쌓이는거 해결되는지 체크해야함
		Commons.windowInit();
	}
	// TODO CRUD 로직 구현중
	private void prodStartSaleForMarket(String marketName) {
		switch (marketName) {
		case "네이버": break;
		case "쿠팡": break;
		case "티몬": break;
		case "위메프": break;
		case "11번가": break;
		case "인터파크": break;
		case "지마켓": break;
			
		default : System.out.println("상품재판매 : 확인필요 "+marketName); break;
		}
	}
	private void prodStopSaleForMarket(String marketName) {
		switch (marketName) {
		case "네이버": break;
		case "쿠팡": break;
		case "티몬": break;
		case "위메프": break;
		case "11번가": break;
		case "인터파크": break;
		case "지마켓": break;
			
		default : System.out.println("상품판매중지 : 확인필요 "+marketName); break;
		}
	}
	private void prodUpdateForMarket(String marketName) {
		switch (marketName) {
		case "네이버": break;
		case "쿠팡": break;
		case "티몬": break;
		case "위메프": break;
		case "11번가": break;
		case "인터파크": break;
		case "지마켓": break;
			
		default : System.out.println("상품수정 : 확인필요 "+marketName); break;
		}
	}
	private void prodRegForMarket(String marketName) {
		String status = "등록중";
		ArrayList<ProductVO> prodList = MySqlQuery.SelectProdInfoArrayMap(marketName, status);
		switch (marketName) {
		case "네이버": 
			if(prodList != null && !prodList.isEmpty())
				NaverController.individualProductRegistration(prodList, "naver"); 
			break;
		case "쿠팡": break;
		case "티몬": break;
		case "위메프": break;
		case "11번가": break;
		case "인터파크": break;
		case "지마켓": break;
			
		default : System.out.println("상품등록 : 확인필요 "+marketName); break;
		}
	}
}
