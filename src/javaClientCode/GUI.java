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
		QuestionWindow QB = new QuestionWindow(new QuestionSchema());
		stage.setScene(QB.createScene());
		stage.show();
	}
	
	
	public static void main(String[] args){
		launch();
		
	}
	

}
