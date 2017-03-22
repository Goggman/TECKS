package javaClientCode;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.geometry.Pos;
import javafx.scene.control.Button;

public class DummyWindow implements Window {
	Stage stage;
	GUIController ctrl;
	DummyWindow(Stage stageInput, GUIController CtrlIn){
		ctrl=CtrlIn;
		stage=stageInput;
		
	}
	public Scene createScene(){
		Pane root = new Pane();
		return new Scene(root, 1300, 700);
	}
}
