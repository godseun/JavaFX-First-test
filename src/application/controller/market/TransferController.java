package application.controller.market;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class TransferController implements Initializable{

	private Main main;
	
	@FXML
	public Button marketUpBtn;
	@FXML
	public Button marketDownBtn;
	
	@FXML
	public TextArea textArea;
	
	@FXML
	public ListView<String> availableMarketListView;
	
	@FXML
	public ListView<String> usingMarketListView;
	
	ArrayList<String> marketNames() {
		ArrayList<String> getMarketNames = new ArrayList<String>();
		for(String s : Main.loginMap.keySet()) {
			getMarketNames.add(s);
		}
		return getMarketNames;
	}
	
	ObservableList<String> availableMarketList = FXCollections.observableArrayList(marketNames());
	ObservableList<String> usingMarketList = FXCollections.observableArrayList();
	
	Object obj;
	
	@FXML
	private void StartBtnClick() {
		System.out.println("start btn");
	}
	@FXML
	private void marketUpBtn() {
		usingMarketList.remove(obj);
		if(!availableMarketList.contains(obj)) {
			availableMarketList.add(obj.toString());
		}
		usingMarketListView.getSelectionModel().clearSelection();
	}
	@FXML
	private void marketDownBtn() {
		availableMarketList.remove(obj);
		if(!usingMarketList.contains(obj)) {
			usingMarketList.add(obj.toString());
		}
		availableMarketListView.getSelectionModel().clearSelection();
	}
	
	
	public void comboChange(ActionEvent event) {
	}
	
	public void setMainApp(Main main) {
		this.main = main;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		availableMarketListView.setItems(availableMarketList);
		usingMarketListView.setItems(usingMarketList);
		
		availableMarketListView.setOnMouseClicked(event ->{
			obj = availableMarketListView.getSelectionModel().getSelectedItem();
			usingMarketListView.getSelectionModel().clearSelection();
		});
		
		
		usingMarketListView.setOnMouseClicked(event ->{
			obj = usingMarketListView.getSelectionModel().getSelectedItem();
			availableMarketListView.getSelectionModel().clearSelection();
		});
	}
}
