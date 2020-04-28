package application.controller.market;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class TransferController implements Initializable{

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
		marketAndFunctionTransfer();
	}
	@FXML
	private void PauseBtnClick() {
		System.out.println("PauseBtnClick");
	}
	@FXML
	private void StopBtnClick() {
		startBtn.setDisable(false);
		checkBoxEnabled();
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
	
	private void checkBoxChange(CheckBox target, boolean stat) {
		target.setDisable(stat);
	}
	
	private void marketAndFunctionTransfer() {
		for(String marketName : useingMarketList) {
			if(prodInsert.isSelected()) ;
			else if(prodUpdate.isSelected()) ;
			else if(prodStopSale.isSelected()) ;
			else if(prodStartSale.isSelected()) ;
		}
	}
}
