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
		int xBase=200, yBase = 0;
		Pane root = new Pane(); root.setStyle("-fx-background-color: white");
		Label feed = new Label();
		
		Button tab1 = new Button("Goto quiz"); tab1.setLayoutX(100); tab1.setLayoutY(0);
		tab1.setStyle("-fx-pref-width: 100");
		tab1.setOnAction(e->{
			stage.setScene(ctrl.getScene(1)); //QuestionScene at index 1 in GUIctrl
			
		});
		
		Button tab2 = new Button("Goto Qcreator"); tab2.setLayoutX(200); tab2.setLayoutY(0);
		tab2.setStyle("-fx-pref-width: 100");
		tab2.setOnAction(e->{
			stage.setScene(ctrl.getScene(2));
		});
		
		////
		
		Button tab3 = new Button("Goto login"); tab3.setLayoutX(300); tab3.setLayoutY(0);
		tab3.setStyle("-fx-pref-width: 100");
		tab3.setOnAction(e->{
			stage.setScene(ctrl.getScene(4));
		});
		
		Button tab4 = new Button("Goto profile"); tab4.setLayoutX(400); tab4.setLayoutY(0);
		tab4.setStyle("-fx-pref-widt: 100");
		tab4.setOnAction(e->{

			stage.setScene(ctrl.getScene(5));
		});
		
		root.getChildren().addAll(feed, tab1, tab2, tab3, tab4);
		Scene scene = new Scene(root, 1300, 700);
		scene.getStylesheets().add(getClass().getResource("GUI.css").toExternalForm());
		
		return scene;
		
	}
	
	public void wakeUp(){
		
	}
	public void sleep(){
		
	}
}
