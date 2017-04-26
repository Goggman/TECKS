package javaClientCode;
import java.util.Iterator;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
public class QuestionWindow implements Window {
	QuestionSchema schema;
	ArrayList<RadioButton> radiOptions;
	int index=0;
	int quizStarted=0;
	Stage stage;
	Stage chat;
	GUIController ctrl;
	TextArea subjects;
	TextArea questions;
	TextArea serverIn;
	TextArea bestQuestions;
	TextArea feed;
	Label green;
	Label red;
	MenuButton pickCategory;
	MenuButton m;
	Button confirm;
	CheckBox check;
	ArrayList<QuestionSchema> schemas = new ArrayList<>();
	ArrayList<String> answers = new ArrayList<>();
	ServerClient client;
	int firstStart = 1;
	int SubjectNeedsUpdate = 1;
	int CategoryNeedsUpdate = 1;

	QuestionWindow(Stage stageInput, GUIController ctrlIn, ServerClient clientIn, Stage chatIn){
		client = clientIn;
		stage=stageInput;
		ctrl=ctrlIn;
		chat=chatIn;
		
	}
	public QuestionWindow(){
		
	}
	

	/**
	 * Method for creating a scene in gui controller
	 * @return scene1 Scene objekt
	 */
	public Scene createScene(){
		int xBase=100, yBase=70;
		subjects = new TextArea();
		questions = new TextArea();
		bestQuestions = new TextArea();
		
		Pane root = new Pane(); root.setStyle("-fx-background-color: white");

		Label title = new Label("Quiz");title.setLayoutX(100);title.setLayoutY(50);title.setStyle("-fx-font-size: 30px");
		green = new Label(" Correct"); green.setLayoutX(xBase+250);green.setLayoutY(yBase+350); green.setStyle("-fx-background-color: green"); green.setVisible(false); green.setPrefSize(70, 25);
		red = new Label(" Wrong"); red.setLayoutX(xBase+250); red.setLayoutY(yBase+350); red.setStyle("-fx-background-color: red"); red.setVisible(false);	red.setPrefSize(70, 25);
		feed = new TextArea(); feed.setLayoutX(xBase); feed.setLayoutY(yBase+150); feed.setStyle("-fx-border-color: black"); feed.setPrefSize(400, 200);feed.setEditable(false);
		check = new CheckBox("Enable hints?"); check.setLayoutX(xBase+250); check.setLayoutY(yBase+390);

		serverIn = new TextArea("InfoMessagesFromServer");serverIn.setLayoutX(100); serverIn.setLayoutY(500); serverIn.setStyle("-fx-border-color: black"); serverIn.setPrefSize(400, 100);serverIn.setEditable(false);
		Button nextQ = new Button("Next"); nextQ.setLayoutX(xBase+50); nextQ.setLayoutY(yBase+350);
		Button prevQ = new Button("Prev"); prevQ.setLayoutX(xBase); prevQ.setLayoutY(yBase+350);
		confirm  = new Button("Confirm"); confirm.setLayoutX(xBase+330); confirm.setLayoutY(yBase+350);
		TextField userInput = new TextField(); userInput.setPromptText("Type here"); userInput.setLayoutX(xBase+100); userInput.setLayoutY(yBase+350);
		
		pickCategory = new MenuButton(); pickCategory.setLayoutX(xBase+200); pickCategory.setLayoutY(yBase+100);pickCategory.setText("Categories");
		m = new MenuButton("Subjects"); m.setLayoutX(xBase+100); m.setLayoutY(yBase+100);

		

		Button tab1 = new Button("Quiz"); tab1.setLayoutX(92); tab1.setLayoutY(2); 
		tab1.setStyle("-fx-background-color: -fx-outer-border, -fx-inner-border, -fx-body-color; -fx-background-insets: 0, 1, 2;-fx-background-radius: 5, 4, 3;");

		tab1.setPrefWidth(100);
		tab1.setOnAction(e->{
			stage.setScene(ctrl.getScene(1));
			
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
		
		
		
		
		

		radiOptions = new ArrayList<>();
		ArrayList<String> options = new ArrayList<String>();
		options.add("");
		options.add("");
		options.add("");
		
		int radX = xBase+170, radY = yBase + 350;
		
		ToggleGroup tg = new ToggleGroup();
		
		for (String s : options){
			RadioButton temp = new RadioButton(s);
			temp.setLayoutX(radX);temp.setLayoutY(radY);

			temp.setToggleGroup(tg );
			temp.setOnAction(e -> {
				if (quizStarted!=1){
					return;
				}
				schema.getAnswers().put(schema.getQuestions().get(index), temp.getText());
				feed.setText(""+schema.getQuestions().get(index).getQuestionText()+"\nAnswer Given: "+schema.getAnswers().get(schema.getQuestions().get(index)));
				feed.setText(feed.getText()+"\n\n\n\n\n\n\n\n\n"+(index+1)+"/"+schema.getQuestions().size());
				if (check.isSelected()){
					Question question = schema.getQuestions().get(index);
					String correctAnswer = question.getCorrectAnswer();
					if (temp.getText().equals(correctAnswer)){
						green.setVisible(true);
						red.setVisible(false);
					}
					else{
						green.setVisible(false);
						red.setVisible(true);
					
					}
				}
				else{
					green.setVisible(false);
					red.setVisible(false);
				}
			
			});

			radiOptions.add(temp);
			temp.setVisible(false);
			radY += 20;
		}
		
		
		root.getChildren().addAll(radiOptions);
		
	
		userInput.setOnAction(e -> {
			if (quizStarted == 1){
				
				schema.getAnswers().put(schema.getQuestions().get(index), userInput.getText());
				feed.setText(""+schema.getQuestions().get(index).getQuestionText()+"\n Answer Given: "+schema.getAnswers().get(schema.getQuestions().get(index)));
				feed.setText(feed.getText()+"\n\n\n\n\n\n\n\n\n"+(index+1)+"/"+schema.getQuestions().size());
				if (check.isSelected()){
					Question question = schema.getQuestions().get(index);
					String correctAnswer = question.getCorrectAnswer();
					if (userInput.getText().equals(correctAnswer)){
						green.setVisible(true);
						red.setVisible(false);
					}
					else{
						green.setVisible(false);
						red.setVisible(true);
					
					}
				}
				else{
					green.setVisible(false);
					red.setVisible(false);
				}
				userInput.clear();

			}
			
		});	
		
		confirm.setOnAction(e-> {
			Analyzer analyzer = new Analyzer();
			if (quizStarted == 1){
				
				for (int i = 0; i < schema.getQuestions().size(); i++){
					Question question = schema.getQuestions().get(i);
					String correctAnswer = question.getCorrectAnswer();
					String answer = schema.getAnswers().get(question); 
					String category = question.getCategory();
					
					analyzer.registerAnswer(category, correctAnswer.equals(answer));
					
					
				}

				String results ="";
				for (String category : analyzer.getCategories().keySet()){
					results+="@"+category+"- correct answers: "+analyzer.getCategories().get(category)[0]+" #questions: "+analyzer.getCategories().get(category)[1]+"\n";
				}
				feed.setText("Your results: \n"+results);

				client.sendMessage("request:add_results\tcontent:"+analyzer.prepareContent());
				quizStarted=0;
				index=0;
				pickCategory.setDisable(false);
				m.setDisable(false);
			}
		});
		

		nextQ.setOnAction(e->{
			
			if (quizStarted==1){
				if (index < schema.getQuestions().size()-1){
					index++;
					green.setVisible(false);
					red.setVisible(false);
					
				} 
				tg.selectToggle(null);
				Question q = schema.getQuestions().get(index);
				if (q.getOptions().size() > 1){
					userInput.setVisible(false);
					for (RadioButton rb : radiOptions){
						rb.setVisible(true);
					}
					ArrayList<String> answerOptions=new ArrayList<String>(q.getOptions());
					answerOptions.add(q.getCorrectAnswer());
					Collections.shuffle(answerOptions);
					for (int k = 0, j = 0; k < answerOptions.size() && j < radiOptions.size(); k++, j++){
						radiOptions.get(j).setText(answerOptions.get(k));
					}
				}
				else {
					userInput.setVisible(true);
					for (RadioButton rb : radiOptions){
						rb.setVisible(false);
					}
				}	
				feed.setText(q.getQuestionText()+"\nAnswer given: "+schema.getAnswers().get(q));
				feed.setText(feed.getText()+"\n\n\n\n\n\n\n\n\n"+(index+1)+"/"+schema.getQuestions().size());
				if (index == schema.getQuestions().size()-1){
					prevQ.setDisable(false);nextQ.setDisable(true);
				} 
				else {
					//Do nothing
					
				}
			}
		});

		prevQ.setOnAction(e->{
			if (quizStarted==1){
				if (index!=0){
					index--;
					green.setVisible(false);
					red.setVisible(false);
				}
				tg.selectToggle(null);
				Question q = schema.getQuestions().get(index);
				ArrayList<String> answerOptions=new ArrayList<>(q.getOptions());
				answerOptions.add(q.getCorrectAnswer());
				if (answerOptions.size() > 1){
					userInput.setVisible(false);
					for (RadioButton rb : radiOptions){
						rb.setVisible(true);
					}
					
					Collections.shuffle(answerOptions);
					
					for (int k = 0, j = 0; k <answerOptions.size() && j < radiOptions.size(); k++, j++){
						radiOptions.get(j).setText(answerOptions.get(k));
						
						
					}
				}
				else {
					userInput.setVisible(true);
					for (RadioButton rb : radiOptions){
						rb.setVisible(false);
					}
				}
				feed.setText(q.getQuestionText()+"\nAnswer given: "+schema.getAnswers().get(q));
				feed.setText(feed.getText()+"\n\n\n\n\n\n\n\n\n"+(index+1)+"/"+schema.getQuestions().size());
				if (index==0){
					nextQ.setDisable(false);prevQ.setDisable(true);
				}
				else {
					//Do nothing
					
				}
			}
		});

		
	
		m.setPrefWidth(100);
		m.setOnMouseEntered(e->{

			if (subjects.getText().trim().isEmpty()){
				return;
			}
			if (SubjectNeedsUpdate ==1){
				m.getItems().clear();
				String[] subjectArray = subjects.getText().split("[\n]")[1].split("[@]");
				for(String subject : subjectArray){
					String itemsubject = subject;
					MenuItem item = new MenuItem(subject);
					item.setOnAction(x->{
						questions.clear();
						CategoryNeedsUpdate=1;
						client.sendMessage("request:set_subject\tcontent:"+itemsubject);
						client.sendMessage("request:get_questions\tcontent:");
						client.sendMessage("request:get_best_questions\tcontent:");
						pickCategory.setDisable(false);
					});
					m.getItems().add(item);
				}
				SubjectNeedsUpdate=0;
				CategoryNeedsUpdate=1;
			}
			

		});
		pickCategory.setPrefWidth(100);
		pickCategory.setOnMouseEntered(e->{
			HashMap<String, ArrayList<Question>> categoryQuestionMap = new HashMap<>();
			if (questions.getText().trim().isEmpty()){
				return;
			}
			if (CategoryNeedsUpdate==1 && SubjectNeedsUpdate==0){
				if (questions.getText().trim().isEmpty() || quizStarted ==1){
					return;
				}
				pickCategory.getItems().clear();
				QuestionSchema schemaTemp = readToSchema(questions.getText().trim());
				
				for(Question q : schemaTemp.getQuestions()){
					if (categoryQuestionMap.containsKey(q.getCategory())){
						categoryQuestionMap.get(q.getCategory()).add(q);
					}
					else{
						categoryQuestionMap.put(q.getCategory(), new ArrayList<Question>());
						categoryQuestionMap.get(q.getCategory()).add(q);
					}
				}
				Iterator<String> categories_it = categoryQuestionMap.keySet().iterator();
				while (categories_it.hasNext()){
					String next = categories_it.next();
					MenuItem item = new MenuItem(next);
					item.setOnAction(x->{
						setSchema(new QuestionSchema(categoryQuestionMap.get(next),0));
						feed.setText(schema.getQuestions().get(index).getQuestionText()+"\nAnswer given: "+schema.getAnswers().get(schema.getQuestions().get(index)));
						feed.setText(feed.getText()+"\n\n\n\n\n\n\n\n\n"+(index+1)+"/"+schema.getQuestions().size());
						quizStarted=1;
						pickCategory.setDisable(true);
						m.setDisable(true);
						Question q = schema.getQuestions().get(index);
						ArrayList<String> answerOptions = new ArrayList<>(q.getOptions());
						answerOptions.add(q.getCorrectAnswer());
						if (answerOptions.size() > 1){
							userInput.setVisible(false);
							for (RadioButton rb : radiOptions){
								rb.setVisible(true);
							}
							Collections.shuffle(answerOptions);
							for (int k = 0, j = 0; k < answerOptions.size() && j < radiOptions.size(); k++, j++){
								radiOptions.get(j).setText(answerOptions.get(k));
							}
						}
						else {
							userInput.setVisible(true);
							for (RadioButton rb : radiOptions){
								rb.setVisible(false);
							}
						}
					});
					pickCategory.getItems().add(item);
				}
				CategoryNeedsUpdate=0;
			
			
			
			MenuItem recommended = new MenuItem("Recommended questions");
			recommended.setOnAction(z->{
				if (bestQuestions.getText().trim().isEmpty()){
					return;
				}
				if(!categoryQuestionMap.containsKey(bestQuestions.getText().trim())) {
					return;
				}
				
				setSchema(new QuestionSchema(categoryQuestionMap.get(bestQuestions.getText().trim()),0));
				feed.setText(schema.getQuestions().get(index).getQuestionText()+"\n Answer given: "+schema.getAnswers().get(schema.getQuestions().get(index)));
				feed.setText(feed.getText()+"\n\n\n\n\n\n\n\n\n"+(index+1)+"/"+schema.getQuestions().size());
				quizStarted=1;
				pickCategory.setDisable(true);
				m.setDisable(true);
				Question q = schema.getQuestions().get(index);
				ArrayList<String> answerOption = new ArrayList<>(q.getOptions());
				answerOption.add(q.getCorrectAnswer());
				if (answerOption.size() > 1){
					userInput.setVisible(false);
					for (RadioButton rb : radiOptions){
						rb.setVisible(true);
					}
					Collections.shuffle(answerOption);
					for (int k = 0, j = 0; k < answerOption.size() && j < radiOptions.size(); k++, j++){
						radiOptions.get(j).setText(answerOption.get(k));
					}
				}
				else {
					userInput.setVisible(true);
					for (RadioButton rb : radiOptions){
						rb.setVisible(false);
					}
				}
			});
			pickCategory.getItems().add(recommended);
			}
		});

		
		root.getChildren().addAll(feed, confirm, nextQ, prevQ, pickCategory, serverIn, showChat, hideChat, m, tab1, tab2, tab3, tab4, userInput, title, green, red, check);

		Scene scene1 = new Scene(root, 600, 600);
		scene1.getStylesheets().add(getClass().getResource("GUI.css").toExternalForm());


		FeedUpdater updater1 = new FeedUpdater(client, subjects, client.QuestionWindowSubjects);
		updater1.start();
		FeedUpdater updater2 = new FeedUpdater(client, serverIn, client.QuestionWindowInfo);
		updater2.start();
		FeedUpdater updater3 = new FeedUpdater(client, questions, client.QuestionWindowQuestions);
		updater3.start();
		FeedUpdater updater4 = new FeedUpdater(client, bestQuestions, client.QuestionWindowRecQs);
		updater4.start();
		return scene1;
	}
	
	public void addSchema(QuestionSchema qs){	
		schemas.add(qs);
		setSchema(qs);
	}
	
	
	/**
	 * 
	 * @return schema returns schema for the quiz window
	 */
	public QuestionSchema getSchema(){
		return schema;
	}

	public ArrayList<QuestionSchema> getSchemas(){
		return schemas;
	}
	public void setSchema(QuestionSchema questionSchema) {
		schema = questionSchema;
		
	}
	
	public void setAnswer(String a, String b){
		b = a;
		
	}
	
	public QuestionSchema readToSchema(String content) throws IndexOutOfBoundsException{ //Returnerer en liste av spørsmål i standard format, fra server til gjennomførbar quiz
		//Ment for bruk av Question WIndow når skal sjekke hvilke quizer som er tilgjengelige
		
		//; instead of :, | instead of \n, @ for start of new question
		//Header;header1|c;category1|op;op1|op;op2|a;answer1
		ArrayList<ArrayList<String>> textData = new ArrayList<>();
		String[] rawQuestionArray = content.split("@"); // array of questions
		int i =-1;
		for(String rawQuestion : rawQuestionArray){
			String q = rawQuestion.replace('|', '\n');
			q=q.replace(';', ':');
			
			Scanner scanner = new Scanner(q);
			
			while (scanner.hasNext()){
				String next = scanner.nextLine();
				if (next.contains("Header:")){
					textData.add(new ArrayList<String>());
					i++;
					}
				textData.get(i).add(next);
			}
			scanner.close();
		}
		
		
		QuestionSchema s = new QuestionSchema(textData);
		
		return s;
		}
	
	public void wakeUp(){
		for (RadioButton b :radiOptions){
			b.setVisible(false);
		}
		
		red.setVisible(false);
		green.setVisible(false);
		quizStarted=0;
		CategoryNeedsUpdate=1;
		SubjectNeedsUpdate=1;
		subjects.clear();
		questions.clear();
		bestQuestions.clear();
		serverIn.clear();
		feed.setText("Pick a subject to continue");
		pickCategory.setDisable(true);
		m.setDisable(false);
		client.sendMessage("request:get_subjects\tcontent:local");
		if (firstStart==1){
			feed.setText("Pick a subject to continue :)");
			serverIn.setText("Messages from server\n");
			firstStart=0;
		}
		
	}	
	
	public void sleep(){
		
	}
	

}
