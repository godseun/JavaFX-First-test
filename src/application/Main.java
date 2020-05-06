package application;
	
import java.io.IOException;
import java.util.Map;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import application.controller.market.TransferController;
import application.model.commons.ConnectServerInterface;
import application.model.commons.MySqlQuery;
import application.model.vo.DeliveryCodeVO;
import application.model.vo.LoginVO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;


public class Main extends Application {
	
	public static WebDriver driver;
	public static JavascriptExecutor js;
	
	public static String defaultWindow;
	
	public static String DB;
	
	public static Map<String, LoginVO> loginMap;
	public static Map<String, DeliveryCodeVO> delCodeMap;
	
	private Stage primaryStage;
	private AnchorPane layout;

	static {
		// chromedriver 경로설정 밑 DB 가져오기
		String dir = System.getProperty("user.dir").concat("\\jre\\chromedriver.exe");
		System.setProperty("webdriver.chrome.driver", dir);
		
		setDefualtInfo();
	}
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Autometic Open Market Controller ver.BETA");;
		setLayout();
	}
	
	public void setLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/View.fxml"));
			layout = (AnchorPane) loader.load();
			Scene scene = new Scene(layout);
			primaryStage.setScene(scene);
			primaryStage.show();
			
			TransferController tranController = loader.getController();
			tranController.setMainApp(this);
			
			primaryStage.setOnCloseRequest(event -> {
				closeDriver();
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static public void openDriver() {
		if(driver == null) {
			ChromeOptions options = new ChromeOptions();
//			options.addArguments("headless");
			driver = new ChromeDriver(options);
			js = (JavascriptExecutor) driver;
			driver.manage().window().setSize(new Dimension(1936, 1056));
			defaultWindow = driver.getWindowHandle();
		}
	}
	
	static public void closeDriver() {
		if(driver != null) {
			for(String windowName : driver.getWindowHandles()) {
				driver.switchTo().window(windowName);
				driver.close();
			}
			driver.quit();
		}
	}
	
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	
	static public void connectDB() {
		DB = ConnectServerInterface.serachDB();
		if (DB.equals("") || DB == null) {
			// TODO DB연결실패 로직만들자
		}
	}
	
	static public void setDefualtInfo() {
		connectDB();
		// TODO DB연결실패 로직 만들어야함
		openDriver();
		
		loginMap = MySqlQuery.SelectLoginInfoMap();
		delCodeMap = MySqlQuery.SelectDelcompcodeInfoArrayMap();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
