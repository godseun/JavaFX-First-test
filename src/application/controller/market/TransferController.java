package application.controller.market;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;

import application.Main;
import application.model.Commons;
import application.model.commons.MySqlQuery;
import application.model.vo.ProductVO;
import javafx.application.Platform;
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
	private Thread setTextThread;
	
	private Main main;
	public void setMainApp(Main main) {
		this.main = main;
	}
	@FXML
	public Button marketUpBtn,marketDownBtn,startBtn,pauseBtn,stopBtn;
	@FXML
	public TextArea logTextArea;
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
	
	ObservableList<String> usingMarketList = FXCollections.observableArrayList(marketNameList());
	ObservableList<String> notUsingMarketList = FXCollections.observableArrayList();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		if (Main.DB.equals("") || Main.DB == null) {
			setTextAreaWrite("\n [DB 서치실패] \n");
			checkBoxDisabled();
			startBtn.setDisable(true);
			pauseBtn.setDisable(true);
			stopBtn.setDisable(true);
		}
		
		useingMarketListView.setItems(usingMarketList);
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
	
	@FXML
	private void StartBtnClick() {
		if(thread!=null) {
			setTextAreaWrite("종료중입니다. 잠시만 기다려주세요.");
			return;
		}
		startBtn.setDisable(true);
		Main.startBtnStat = true;
		setTextAreaWrite("thread 시작");
		checkBoxDisabled();
		disableAfterComparingMarketAndDefaultDelCodeInUse();
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
		if(thread==null) return;
		startBtn.setDisable(false);
		Main.startBtnStat = false;
		setTextAreaWrite("종료중 .. 작업이 끝나면 종료됩니다. 기다려주세요");
		checkBoxEnabled();
	}
	@FXML
	private void marketUpBtn() {
		notUsingMarketList.remove(listViewSelectTarget);
		if(!usingMarketList.contains(listViewSelectTarget)) {
			usingMarketList.add(listViewSelectTarget.toString());
		}
		notUsingMarketListView.getSelectionModel().clearSelection();
	}
	@FXML
	private void marketDownBtn() {
		usingMarketList.remove(listViewSelectTarget);
		if(!notUsingMarketList.contains(listViewSelectTarget)) {
			notUsingMarketList.add(listViewSelectTarget.toString());
		}
		useingMarketListView.getSelectionModel().clearSelection();
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
	// TODO 일시정지,종료 시 어떻게할건지 미정
	private void startLogic() {
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					if(!startBtn.isDisable()) break;
					marketAndFunctionTransfer();
				}
				Commons.windowInit();
				stopLogic();
			}
		});
		thread.setDaemon(true);
		thread.start();
		
		setTextThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(!Main.setTextArea.equals("")) {
						Platform.runLater(()->{
							setTextAreaWrite(Main.setTextArea);
							Main.setTextArea = "";
						});
					}
				}
			}
		});
		setTextThread.setDaemon(true);
		setTextThread.start();
	}
	
	private void stopLogic() {
		thread.interrupt();
		thread = null;
		setTextThread.interrupt();
		setTextThread = null;
		setTextAreaWrite("thread 종료");
	}
	
	private void marketAndFunctionTransfer() {
		for(String marketName : usingMarketList) {
			if(prodInsert.isSelected()) {
				prodRegForMarket(marketName);
			} else if(prodUpdate.isSelected()) {
				prodUpdateForMarket(marketName);
			} else if(prodStopSale.isSelected()) {
				prodStopSaleForMarket(marketName);
			} else if(prodStartSale.isSelected()) {
				prodStartSaleForMarket(marketName);
			}
		}
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
				if(prodList != null && !prodList.isEmpty()) {
					NaverController naverController = new NaverController();
					naverController.individualProductRegistration(prodList, "naver");
				}
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
	
	private void disableAfterComparingMarketAndDefaultDelCodeInUse() {
		if(!Main.delCodeMap.isEmpty()) {
			ArrayList<String> list = new ArrayList<String>();
			String marketList = "";
			for(String marketName : usingMarketList) {
				if(Main.delCodeMap.get(marketName) == null) {
					list.add(marketName);
				}
			}
			if(list.size()>0) {
				for(String marketName : list) {
					usingMarketList.remove(marketName);
					notUsingMarketList.add(marketName);
					marketList = marketList.concat(","+marketName);
				}
				setTextAreaWrite("["+marketList.substring(1, marketList.length())+"] 기본배송지설정이 안되어있어 사용불가합니다.");
			}
		}
	}
	
	public void setTextAreaWrite(String text) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateForm = new SimpleDateFormat("MM-dd HH:mm:ss");
		
		logTextArea.appendText(dateForm.format(calendar.getTime())+" > "+text+"\n");
	}
}
