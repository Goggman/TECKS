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
	FeedUpdater updater4;
	TextArea stats;
	TextArea serverFeed;
	TextArea subjectsGlobal;
	TextArea scoreFeed;
	Stage chat;
	
	ProfileWindow(Stage stageIn, GUIController ctrlIn, ServerClient clientIn, Stage chatIn){
		stage=stageIn;
		ctrl=ctrlIn;
		client=clientIn;
		chat = chatIn;
	}
	
	public Scene createScene(){
		int xBase = 0, yBase = 0;
		Pane root = new Pane();

		subjectsGlobal = new TextArea(); subjectsGlobal.setLayoutX(xBase+300); subjectsGlobal.setLayoutY(yBase+250);subjectsGlobal.setEditable(false);
		subjectsGlobal.setPrefHeight(150);subjectsGlobal.setPrefWidth(200);
		stats = new TextArea(); stats.setLayoutX(xBase+100); stats.setLayoutY(yBase+100);stats.setEditable(false);
		stats.setPrefHeight(150); stats.setPrefWidth(400);
		serverFeed = new TextArea(); serverFeed.setLayoutX(xBase+100); serverFeed.setLayoutY(yBase+400);
		serverFeed.setPrefHeight(150); serverFeed.setPrefWidth(250);serverFeed.setEditable(false);
		scoreFeed = new TextArea(); scoreFeed.setLayoutX(xBase+100); scoreFeed.setLayoutY(yBase+250);

		scoreFeed.setPrefSize(200, 150);
		scoreFeed.setEditable(false);
		
		Button tab1 = new Button("Goto quiz"); tab1.setLayoutX(92); tab1.setLayoutY(2); tab1.setStyle("-fx-background-color: -fx-outer-border, -fx-inner-border, -fx-body-color; -fx-background-insets: 0, 1, 2;-fx-background-radius: 5, 4, 3;");
		tab1.setPrefWidth(100);
		tab1.setOnAction(e->{
			stage.setScene(ctrl.getScene(1)); //QuestionScene at index 1 in GUIctrl
			
		});
		

		Button tab2 = new Button("Goto Qcreator"); tab2.setLayoutX(196); tab2.setLayoutY(2); tab2.setStyle("-fx-background-color: -fx-outer-border, -fx-inner-border, -fx-body-color; -fx-background-insets: 0, 1, 2;-fx-background-radius: 5, 4, 3;");
		tab2.setPrefWidth(100);
		tab2.setOnAction(e->{
			stage.setScene(ctrl.getScene(2));
		});
		
		////
		
		Button tab3 = new Button("Goto login"); tab3.setLayoutX(300); tab3.setLayoutY(2); tab3.setStyle("-fx-background-color: -fx-outer-border, -fx-inner-border, -fx-body-color; -fx-background-insets: 0, 1, 2;-fx-background-radius: 5, 4, 3;");
		tab3.setPrefWidth(100);
		tab3.setOnAction(e->{
			stage.setScene(ctrl.getScene(4));
		});
		

		Button tab4 = new Button("Goto profile"); tab4.setLayoutX(404); tab4.setLayoutY(2); tab4.setStyle("-fx-background-color: -fx-outer-border, -fx-inner-border, -fx-body-color; -fx-background-insets: 0, 1, 2;-fx-background-radius: 5, 4, 3;");
		tab4.setPrefWidth(100);
		tab4.setOnAction(e->{
			stage.setScene(ctrl.getScene(5));
		});

		Button showChat = new Button("ShowChat"); showChat.setLayoutX(502);showChat.setLayoutY(643);
		showChat.setStyle("-fx-pref-width: 95");
		showChat.setOnAction(e->{
			ctrl.chat.wakeUp();
			chat.show();
			
		});
		Button hideChat = new Button("HideChat"); hideChat.setLayoutX(502);hideChat.setLayoutY(673);
		hideChat.setStyle("-fx-pref-width: 95");
		hideChat.setOnAction(e->{
			ctrl.chat.sleep();
			chat.hide();
		});
		
		
		TextField addSubjects = new TextField(); addSubjects.setLayoutX(325); addSubjects.setLayoutY(400);
		addSubjects.setPromptText("addSubject");
		addSubjects.setStyle("-fx-pref-width: 175");
		addSubjects.setOnAction(e->{
			client.sendMessage("request:add_subject\tcontent:"+addSubjects.getText());
		});
		
		TextField removeSubject = new TextField(); removeSubject.setLayoutX(325); removeSubject.setLayoutY(430);
		removeSubject.setPromptText("removeSubject");
		removeSubject.setStyle("-fx-pref-width: 175");
		removeSubject.setOnAction(e->{
			client.sendMessage("request:remove_subject\tcontent:"+removeSubject.getText());
		});
		

		TextField createSubject = new TextField(); createSubject.setLayoutX(325); createSubject.setLayoutY(460);
		createSubject.setPromptText("createSubject");
		createSubject.setStyle("-fx-pref-width: 175");
		createSubject.setOnAction(e->{
			client.sendMessage("request:create_subject\tcontent:"+createSubject.getText());
		});

		TextField setSubject = new TextField(); setSubject.setLayoutX(325); setSubject.setLayoutY(490);
		setSubject.setPromptText("setSubject");
		setSubject.setStyle("-fx-pref-width: 175");
		setSubject.setOnAction(e->{
			client.sendMessage("request:set_subject\tcontent:"+setSubject.getText());
		});
		
		Button resetScore = new Button("resetScore"); resetScore.setLayoutX(325); resetScore.setLayoutY(520);
		resetScore.setOnAction(e->{
			client.sendMessage("request:reset_score\tcontent:local");
		});
		
		Button requestScore = new Button("reqScore"); requestScore.setLayoutX(425); requestScore.setLayoutY(520);
		requestScore.setOnAction(e->{
			client.sendMessage("request:get_subject_scores\tcontent:");
		});
		//TextArea subjects = new TextArea(); subjects.setLayoutX(xBase); subjects.setLayoutY(yBase+100);
		//TextArea score = new TextArea(); score.setLayoutX(xBase); score.setLayoutY(yBase+150);
		//TextArea classScore = new TextArea(); score.setLayoutX(xBase); score.setLayoutY(yBase+200);
		

		root.getChildren().addAll(stats, serverFeed, subjectsGlobal, addSubjects, showChat, hideChat, createSubject, resetScore, removeSubject, setSubject, tab1, tab2, tab3, tab4, scoreFeed, requestScore);
		
		
		updater1 = new FeedUpdater(client, stats, client.ProfileWindowStats);
		updater1.start();
		updater2 = new FeedUpdater(client, serverFeed, client.ProfileWindow);
		updater2.start();
		updater3 = new FeedUpdater(client, subjectsGlobal, client.ProfileWindowSubjects);
		updater3.start();

		

		updater4 = new FeedUpdater(client, scoreFeed, client.ProfileWindowScores);
		updater4.start();
		Scene scene = new Scene(root, 600, 700);
		scene.getStylesheets().add(getClass().getResource("GUI.css").toExternalForm());
		

	return scene;
	}
	
	public void wakeUp(){
		stats.setText("You stats:");	
		serverFeed.setText("Messages from server:");
		subjectsGlobal.setText("Global subjects:");
		scoreFeed.setText("Subject scores here:");
		client.sendMessage("request:get_username\tcontent:");
		client.sendMessage("request:get_type\tcontent:");
		client.sendMessage("request:get_subjects\tcontent:local");
		client.sendMessage("request:get_subjects\tcontent:global");
		client.sendMessage("request:get_stats\tcontent:");
		
		
	}
	public void sleep(){
		
	}
}
