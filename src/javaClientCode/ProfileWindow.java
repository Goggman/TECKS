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
public class ProfileWindow implements Window { //TODO: Make a profile window, or integrate elements of this window into others
	Stage stage;
	GUIController ctrl;
	ServerClient client;
	FeedUpdater updater;
	ProfileWindow(Stage stageIn, GUIController ctrlIn, ServerClient clientIn){
		stage=stageIn;
		ctrl=ctrlIn;
		client=clientIn;
	}
	public Scene createScene(){
		int xBase = 0, yBase = 0;
		Pane root = new Pane();
		Label username = new Label(); username.setLayoutX(xBase); username.setLayoutY(yBase+50);
		//Label subjects = new Label(); subjects.setLayoutX(xBase); subjects.setLayoutY(yBase+100);
		//Label score = new Label(); score.setLayoutX(xBase); score.setLayoutY(yBase+150);
		//Label classScore = new Label(); score.setLayoutX(xBase); score.setLayoutY(yBase+200);
		
		root.getChildren().addAll(username);
		
		
		updater = new FeedUpdater(client, username, client.ProfileWindow);
		updater.start();
		
		Scene scene = new Scene(root, 1300, 700);
		
	return scene;
	}
	
	public void wakeUp(){
		
	}
	public void sleep(){
		
	}
}
