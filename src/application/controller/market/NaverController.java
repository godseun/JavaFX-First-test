package application.controller.market;

import application.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class NaverController {

	@FXML
	private Button check;
	
	private Main main;
	
	public NaverController() {
		
	}
	
	@FXML
	private void initialize() {
		
	}
	
	@FXML
	private void StartBtnClick() {
		System.out.println("start btn");
	}
	
	public void setMainApp(Main main) {
		this.main = main;
	}
}
