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
		Stage stage1= new Stage();
		chat chat = new chat();
		
		GUIController ctrl = new GUIController(stage);
		stage.setScene(ctrl.getScene(4));
		stage.setTitle("TECKS");
		stage.show();
		chat.start(stage1);
	}
	
	
	public static void main(String[] args){
		launch();
		
	}
	
	
	public class chat extends Application{
		public void start(Stage stage) throws Exception{
			Pane root = new Pane();
			Scene scene = new Scene(root, 200, 200);
			stage.setScene(scene);
			stage.setTitle("Chat");
			stage.show();
		}
	}
	

}
