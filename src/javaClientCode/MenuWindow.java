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
		VBox root = new VBox();
		Label feed = new Label();
		Button menu1 = new Button("MenuItem1");
		menu1.setOnAction(e->{
			//stage.setScene(questionScene);
			stage.setScene(ctrl.getWindow(1)); //QuestionScene at index 1 in GUIctrl
			
		});
		Button menu2 = new Button("MenuItem2");
		Button menu3 = new Button("MenuItem3");
		
		root.getChildren().addAll(feed, menu1,menu2,menu3);
		Scene scene = new Scene(root, 1300, 700);
		return scene;
		
	}

}
