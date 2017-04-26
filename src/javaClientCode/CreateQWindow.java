package javaClientCode;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

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
import javafx.scene.control.Label;

public class CreateQWindow implements Window {
	Stage stage;
	GUIController ctrl;
	ServerClient client;
	TextArea serverFeed;
	Stage chat;
	int index=0;
	int SubjectNeedsUpdate =1;
	TextArea subjects;
	ArrayList<Question> quiz;
	CreateQWindow(Stage stageIn, GUIController ctrlIn, ServerClient clientIn, Stage chatIn){
		stage=stageIn;
		ctrl=ctrlIn;
		client=clientIn;
		chat = chatIn;
	}
	
	/**
	 * create a scene with create question forms
	 * @return Scene
	 */
	public Scene createScene(){
		int xBase=100, yBase=0;

		int fieldBaseX=130, fieldBaseY=300;
		
		
		quiz = new ArrayList<>();
		subjects = new TextArea();
		TextArea feed = new TextArea("QuestionMaker"); feed.setLayoutX(xBase); feed.setLayoutY(yBase+100); feed.setStyle("-fx-border-color: black");feed.setPrefSize(400, 150);feed.setEditable(false);feed.setWrapText(true);
		Pane root = new Pane(); root.setStyle("-fx-background-color: white");
		Label title = new Label("Create Quiz");title.setLayoutX(100);title.setLayoutY(50);title.setStyle("-fx-font-size: 30px; text-decoration: underline");
		TextField head = new TextField(); head.setLayoutX(xBase+fieldBaseX+0); head.setLayoutY(yBase+fieldBaseY+30); head.setPromptText("Set header"); head.setVisible(false); head.setDisable(true);
		TextField category = new TextField(); category.setLayoutX(xBase+fieldBaseX);category.setLayoutY(yBase+fieldBaseY+2);category.setPromptText("Category");
		TextField qText = new TextField(); qText.setLayoutX(xBase+fieldBaseX+0); qText.setLayoutY(yBase+fieldBaseY+32);qText.setPromptText("Question");
		TextField aText = new TextField(); aText.setLayoutX(xBase+fieldBaseX+0); aText.setLayoutY(yBase+fieldBaseY+62);aText.setPromptText("Answer");
		TextField op1 = new TextField(); op1.setLayoutX(xBase+fieldBaseX);op1.setLayoutY(yBase+fieldBaseY+92);op1.setPromptText("Option 1");op1.setDisable(true);
		TextField op2 = new TextField(); op2.setLayoutX(xBase+fieldBaseX);op2.setLayoutY(yBase+fieldBaseY+122);op2.setPromptText("Option 2");op2.setDisable(true);
		Button createQ = new Button("Create"); createQ.setLayoutX(xBase+fieldBaseX); createQ.setLayoutY(yBase+fieldBaseY+152);
		Button save = new Button("Save q's");save.setLayoutX(xBase+fieldBaseX+75);save.setLayoutY(yBase+fieldBaseY+152);
		MenuButton setSubjectMenu = new MenuButton("setSubject"); setSubjectMenu.setLayoutX(xBase+fieldBaseX-110); setSubjectMenu.setLayoutY(yBase+fieldBaseY+60);
		setSubjectMenu.setPrefWidth(100);
		
		createQ.setStyle("-fx-pref-width: 73");
		
	
		save.setStyle("-fx-pref-width: 73");

		
		Button nextQ = new Button("Next"); nextQ.setLayoutX(xBase+51); nextQ.setLayoutY(yBase+252);
		Button prevQ = new Button("Prev"); prevQ.setLayoutX(xBase); prevQ.setLayoutY(yBase+252);
		Button discard = new Button("discQ"); discard.setLayoutX(xBase+103); discard.setLayoutY(yBase+252);
	


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
		
		
		
		
		setSubjectMenu.setOnMouseEntered(e->{
			if (subjects.getText().trim().isEmpty()){
				return;
			}
			if (SubjectNeedsUpdate==1){
				setSubjectMenu.getItems().clear();
				String[] subjectArray = subjects.getText().trim().split("[@]");
				for (String subject : subjectArray){
					String itemsubject = subject;
					MenuItem item = new MenuItem(subject);
					item.setOnAction(x->{
						client.sendMessage("request:set_subject\tcontent:"+itemsubject);
					});
					setSubjectMenu.getItems().add(item);
				}
				SubjectNeedsUpdate = 0;
			}
		});

		Label error = new Label("Needs setSubject");error.setVisible(false);
		
		save.setDisable(true);
		
		MenuItem mulChoice = new MenuItem(); mulChoice.setText("Multiple choice");
		MenuItem fillIn = new MenuItem(); fillIn.setText("Fill in the blank");

		MenuButton qType = new MenuButton("Fill in", null, mulChoice, fillIn); qType.setLayoutX(xBase+fieldBaseX-110);qType.setLayoutY(yBase+fieldBaseY+93);
		qType.setPrefWidth(100);

		//choose question type
		mulChoice.setOnAction(e -> {
			op1.setDisable(false);
			op2.setDisable(false);
			qType.setText("Multiple choice");
		});
		fillIn.setOnAction(e -> {
			op1.setDisable(true);
			op2.setDisable(true);
			qType.setText("Fill in");
		});
		//set category

		
		nextQ.setOnAction(e->{//TODO: nextQ prevQ and discard is throwing out of bounds exceptions, fix this

			if (quiz.isEmpty()){
				return;
			}
			feed.setText(quiz.get(index).getHeader()+"\n"+quiz.get(index).getCategory()+"\n"+quiz.get(index).getQuestionText()+"\n"+quiz.get(index).getOptions().toString());
			if (index+1<quiz.size()){
				index++;
				String options = quiz.get(index).getOptions().toString();
				feed.setText(quiz.get(index).getHeader()+"\n"+quiz.get(index).getCategory()+"\n"+quiz.get(index).getQuestionText()+"\n"+quiz.get(index).getOptions().toString());
			}
		});
		prevQ.setOnAction(e->{

			if (quiz.isEmpty()){
				return;
			}
			feed.setText(quiz.get(index).getHeader()+"\n"+quiz.get(index).getCategory()+"\n"+quiz.get(index).getQuestionText()+"\n"+quiz.get(index).getOptions().toString());
			if (index-1>-1){
				index--;
				feed.setText(quiz.get(index).getHeader()+"\n"+quiz.get(index).getCategory()+"\n"+quiz.get(index).getQuestionText()+"\n"+quiz.get(index).getOptions().toString());
			}
		});
		discard.setOnAction(e->{

			if (!quiz.isEmpty()){
				int number = index+1;
				quiz.remove(index);
				feed.setText("Question number "+number+" removed");
				if (index>0){
					index--;
				}
				
			}
		});
	
		createQ.setOnAction(e->{
			String questionText = qText.getText();
			String answerText = aText.getText();
			String headerText = qType.getText();
			String categoryText = category.getText();
			if (headerText.isEmpty() || questionText.isEmpty() || answerText.isEmpty() || categoryText.isEmpty()){
				feed.setText("Need values for Header, Question and answer");
			}else {
				save.setDisable(false);
			}
			
			feed.setText(""+headerText+"\n"+categoryText+"\n"+questionText+"\n"+answerText+"\n"+op1.getText()+"\n"+op2.getText());
			if (qType.getText().equals("Multiple choice")){
				quiz.add(new Question(categoryText, questionText, answerText, headerText, op1.getText(), op2.getText()));
				
			} else {
				quiz.add(new Question(categoryText, questionText, answerText, headerText));
			}
			qText.clear();
			aText.clear();
			head.clear();
			category.clear();
			op1.clear();
			op2.clear();
		});
		
		//send questions to server
		save.setOnAction(e -> {
				error.setVisible(false);
				createQuestionToServer(quiz);
		});
		
		//Setup serverFeed and the updater to maintain the feed
		serverFeed = new TextArea(); serverFeed.setLayoutX(xBase); serverFeed.setLayoutY(yBase+500); serverFeed.setStyle("-fx-border-color: black"); serverFeed.setPrefSize(400, 100);serverFeed.setEditable(false);
		serverFeed.setWrapText(true);
		FeedUpdater updater = new FeedUpdater(client, serverFeed, client.CreateQWindow);
		updater.start();

		FeedUpdater updater2 = new FeedUpdater(client, subjects, client.CreateQWindowSubjects);
		updater2.start();
		root.getChildren().addAll(setSubjectMenu, qType, qText, aText, feed, head, createQ, op1, op2, save, error, category, serverFeed, tab1, tab2, tab3, tab4, showChat, hideChat, nextQ, prevQ, discard, title);
		Scene scene = new Scene(root, 600, 600);
		scene.getStylesheets().add(getClass().getResource("GUI.css").toExternalForm());

		return scene;
	}
	
	
	/**
	 * Load question object to file
	 * @param setSubject 
	 * @param q Question object to be saved
	 * @throws IOException
	 */

	
	
	public void createQuestionToServer(ArrayList<Question> q){

		for (Question question : q){
			String questionToServer ="request:add_question\tcontent:"+"Header; "+question.getHeader()+"|"
																	+"c; "+question.getCategory()+"|"
																	+"q; "+question.getQuestionText()+"|";
			
			for (String option : question.getOptions()){
				questionToServer+="op; "+option+"|";
			}
			questionToServer+="a; "+question.getCorrectAnswer();
			
			client.sendMessage(questionToServer);
		}		
		

		
	}








	public void wakeUp(){
		SubjectNeedsUpdate=1;
		serverFeed.setText("Messages From Server:");
		subjects.clear();
		client.sendMessage("request:get_subjects\tcontent:local");
	}
	public void sleep(){
		
	}
}