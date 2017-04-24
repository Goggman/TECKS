package javaClientCode;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;



import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
	FeedUpdater updater5;
	TextArea stats;
	TextArea serverFeed;
	TextArea subjectsGlobal;
	TextArea subjectsLocal;
	TextArea scoreFeed;
	MenuButton removeSubject;
	MenuButton setSubject;
	MenuButton addSubject;
	Stage chat;
	boolean setSubjectNeedsUpdate=true;
	boolean addSubjectNeedsUpdate=true;
	
	ProfileWindow(Stage stageIn, GUIController ctrlIn, ServerClient clientIn, Stage chatIn){
		stage=stageIn;
		ctrl=ctrlIn;
		client=clientIn;
		chat = chatIn;
	}
	
	public Scene createScene(){
		int xBase = 0, yBase = 0;
		Pane root = new Pane();

		Label title = new Label("Your Profile");title.setLayoutX(200);title.setLayoutY(50);title.setStyle("-fx-font-size: 30px");
		subjectsLocal = new TextArea();
		subjectsGlobal = new TextArea(); subjectsGlobal.setLayoutX(xBase+300); subjectsGlobal.setLayoutY(yBase+250);//subjectsGlobal.setEditable(false);
		subjectsGlobal.setPrefHeight(150);subjectsGlobal.setPrefWidth(200);subjectsGlobal.setVisible(false);
		subjectsGlobal.setWrapText(true);


		
		subjectsGlobal = new TextArea(); subjectsGlobal.setLayoutX(xBase+300); subjectsGlobal.setLayoutY(yBase+250); //subjectsGlobal.setEditable(false);
		subjectsGlobal.setPrefHeight(150);subjectsGlobal.setPrefWidth(200); 

		stats = new TextArea(); stats.setLayoutX(xBase+100); stats.setLayoutY(yBase+100);stats.setEditable(false);

		stats.setPrefHeight(150); stats.setPrefWidth(400);
		stats.setEditable(false);
		
		serverFeed = new TextArea(); serverFeed.setLayoutX(xBase+100); serverFeed.setLayoutY(yBase+400);

		serverFeed.setPrefHeight(150); serverFeed.setPrefWidth(250);
		serverFeed.setEditable(false);
		serverFeed.setWrapText(true);


		scoreFeed = new TextArea(); scoreFeed.setLayoutX(xBase+100); scoreFeed.setLayoutY(yBase+250);
		scoreFeed.setPrefSize(200, 150);

		scoreFeed.setEditable(false); scoreFeed.setVisible(false);
		removeSubject = new MenuButton("RemoveSubject");  removeSubject.setLayoutX(350); removeSubject.setLayoutY(430);
		setSubject = new MenuButton("setSubject"); setSubject.setLayoutX(xBase+350); setSubject.setLayoutY(xBase+490);
		setSubject.setOnMouseEntered(e->{
			//System.out.println("The needsUpdate: "+setSubjectNeedsUpdate);
			if (subjectsLocal.getText().trim().split("[;]").length == 1){
				return;
			}
			if (subjectsLocal.getText().trim().split("[;]")[1].isEmpty()){
				return;
			}
			if (setSubjectNeedsUpdate){
				setSubject.getItems().clear();
				removeSubject.getItems().clear();
				String[] subjectArray = subjectsLocal.getText().trim().split("[;]")[1].trim().split("[@]");
				for(String subject : subjectArray){
					MenuItem item = new MenuItem(subject);
					item.setOnAction(x->{
						client.sendMessage("request:set_subject\tcontent:"+new String(subject));
					});
					setSubject.getItems().add(item);
					
					MenuItem removeSubjectItem = new MenuItem(subject);
					removeSubjectItem.setOnAction(c->{
						client.sendMessage("request:remove_subject\tcontent:"+new String(subject));
						setSubjectNeedsUpdate=true;
						wakeUp();
						});
					removeSubject.getItems().add(removeSubjectItem);
					
				}
				setSubjectNeedsUpdate=false;
			}
			
		});
		removeSubject.setOnMouseEntered(setSubject.getOnMouseEntered());
		
		addSubject = new MenuButton("addSubject");  addSubject.setLayoutX(xBase+350); addSubject.setLayoutY(yBase+400);
		addSubject.setOnMouseEntered(x->{
			if (subjectsGlobal.getText().trim().isEmpty()){
				return;
			}
			if (addSubjectNeedsUpdate){
				addSubject.getItems().clear();
				String[] subjectArray = subjectsGlobal.getText().trim().split("[@]");
				for(String subject : subjectArray){
					String itemsubject = subject;
					MenuItem item = new MenuItem(subject);
					item.setOnAction(z->{
						client.sendMessage("request:add_subject\tcontent:"+itemsubject);
						wakeUp();
					});
					addSubject.getItems().add(item);
				}
				addSubjectNeedsUpdate=false;
			}
			
		});
		
		
		Button tab1 = new Button("Quiz"); tab1.setLayoutX(92); tab1.setLayoutY(2); 
		tab1.setStyle("-fx-background-color: -fx-outer-border, -fx-inner-border, -fx-body-color; -fx-background-insets: 0, 1, 2;-fx-background-radius: 5, 4, 3;");
		tab1.setPrefWidth(100);

		tab1.setOnAction(e->{
			stage.setScene(ctrl.getScene(1)); //QuestionScene at index 1 in GUIctrl
			
		});
		


		Button tab2 = new Button("Qcreator"); tab2.setLayoutX(196); tab2.setLayoutY(2); 
		tab2.setStyle("-fx-background-color: -fx-outer-border, -fx-inner-border, -fx-body-color; -fx-background-insets: 0, 1, 2;-fx-background-radius: 5, 4, 3;");

		tab2.setPrefWidth(100);
		tab2.setOnAction(e->{
			stage.setScene(ctrl.getScene(2));
		});
		

		

		Button tab3 = new Button("Login"); tab3.setLayoutX(300); tab3.setLayoutY(2); 
		tab3.setStyle("-fx-background-color: -fx-outer-border, -fx-inner-border, -fx-body-color; -fx-background-insets: 0, 1, 2;-fx-background-radius: 5, 4, 3;");

		tab3.setPrefWidth(100);
		tab3.setOnAction(e->{
			stage.setScene(ctrl.getScene(4));
			//TODO: log user out
		});

		Button tab4 = new Button("Profile"); tab4.setLayoutX(404); tab4.setLayoutY(2); 
		tab4.setStyle("-fx-background-color: -fx-outer-border, -fx-inner-border, -fx-body-color; -fx-background-insets: 0, 1, 2;-fx-background-radius: 5, 4, 3;");

		tab4.setPrefWidth(100);
		tab4.setOnAction(e->{
			stage.setScene(ctrl.getScene(5));
		});

		Button showChat = new Button("ShowChat"); showChat.setLayoutX(502);showChat.setLayoutY(543);
		showChat.setStyle("-fx-pref-width: 95");
		showChat.setOnAction(e->{
			ctrl.chat.wakeUp();
			chat.show();
			
		});
		Button hideChat = new Button("HideChat"); hideChat.setLayoutX(502);hideChat.setLayoutY(573);
		hideChat.setStyle("-fx-pref-width: 95");
		hideChat.setOnAction(e->{
			ctrl.chat.sleep();
			chat.hide();
		});
		

		
		TextField addSubjects = new TextField(); addSubjects.setLayoutX(xBase+350); addSubjects.setLayoutY(yBase+400);
		addSubjects.setPromptText("addSubject");
		addSubjects.setOnAction(e->{
			client.sendMessage("request:add_subject\tcontent:"+addSubjects.getText());
			addSubjects.clear();
		});

/*
		TextField removeSubject = new TextField(); removeSubject.setLayoutX(350); removeSubject.setLayoutY(430);

		removeSubject.setPromptText("removeSubject");
		removeSubject.setStyle("-fx-pref-width: 150");
		removeSubject.setOnAction(e->{
			client.sendMessage("request:remove_subject\tcontent:"+removeSubject.getText());
<<<<<<< HEAD
			removeSubject.clear();
=======
			setSubjectNeedsUpdate=true;
>>>>>>> c6d56f4438324ababc695b4cd7608e227817129c
		});
		*/

		TextField createSubject = new TextField(); createSubject.setLayoutX(350); createSubject.setLayoutY(460);
		createSubject.setPromptText("createSubject");
		createSubject.setStyle("-fx-pref-width: 150");
		createSubject.setOnAction(e->{
			client.sendMessage("request:create_subject\tcontent:"+createSubject.getText());
			/*
			createSubject.clear();
			//stage.setScene(ctrl.getScene(5));
		});

		
		TextField setSubject = new TextField(); setSubject.setLayoutX(xBase+350); setSubject.setLayoutY(yBase+490);

		setSubject.setPromptText("setSubject");
		setSubject.setOnAction(e->{
			client.sendMessage("request:set_subject\tcontent:"+setSubject.getText());
			setSubject.clear();
		});
		
		
=======*/
			addSubjectNeedsUpdate=true;
		});


		Button resetScore = new Button("resetScore"); resetScore.setLayoutX(325); resetScore.setLayoutY(520);

		resetScore.setOnAction(e->{
			client.sendMessage("request:reset_score\tcontent:local");
			
		});
		
		Button requestScore = new Button("reqScore"); requestScore.setLayoutX(425); requestScore.setLayoutY(520);
		requestScore.setOnAction(e->{
			client.sendMessage("request:get_subject_scores\tcontent:");
		});



		root.getChildren().addAll(stats, serverFeed, subjectsGlobal, addSubject, showChat, hideChat, createSubject, resetScore, removeSubject, setSubject, tab1, tab2, tab3, tab4, scoreFeed, requestScore);

		
		
		updater1 = new FeedUpdater(client, stats, client.ProfileWindowStats);
		updater1.start();
		updater2 = new FeedUpdater(client, serverFeed, client.ProfileWindow);
		updater2.start();
		updater3 = new FeedUpdater(client, subjectsGlobal, client.ProfileWindowSubjectsGlobal);
		updater3.start();
		updater4 = new FeedUpdater(client, scoreFeed, client.ProfileWindowScores);
		updater4.start();
		updater5 = new FeedUpdater(client, subjectsLocal, client.ProfileWindowSubjectsLocal);
		updater5.start();
		Scene scene = new Scene(root, 600, 600);
		scene.getStylesheets().add(getClass().getResource("GUI.css").toExternalForm());
		

	return scene;
	}
	
	public void wakeUp(){
		stats.setText("Your stats:");	
		serverFeed.setText("Messages from server:");
		subjectsGlobal.clear();
		subjectsLocal.clear();
		scoreFeed.clear();
		setSubject.getItems().clear();
		removeSubject.getItems().clear();
		addSubject.getItems().clear();
		setSubjectNeedsUpdate=true;
		addSubjectNeedsUpdate=true;
		client.sendMessage("request:get_username\tcontent:");
		client.sendMessage("request:get_type\tcontent:");
		client.sendMessage("request:get_subjects\tcontent:local");
		client.sendMessage("request:get_subjects\tcontent:global");
		client.sendMessage("request:get_stats\tcontent:");
		
		
	}
	public void sleep(){
		
	}
}
