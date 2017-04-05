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
		int xBase = 100, yBase = 100;
		Pane root = new Pane();
		subjectsGlobal = new TextArea(); subjectsGlobal.setLayoutX(xBase); subjectsGlobal.setLayoutY(yBase+50);
		stats = new TextArea(); stats.setLayoutX(xBase); stats.setLayoutY(yBase+100);
		serverFeed = new TextArea(); serverFeed.setLayoutX(xBase); serverFeed.setLayoutY(yBase+150);
		
		TextField addSubjects = new TextField(); addSubjects.setLayoutX(xBase+50); addSubjects.setLayoutY(yBase+200);
		addSubjects.setPromptText("type in the subject you want to add");
		addSubjects.setOnAction(e->{
			client.sendMessage("request:add_subject\tcontent:"+addSubjects.getText());
		});
		TextField removeSubject = new TextField(); removeSubject.setLayoutX(xBase+50); removeSubject.setLayoutY(yBase+250);
		removeSubject.setPromptText("type here to remove subject");
		removeSubject.setOnAction(e->{
			client.sendMessage("request:remove_subject\tcontent:"+removeSubject.getText());
		});
		Button resetScore = new Button("resetScore"); resetScore.setLayoutX(xBase+50); resetScore.setLayoutY(yBase+300);
		resetScore.setOnAction(e->{
			client.sendMessage("request:reset_score\tcontent:"+resetScore.getText());
		});
		TextField createSubject = new TextField(); createSubject.setLayoutX(xBase+50); createSubject.setLayoutY(yBase+350);
		createSubject.setPromptText("type here to create subject");
		createSubject.setOnAction(e->{
			client.sendMessage("request:create_subject\tcontent:"+createSubject.getText());
		});
		
		Button backToMenu = new Button("Goto Menu"); resetScore.setLayoutX(xBase+50); resetScore.setLayoutY(yBase+400);
		backToMenu.setOnAction(e->{
			stage.setScene(ctrl.getScene(0));
		});
		
		//TextArea subjects = new TextArea(); subjects.setLayoutX(xBase); subjects.setLayoutY(yBase+100);
		//TextArea score = new TextArea(); score.setLayoutX(xBase); score.setLayoutY(yBase+150);
		//TextArea classScore = new TextArea(); score.setLayoutX(xBase); score.setLayoutY(yBase+200);
		
		root.getChildren().addAll(stats, serverFeed, subjectsGlobal, addSubjects);
		
		
		updater1 = new FeedUpdater(client, stats, client.ProfileWindowStats);
		updater1.start();
		updater2 = new FeedUpdater(client, serverFeed, client.ProfileWindow);
		updater2.start();
		updater3 = new FeedUpdater(client, subjectsGlobal, client.ProfileWindowSubjects);
		updater3.start();
		Scene scene = new Scene(root, 1300, 700);
		
	return scene;
	}
	
	public void wakeUp(){
		stats.setText("");
		serverFeed.setText("");
		client.sendMessage("request:get_username\tcontent:");
		client.sendMessage("request:get_subjects\tcontent:local");
		client.sendMessage("request:get_stats\tcontent:");
		
		
	}
	public void sleep(){
		
	}
}
