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
	
	public Scene createScene(){
		int xBase=600; int yBase = 200;
		Pane root = new Pane();
		Label feed = new Label();
		Button menu1 = new Button("Goto quiz"); menu1.setLayoutX(xBase+0); menu1.setLayoutY(yBase+0);
		menu1.setOnAction(e->{
			stage.setScene(ctrl.getScene(1)); //QuestionScene at index 1 in GUIctrl
			
		});
		Button menu2 = new Button("Goto Qcreator"); menu2.setLayoutX(xBase+0); menu2.setLayoutY(yBase+50);
		menu2.setOnAction(e->{
			stage.setScene(ctrl.getScene(2));
		});
		Button menu3 = new Button("Goto Qloader"); menu3.setLayoutX(xBase+0); menu3.setLayoutY(yBase+100);
		menu3.setOnAction(e->{
			stage.setScene(ctrl.getScene(3));
		});
		
		root.getChildren().addAll(feed, menu1, menu2, menu3);
		Scene scene = new Scene(root, 1300, 700);
		return scene;
		
	}

}
