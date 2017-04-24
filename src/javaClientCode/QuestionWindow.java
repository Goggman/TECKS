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
	TextArea feed;
	ArrayList<QuestionSchema> schemas = new ArrayList<>(); //list of categories
	ArrayList<String> answers = new ArrayList<>(); //input answers from user
	ServerClient client;
	int firstStart = 1;
	int SubjectNeedsUpdate = 1;
	int CategoryNeedsUpdate = 1;
	public void addSchema(QuestionSchema qs){
		
		schemas.add(qs);
		setSchema(qs);
	}
	
	QuestionWindow(Stage stageInput, GUIController ctrlIn, ServerClient clientIn, Stage chatIn){
		client = clientIn;
		stage=stageInput;
		ctrl=ctrlIn;
		chat=chatIn;
		
	}
	public void setAnswer(String a, String b){
		b = a;
		
	}

	/**
	 * Method for creating a scene in gui controller
	 * @return scene1 Scene objekt
	 */
	public Scene createScene(){
		int xBase=100, yBase=0;
		subjects = new TextArea();
		questions = new TextArea();
		
		Pane root = new Pane(); root.setStyle("-fx-background-color: white");
		Label title = new Label("Quiz");title.setLayoutX(200);title.setLayoutY(50);title.setStyle("-fx-font-size: 30px");
		feed = new TextArea(); feed.setLayoutX(xBase); feed.setLayoutY(yBase+100); feed.setStyle("-fx-border-color: black"); feed.setPrefSize(400, 200);feed.setEditable(false);
		serverIn = new TextArea("InfoMessagesFromServer");serverIn.setLayoutX(xBase); serverIn.setLayoutY(yBase+450); serverIn.setStyle("-fx-border-color: black"); serverIn.setPrefSize(400, 100);serverIn.setEditable(false);
		Button nextQ = new Button("Next"); nextQ.setLayoutX(xBase+50); nextQ.setLayoutY(yBase+300);
		Button prevQ = new Button("Prev"); prevQ.setLayoutX(xBase+0); prevQ.setLayoutY(yBase+300);
		Button confirm = new Button("Confirm"); confirm.setLayoutX(xBase+100); confirm.setLayoutY(yBase+300);
		TextField userInput = new TextField(); userInput.setPromptText("Type here"); userInput.setLayoutX(xBase+0); userInput.setLayoutY(yBase+330);
		TextField setSubject = new TextField();  setSubject.setLayoutX(xBase+300); setSubject.setLayoutY(yBase+390);
		Button loadQuestionsFromServer = new Button("LoadQuestions");  loadQuestionsFromServer.setLayoutX(xBase+0); loadQuestionsFromServer.setLayoutY(yBase+390);
		MenuButton pickCategory = new MenuButton(); pickCategory.setLayoutX(xBase+300); pickCategory.setLayoutY(yBase+390);pickCategory.setText("Categories");
		Button load = new Button("Load"); load.setLayoutX(xBase+200);load.setLayoutY(yBase+390);
		
		
		Button tab1 = new Button("Quiz"); tab1.setLayoutX(100); tab1.setLayoutY(0); tab1.setStyle("-fx-background-color: -fx-outer-border, -fx-inner-border, -fx-body-color; -fx-background-insets: 0, 1, 2;-fx-background-radius: 5, 4, 3;");
		tab1.setPrefWidth(100);
		tab1.setOnAction(e->{
			stage.setScene(ctrl.getScene(1)); //QuestionScene at index 1 in GUIctrl
			
		});
		
		Button tab2 = new Button("Create Quiz"); tab2.setLayoutX(200); tab2.setLayoutY(0); tab2.setStyle("-fx-background-color: -fx-outer-border, -fx-inner-border, -fx-body-color; -fx-background-insets: 0, 1, 2;-fx-background-radius: 5, 4, 3;");
		tab2.setPrefWidth(100);
		tab2.setOnAction(e->{
			stage.setScene(ctrl.getScene(2));
		});
		
		////
		
		Button tab3 = new Button("Log out"); tab3.setLayoutX(400); tab3.setLayoutY(0); tab3.setStyle("-fx-background-color: -fx-outer-border, -fx-inner-border, -fx-body-color; -fx-background-insets: 0, 1, 2;-fx-background-radius: 5, 4, 3;");
		tab3.setPrefWidth(100);
		tab3.setOnAction(e->{
			stage.setScene(ctrl.getScene(4));
			//TODO: log user out
		});
		
		Button tab4 = new Button("Profile"); tab4.setLayoutX(300); tab4.setLayoutY(0); tab4.setStyle("-fx-background-color: -fx-outer-border, -fx-inner-border, -fx-body-color; -fx-background-insets: 0, 1, 2;-fx-background-radius: 5, 4, 3;");
		tab4.setPrefWidth(100);
		tab4.setOnAction(e->{
			stage.setScene(ctrl.getScene(5));
		});
		Button showChat = new Button("ShowChat"); showChat.setLayoutX(500);showChat.setLayoutY(550);
		showChat.setPrefWidth(100);
		showChat.setOnAction(e->{
			ctrl.chat.wakeUp();
			chat.show();
			
		});
		Button hideChat = new Button("HideChat"); hideChat.setLayoutX(500);hideChat.setLayoutY(575);
		hideChat.setPrefWidth(100);
		hideChat.setOnAction(e->{
			ctrl.chat.sleep();
			chat.hide();
		});
		
		
		
		

		radiOptions = new ArrayList<>();
		ArrayList<String> options = new ArrayList<String>();
		options.add("");
		options.add("");
		options.add("");
		
		int radX = xBase+200, radY = yBase + 300;
		
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
				feed.setText(""+schema.getQuestions().get(index).getQuestionText()+"\n Answer Given: "+schema.getAnswers().get(schema.getQuestions().get(index)));
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
				userInput.clear();
			}
			
		});

		
		
		//
		
		setSubject.setPrefWidth(100);
		setSubject.setPromptText("Set subject");
		//get subject from server
		setSubject.setOnAction(e->{
			client.sendMessage("request:set_subject\tcontent:"+setSubject.getText().toUpperCase());
			client.sendMessage("request:get_questions\tcontent:");

		});


		loadQuestionsFromServer.setPrefWidth(100);
		/*
		loadQuestionsFromServer.setOnAction(e->{
			if (!metafeed.getText().equals("")){
				try {
				Scanner scanner = new Scanner(metafeed.getText());
				System.out.println("120: " + scanner.nextLine());scanner.nextLine();//Skips first lines, as it contains sender
				String content = scanner.nextLine();
				addSchema(readToSchema(content));
				scanner.close();
				metafeed.setText("");
				serverIn.setText("Load successfull");
				}
				catch (IndexOutOfBoundsException ex){
					serverIn.setText("no quiz found for this subject");
				}
				
			}
			else{
				serverIn.setText("Load unsuccessfull");
			}
			
		});
		*/


		
		
		
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

				/*String appender = "";
				Iterator<String> it = analyzer.getCategories().keySet().iterator();
				while (it.hasNext()){
					String next = it.next().toString();
					appender += next + ": "
							+ ""+ analyzer.getCategories().get(next)[0] + "/" + analyzer.getCategories().get(next)[1] + "\n";
					
				}
				feed.setText("Your results: \n" + appender);*/

				String results ="";
				for (String category : analyzer.getCategories().keySet()){
					results+="@"+category+"- correct answers: "+analyzer.getCategories().get(category)[0]+" #questions: "+analyzer.getCategories().get(category)[1]+"\n";
				}
				feed.setText("Your results: \n"+results);

				client.sendMessage("request:add_results\tcontent:"+analyzer.prepareContent());
				quizStarted=0;
			}
		});
		
		//

		nextQ.setOnAction(e->{
			
			if (quizStarted==1){
				if (index < schema.getQuestions().size()-1){
					index++;
					
				} 
				tg.selectToggle(null);
				Question q = schema.getQuestions().get(index);
				if (q.getOptions().size() > 1){
					userInput.setVisible(false);
					for (RadioButton rb : radiOptions){
						rb.setVisible(true);
					}
					Collections.shuffle(q.getOptions());
					for (int k = 0, j = 0; k < q.getOptions().size() && j < radiOptions.size(); k++, j++){
						radiOptions.get(j).setText(q.getOptions().get(k));
					}
				}
				else {
					userInput.setVisible(true);
					for (RadioButton rb : radiOptions){
						rb.setVisible(false);
					}
				}	
				feed.setText(q.getQuestionText()+"\n Answer given: "+schema.getAnswers().get(q));
				
				if (index == schema.getQuestions().size()-1){
					System.out.println("false");
					prevQ.setDisable(false);nextQ.setDisable(true);
				} else {
					System.out.println("true");
					
				}
			}
		});

		prevQ.setOnAction(e->{
			if (quizStarted==1){
				if (index!=0){
					index--;
				}
				tg.selectToggle(null);
				Question q = schema.getQuestions().get(index);
				if (q.getOptions().size() > 1){
					userInput.setVisible(false);
					for (RadioButton rb : radiOptions){
						rb.setVisible(true);
					}
					
					Collections.shuffle(q.getOptions());
					
					for (int k = 0, j = 0; k < q.getOptions().size() && j < radiOptions.size(); k++, j++){
						radiOptions.get(j).setText(q.getOptions().get(k));
						
						
					}
				}
				else {
					userInput.setVisible(true);
					for (RadioButton rb : radiOptions){
						rb.setVisible(false);
					}
				}
				feed.setText(q.getQuestionText()+"\n Answer given: "+schema.getAnswers().get(q));
				
				if (index==0){
					System.out.println("false");
					nextQ.setDisable(false);prevQ.setDisable(true);
				}
				else {
					System.out.println("true");
					
				}
			}
		});

		
		MenuButton m = new MenuButton("Subjects"); m.setLayoutX(xBase+300); m.setLayoutY(yBase+420);
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
						client.sendMessage("request:set_subject\tcontent:"+itemsubject);
						client.sendMessage("request:get_questions\tcontent:");
					});
					m.getItems().add(item);
				}
				SubjectNeedsUpdate=0;
				CategoryNeedsUpdate=1;
			}
			

		});
		pickCategory.setPrefWidth(100);
		pickCategory.setOnMouseEntered(e->{
			if (questions.getText().trim().isEmpty()){
				return;
			}
			if (CategoryNeedsUpdate==1 && SubjectNeedsUpdate==0){
				if (questions.getText().trim().isEmpty() || quizStarted ==1){
					return;
				}
				pickCategory.getItems().clear();
				System.out.println(questions.getText().trim());
				QuestionSchema schemaTemp = readToSchema(questions.getText().trim());
				HashMap<String, ArrayList<Question>> categoryQuestionMap = new HashMap<>();
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
					//System.out.println(next);
					MenuItem item = new MenuItem(next);
					item.setOnAction(x->{
						setSchema(new QuestionSchema(categoryQuestionMap.get(next),0));
						feed.setText(schema.getQuestions().get(index).getQuestionText()+"\n Answer given: "+schema.getAnswers().get(schema.getQuestions().get(index)));
						quizStarted=1;
						Question q = schema.getQuestions().get(index);
						if (q.getOptions().size() > 1){
							userInput.setVisible(false);
							for (RadioButton rb : radiOptions){
								rb.setVisible(true);
							}
							
							Collections.shuffle(q.getOptions());
							for (int k = 0, j = 0; k < q.getOptions().size() && j < radiOptions.size(); k++, j++){
								radiOptions.get(j).setText(q.getOptions().get(k));
								
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
			}
		});
		/*

		m.showingProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue){
				if (newValue){
					
					if (!	serverIn.getText().equals("")){
						Scanner scanner = new Scanner(serverIn.getText());
						
						
						while (scanner.hasNextLine()){
							String next = scanner.nextLine();
							if (next.contains("@")){
								
							String [] subjects = next.split("@");
							for (String s : subjects){
								
								MenuItem temp = new MenuItem(s);
								addMenuItem(temp);
								temp.setOnAction(e -> {
									//TODO: set subject
									client.sendMessage("request:set_subject\tcontent:" + ((MenuItem) e.getSource()).getText());
									//client.sendMessage("request:set_subject\tcontent:"+setSubject.getText().toUpperCase());
									client.sendMessage("request:get_questions\tcontent:");
									
									
									loadQuestionsFromServer.fire();
									
									
								});
							}
							}
						}
						
						//addSchema(readToSchema(content));
						//pickSubject.getItems().setAll(items);
						scanner.close();
						//metafeed.setText(""); 
						//serverIn.setText("asd");
					
					
					
					}
				}
			}

			public void addMenuItem(MenuItem menuItem){
				MenuItem temp = menuItem;
				ObservableList<MenuItem> items = m.getItems();
				boolean go = true;
				for (MenuItem mi : items){
					if (mi.getText().equals(temp.getText())){
						go = false;
					}
					
				}
				if (go){
					
					m.getItems().add(temp);
				}
			}
			
		});
		
		load.setStyle("-fx-pref-width: 54");
		load.setOnAction(e -> {
			System.out.println("clicked");
			if (pickCategory.getItems().size() < schemas.size()){
				for (int i = 0; i < schemas.size(); i++){
					
					MenuItem temp = new MenuItem(""+(i + 1) +": "+ schemas.get(i).category);
					boolean go = true;
					for (MenuItem mi : pickCategory.getItems()){ //check for duplicates 
						if(mi.getText().equals(temp.getText()) || 
								mi.getText().endsWith(temp.getText().split(":")[1])){
							go = false;
						}
					}
					if (go){
						pickCategory.getItems().add(temp); 
						temp.setOnAction(ev -> {
							index = 0;
							try {
								
								schema = schemas.get(Integer.parseInt(temp.getText().split(":")[0])-1);
								feed.setText(""+schema.getQuestions().get(index).getQuestionText());
								
								if (schema.getQuestions().get(0).getOptions().size() > 1){
									userInput.setVisible(false);
									for (int k = 0, j = 0; k < schema.getQuestions().get(0).getOptions().size() &&
											j < radiOptions.size(); k++, j++){
										radiOptions.get(j).setText(schema.getQuestions().get(0).getOptions().get(k));
										radiOptions.get(j).setVisible(true);
										radiOptions.get(j).setOnAction(eve -> {
											if (quizStarted == 1){
												schema.getAnswers().put(schema.getQuestions().get(index), ((RadioButton) eve.getSource()).getText());
												feed.setText(""+schema.getQuestions().get(index).getQuestionText()+"\n Answer Given: "+((RadioButton) eve.getSource()).getText());
												
											}
										});
									}
								}
								else {
									userInput.setVisible(true);
									for (RadioButton rb : radiOptions){
										rb.setVisible(false);
									}
								}
								quizStarted=1;
								
							}
							catch (IndexOutOfBoundsException ex){
								feed.setText("no quiz selected");
							}
							catch (NullPointerException eex){
								feed.setText("no schema found");
							}
						});
					}
				}
			}
			else if (schemas.size() < 1){
				feed.setText("No schema found \n try loading one from file");
			}
		});

		loadQuestionsFromServer.setOnAction(e->{
			System.out.println("questions loaded");
			try {
				
				//TimeUnit.SECONDS.sleep(5);
				Thread.sleep(3000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (!metafeed.getText().equals("")){
				try {
					Scanner scanner = new Scanner(metafeed.getText());
					scanner.nextLine();scanner.nextLine();//Skips first lines, as it contains sender
					String content = scanner.nextLine();
					addSchema(readToSchema(content));
					scanner.close();
					metafeed.setText("");
					serverIn.setText("Load successfull");
					load.fire();
				}
				catch (IndexOutOfBoundsException ex){
					serverIn.setText("no quiz found for this subject");
				}
				
				
			}
			else{
				serverIn.setText("Load unsuccessfull");
			}
			
		});

		*/
		root.getChildren().addAll(feed, confirm, nextQ, prevQ, pickCategory, /*load, setSubject, loadQuestionsFromServer,*/ serverIn, showChat, hideChat, m, tab1, tab2, tab3, tab4, userInput, title);
		Scene scene1 = new Scene(root, 600, 600);
		//scene1.getStylesheets().add(getClass().getResource("GUI.css").toExternalForm());


		FeedUpdater updater1 = new FeedUpdater(client, subjects, client.QuestionWindowSubjects);
		updater1.start();
		FeedUpdater updater2 = new FeedUpdater(client, serverIn, client.QuestionWindowInfo);
		updater2.start();
		FeedUpdater updater3 = new FeedUpdater(client, questions, client.QuestionWindowQuestions);
		updater3.start();
		return scene1;
	}
	
	/**
	 * 
	 * @return schema returns schema for the quiz window
	 */
	QuestionSchema getSchema(){
		return schema;
	}

	public void setSchema(QuestionSchema questionSchema) {
		schema = questionSchema;
		
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
				System.out.println(next);
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
		quizStarted=0;
		CategoryNeedsUpdate=1;
		SubjectNeedsUpdate=1;
		subjects.clear();
		questions.clear();
		serverIn.clear();
		feed.setText("Pick a category to continue");
		client.sendMessage("request:get_subjects\tcontent:local");
		if (firstStart==1){
			feed.setText("Ready to learn? :)");
			serverIn.setText("Messages from server\n");
			firstStart=0;
		}
		
	}	
	
	public void sleep(){
		
	}
	

}
