package javaClientCode;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;



import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
public class ProfileWindow implements Window { //TODO: Make a statistics window, or integrate elements of this window into others
	Stage stage;
	GUIController ctrl;
	ServerClient client;
	ProfileWindow(Stage stageIn, GUIController ctrlIn, ServerClient clientIn){
		stage=stageIn;
		ctrl=ctrlIn;
		client=clientIn;
	}
	public Scene createScene(){
		Pane root = new Pane();
		Label username = new Label();
		Label subjects = new Label();
		Label score = new Label();
		
		
		root.getChildren().addAll(username, subjects, score);
		Scene scene = new Scene(root, 1300, 700);
		
	return scene;
	}
	
	public void wakeUp(){
		
	}
	public void sleep(){
		
	}
}
