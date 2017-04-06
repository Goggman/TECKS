package javaClientCode;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;

public class MenuWindow implements Window{
	Stage stage;
	Scene questionScene;
	GUIController ctrl;
	MenuWindow(Stage stageInput, GUIController CtrlIn){
		ctrl=CtrlIn;
		stage=stageInput;
		
	}
	//TODO: menu should be tabs available in every window in main stage, implement this, making MenuWindow obsolete hopefully
	/**
	 * create scene for the main menu
	 * @return
	 */
	public Scene createScene(){
		int xBase=600; int yBase = 200;
		Pane root = new Pane(); root.setStyle("-fx-background-color: white");
		Label feed = new Label();
		Button menu1 = new Button("Goto quiz"); menu1.setLayoutX(xBase+0); menu1.setLayoutY(yBase+0);
		menu1.setStyle("-fx-pref-width: 100");
		menu1.setOnAction(e->{
			stage.setScene(ctrl.getScene(1)); //QuestionScene at index 1 in GUIctrl
			
		});
		Button menu2 = new Button("Goto Qcreator"); menu2.setLayoutX(xBase+0); menu2.setLayoutY(yBase+30);
		menu2.setStyle("-fx-pref-width: 100");
		menu2.setOnAction(e->{
			stage.setScene(ctrl.getScene(2));
		});
		Button menu3 = new Button("Goto Qloader"); menu3.setLayoutX(xBase+0); menu3.setLayoutY(yBase+60);
		menu3.setStyle("-fx-pref-width: 100");
		menu3.setOnAction(e->{
			stage.setScene(ctrl.getScene(3));
		});
		menu3.setDisable(true);
		Button menu4 = new Button("Goto login"); menu4.setLayoutX(xBase+0); menu4.setLayoutY(yBase+90);
		menu4.setStyle("-fx-pref-width: 100");
		menu4.setOnAction(e->{
			stage.setScene(ctrl.getScene(4));
		});
		Button menu5 = new Button("Goto profile"); menu5.setLayoutX(xBase+0); menu5.setLayoutY(yBase+120);
		menu5.setStyle("-fx-pref-widt: 100");
		menu5.setOnAction(e->{
			stage.setScene(ctrl.getScene(5));
		});
		root.getChildren().addAll(feed, menu1, menu2, menu3, menu4, menu5);
		Scene scene = new Scene(root, 1300, 700);
		scene.getStylesheets().add(getClass().getResource("GUI.css").toExternalForm());
		return scene;
		
	}
	
	public void wakeUp(){
		
	}
	public void sleep(){
		
	}
}
