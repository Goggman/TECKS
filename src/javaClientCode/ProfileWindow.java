package javaClientCode;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;



import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
public class ProfileWindow implements Window { 
	Stage stage;
	GUIController ctrl;
	ServerClient client;
	FeedUpdater updater1;
	FeedUpdater updater2;
	FeedUpdater updater3;
	TextArea stats;
	TextArea serverFeed;
	TextArea subjectsGlobal;
	
	ProfileWindow(Stage stageIn, GUIController ctrlIn, ServerClient clientIn){
		stage=stageIn;
		ctrl=ctrlIn;
		client=clientIn;
	}
	//TODO: need  to test this window, check if the data is shown properly, and is readable, resize the textAreas etc
	public Scene createScene(){
		int xBase = 500, yBase = 200;
		Pane root = new Pane();
		subjectsGlobal = new TextArea(); subjectsGlobal.setLayoutX(xBase); subjectsGlobal.setLayoutY(yBase+200);
		subjectsGlobal.setPrefHeight(100);subjectsGlobal.setPrefWidth(200);
		stats = new TextArea("Global subjects"); stats.setLayoutX(xBase); stats.setLayoutY(yBase);
		stats.setPrefHeight(150); stats.setPrefWidth(300);
		serverFeed = new TextArea(); serverFeed.setLayoutX(50); serverFeed.setLayoutY(50);
		//serverFeed.setStyle("-fx-pref-width: 80"); serverFeed.setStyle("-fx-pref-height: 300");
		serverFeed.setPrefHeight(300); serverFeed.setPrefWidth(300);
		
		
		TextField addSubjects = new TextField(); addSubjects.setLayoutX(xBase+500); addSubjects.setLayoutY(yBase);
		addSubjects.setPromptText("addSubject");
		addSubjects.setOnAction(e->{
			client.sendMessage("request:add_subject\tcontent:"+addSubjects.getText());
		});
		TextField removeSubject = new TextField(); removeSubject.setLayoutX(xBase+500); removeSubject.setLayoutY(yBase+50);
		removeSubject.setPromptText("removeSubject");
		removeSubject.setOnAction(e->{
			client.sendMessage("request:remove_subject\tcontent:"+removeSubject.getText());
		});
		Button resetScore = new Button("resetScore"); resetScore.setLayoutX(xBase+500); resetScore.setLayoutY(yBase+100);
		resetScore.setStyle("-fx-pref-width: 100");
		resetScore.setOnAction(e->{
			client.sendMessage("request:reset_score\tcontent:local"+resetScore.getText());
		});
		TextField createSubject = new TextField(); createSubject.setLayoutX(xBase+500); createSubject.setLayoutY(yBase+150);
		createSubject.setPromptText("createSubject");
		createSubject.setOnAction(e->{
			client.sendMessage("request:create_subject\tcontent:"+createSubject.getText());
		});
		
		Button backToMenu = new Button("Menu"); backToMenu.setLayoutX(xBase+500); backToMenu.setLayoutY(yBase+200);
		backToMenu.setStyle("-fx-pref-width: 100");
		backToMenu.setOnAction(e->{
			stage.setScene(ctrl.getScene(0));
		});
		
		TextField setSubject = new TextField(); setSubject.setLayoutX(xBase+500); setSubject.setLayoutY(yBase+250);
		setSubject.setPromptText("setSubject");
		setSubject.setOnAction(e->{
			client.sendMessage("request:set_subject\tcontent:"+setSubject.getText());
		});
		
		//TextArea subjects = new TextArea(); subjects.setLayoutX(xBase); subjects.setLayoutY(yBase+100);
		//TextArea score = new TextArea(); score.setLayoutX(xBase); score.setLayoutY(yBase+150);
		//TextArea classScore = new TextArea(); score.setLayoutX(xBase); score.setLayoutY(yBase+200);
		
		root.getChildren().addAll(stats, serverFeed, subjectsGlobal, addSubjects, backToMenu, createSubject, resetScore, removeSubject, setSubject);

		
		updater1 = new FeedUpdater(client, stats, client.ProfileWindowStats);
		updater1.start();
		updater2 = new FeedUpdater(client, serverFeed, client.ProfileWindow);
		updater2.start();
		updater3 = new FeedUpdater(client, subjectsGlobal, client.ProfileWindowSubjects);
		updater3.start();
		Scene scene = new Scene(root, 1300, 700);
		scene.getStylesheets().add(getClass().getResource("GUI.css").toExternalForm());
	return scene;
	}
	
	public void wakeUp(){
		stats.setText("You stats:");
		serverFeed.setText("Messages from server:");
		subjectsGlobal.setText("Global subjects:");
		client.sendMessage("request:get_username\tcontent:");
		client.sendMessage("request:get_type\tcontent:");
		client.sendMessage("request:get_subjects\tcontent:local");
		client.sendMessage("request:get_subjects\tcontent:global");
		client.sendMessage("request:get_stats\tcontent:");
		
		
	}
	public void sleep(){
		
	}
}
