package javaClientCode;

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
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;

public class QuestionWindow implements Window {
	QuestionSchema schema;
	int index;
	int quizStarted;
	Stage stage;
	Stage chat;
	GUIController ctrl;
	Label metafeed;
	ArrayList<QuestionSchema> schemas = new ArrayList<>(); //list of categories
	ArrayList<String> answers = new ArrayList<>(); //input answers from user
	ServerClient client;
	public void addSchema(QuestionSchema qs){
		
		schemas.add(qs);
		setSchema(qs);
	}
	
	QuestionWindow(Stage stageInput, GUIController ctrlIn, ServerClient clientIn, Stage chatIn){
		client = clientIn;
		index=0;
		quizStarted = 0;
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
		int xBase=300, yBase=200;
		Pane root = new Pane(); root.setStyle("-fx-background-color: white");
		Label feed = new Label(); feed.setLayoutX(xBase+100); feed.setLayoutY(yBase-90); feed.setStyle("-fx-border-color: black"); feed.setPrefSize(500, 300); feed.setAlignment(Pos.TOP_LEFT);
		//userinput
		ArrayList<RadioButton> radiOptions = new ArrayList<>();
		ArrayList<String> options = new ArrayList<String>();
		options.add("a)");
		options.add("b)");
		options.add("c)");
		
		
		ToggleGroup tg = new ToggleGroup(); //group of radiobuttons
		int radX = xBase+100, radY = yBase + 230;
		
		for (String s : options){
			RadioButton temp = new RadioButton(s);
			temp.setLayoutX(radX);temp.setLayoutY(radY);
			temp.setToggleGroup(tg);
			temp.setVisible(false);
			radiOptions.add(temp);
			radY += 30;
		}
		root.getChildren().addAll(radiOptions);
		
		TextField userInput = new TextField(); userInput.setPromptText("Type here"); userInput.setLayoutX(xBase+100); userInput.setLayoutY(yBase+230);
		root.getChildren().add(userInput);
		userInput.setOnAction(e -> {
			if (quizStarted == 1){
				
				schema.getAnswers().put(schema.getQuestions().get(index), userInput.getText());
				feed.setText(""+schema.getQuestions().get(index).getQuestionText()+"\n Answer Given: "+userInput.getText());
				userInput.clear();
			}
			
		});

		
		
		//
		Label serverIn = new Label("InfoMessagesFromServer");serverIn.setLayoutX(xBase-300); serverIn.setLayoutY(yBase-90); serverIn.setStyle("-fx-border-color: black"); serverIn.setPrefSize(200, 400); serverIn.setAlignment(Pos.TOP_LEFT);
		metafeed = new Label("");
		TextField setSubject = new TextField();  setSubject.setLayoutX(xBase-300); setSubject.setLayoutY(yBase-150);
		setSubject.setPrefWidth(200);
		setSubject.setPromptText("Set subject");
		//get subject from server
		setSubject.setOnAction(e->{
			client.sendMessage("request:set_subject\tcontent:"+setSubject.getText().toUpperCase());
			client.sendMessage("request:get_questions\tcontent:");

		});

		Button loadQuestionsFromServer = new Button("LoadQuestions");  loadQuestionsFromServer.setLayoutX(xBase-xBase); loadQuestionsFromServer.setLayoutY(yBase-124);
		loadQuestionsFromServer.setStyle("-fx-pref-width: 100");

		
		Button showChat = new Button("Show chat"); showChat.setLayoutX(xBase+820);showChat.setLayoutY(yBase+50);
		showChat.setOnAction(e->{
			chat.show();
		});
		Button hideChat = new Button("Hide chat"); hideChat.setLayoutX(xBase+820);hideChat.setLayoutY(yBase+80);
		hideChat.setOnAction(e->{
			chat.hide();
		});
		
		
		Button questBut = new Button("Confirm"); questBut.setLayoutX(xBase+300); questBut.setLayoutY(yBase+230);
		questBut.setOnAction(e-> {
			Analyzer analyzer = new Analyzer();
			if (quizStarted == 1){
				
				for (int i = 0; i < schema.getQuestions().size(); i++){
					Question question = schema.getQuestions().get(i);
					String correctAnswer = question.getCorrectAnswer();//
					String answer = schema.getAnswers().get(question); //user input
					String category = question.getCategory();
					
					analyzer.registerAnswer(category, correctAnswer.equals(answer));
					
					
				}
				String appender = "";
				Iterator<String> it = analyzer.getCategories().keySet().iterator();
				while (it.hasNext()){
					String next = it.next().toString();
					appender += next + ": "
							+ ""+ analyzer.getCategories().get(next)[0] + "/" + analyzer.getCategories().get(next)[1] + "\n";
					
				}
				feed.setText("Your results: \n" + appender);
				client.sendMessage("request:add_results\tcontent:"+analyzer.prepareContent());
			}
		});
		
		//
		Button nextQ = new Button("Next"); nextQ.setLayoutX(xBase+520); nextQ.setLayoutY(yBase+230);
		Button prevQ = new Button("Prev"); prevQ.setLayoutX(xBase+435); prevQ.setLayoutY(yBase+230);
		
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
		//
		
		
		final MenuButton m = new MenuButton("Subjects");
		
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
		
		MenuButton pickCategory = new MenuButton(); pickCategory.setLayoutX(xBase+0); pickCategory.setLayoutY(yBase-yBase);pickCategory.setText("Pick category");
		
		
		//load questions to pick category
		Button load = new Button("Load"); load.setLayoutX(xBase-145);load.setLayoutY(yBase-124);
		load.setStyle("-fx-pref-width: 44");
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

		
		Button menu = new Button("Menu"); menu.setLayoutX(xBase+300); menu.setLayoutY(yBase-yBase);
		menu.setOnAction(e->{
			stage.setScene(ctrl.getScene(0));
			
		});
	
		
		root.getChildren().addAll(feed, questBut, nextQ, prevQ, pickCategory, menu, load, setSubject,loadQuestionsFromServer, serverIn, showChat, hideChat, m);
		Scene scene1 = new Scene(root, 1300, 700);
		scene1.getStylesheets().add(getClass().getResource("GUI.css").toExternalForm());

		FeedUpdater updater1 = new FeedUpdater(client, metafeed, client.QuestionWindowQuestions);
		updater1.start();
		FeedUpdater updater2 = new FeedUpdater(client, serverIn, client.QuestionWindowInfo);
		updater2.start();
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
		//TODO: indexoutofboundsexception => rawQuestionArray is empty
		ArrayList<ArrayList<String>> textData = new ArrayList<>();
		String[] rawQuestionArray = content.split("@"); // array of questions
		int i =-1;
		for(String rawQuestion : rawQuestionArray){
			System.out.println(rawQuestion);
			String q = rawQuestion.replace('|', '\n');
			q=q.replace(';', ':');
			
			Scanner scanner = new Scanner(q);
			while (scanner.hasNext()){
				String next = scanner.nextLine();
				System.out.println(""+next+"");
				System.out.println("index"+i);
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
		client.sendMessage("request:get_subjects\tcontent:global");
		System.out.println("metafeed: " + metafeed.getText());
		String servertext = "";
		
	}	
	
	public void sleep(){
		
	}
	

}
