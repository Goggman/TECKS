package javaClientCode;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.geometry.Pos;
import javafx.scene.control.Button;

public class GUI extends Application{
	
	
	public void start(Stage stage) throws Exception{

		GUIController ctrl = new GUIController(stage);
		stage.setScene(ctrl.getScene(4));
		stage.setTitle("TECKS");
		stage.sizeToScene();

		stage.show();
	}
	
	
	public static void main(String[] args){
		launch();
		
	}
	
	

	

}
