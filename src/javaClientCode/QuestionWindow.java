package javaClientCode;

import java.util.ArrayList;
import java.util.Scanner;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
	ArrayList<QuestionSchema> schemas = new ArrayList<>();
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

	/**
	 * Method for creating a scene in gui controller
	 * @return scene1 Scene objekt
	 */
	public Scene createScene(){
		int xBase=300, yBase=200;
		Pane root = new Pane(); root.setStyle("-fx-background-color: white");
		Label feed = new Label(); feed.setLayoutX(xBase+100); feed.setLayoutY(yBase-90); feed.setStyle("-fx-border-color: black"); feed.setPrefSize(500, 300); feed.setAlignment(Pos.TOP_LEFT);
		TextField userInput = new TextField(); userInput.setPromptText("Type here"); userInput.setLayoutX(xBase+100); userInput.setLayoutY(yBase+230);
		root.getChildren().add(userInput);
		userInput.setOnAction(e -> {
			if (quizStarted == 1){
				
				schema.getAnswers().put(schema.getQuestions().get(index), userInput.getText());
				feed.setText(""+schema.getQuestions().get(index).getQuestionText()+"\n Answer Given: "+userInput.getText());
				userInput.clear();
			}
			
		});
		//TODO: different question types
		
		
		//
		Label serverIn = new Label("InfoMessagesFromServer");serverIn.setLayoutX(xBase-300); serverIn.setLayoutY(yBase-90); serverIn.setStyle("-fx-border-color: black"); serverIn.setPrefSize(200, 400); serverIn.setAlignment(Pos.TOP_LEFT);
		Label metafeed = new Label("");
		TextField setSubject = new TextField();  setSubject.setLayoutX(xBase-300); setSubject.setLayoutY(yBase-150);
		setSubject.setPrefWidth(200);
		setSubject.setPromptText("Set subject");
		setSubject.setOnAction(e->{
			client.sendMessage("request:set_subject\tcontent:"+setSubject.getText());
			client.sendMessage("request:get_questions\tcontent:");

		});

		Button loadQuestionsFromServer = new Button("LoadQuestions");  loadQuestionsFromServer.setLayoutX(xBase-xBase); loadQuestionsFromServer.setLayoutY(yBase-124);
		loadQuestionsFromServer.setStyle("-fx-pref-width: 100");

		loadQuestionsFromServer.setOnAction(e->{
			
			if (!metafeed.getText().equals("")){
				Scanner scanner = new Scanner(metafeed.getText());
				scanner.nextLine();scanner.nextLine();//Skips first lines, as it contains sender
				String content = scanner.nextLine();
				addSchema(readToSchema(content));
				scanner.close();
				metafeed.setText("");
				serverIn.setText("Load successfull");
				
			}
			else{
				serverIn.setText("Load unsuccessfull");
			}
			
		});
		
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
					
					analyzer.getCategories().putIfAbsent(category, 0);
					

					if (correctAnswer.equals(answer)){
						analyzer.getCategories().put(category, analyzer.getCategories().get(category)+1);
						
					}
					
					
				}
				feed.setText("Your results: \n"+analyzer.getCategories().toString());
			}
		});
		
		//
		Button nextQ = new Button("Next"); nextQ.setLayoutX(xBase+520); nextQ.setLayoutY(yBase+230);
		nextQ.setOnAction(e->{
			
			if (quizStarted==1){
				if (index < schema.getQuestions().size()-1){
					index++;
				}
			Question q = schema.getQuestions().get(index);
			feed.setText(q.getQuestionText()+"\n Answer given: "+schema.getAnswers().get(q));
			}
		});
		Button prevQ = new Button("Prev"); prevQ.setLayoutX(xBase+435); prevQ.setLayoutY(yBase+230);
		prevQ.setOnAction(e->{
			if (quizStarted==1){
			if (index!=0){
				index--;
			}
			Question q = schema.getQuestions().get(index);
			feed.setText(q.getQuestionText()+"\n Answer given: "+schema.getAnswers().get(q));
			}
		});
		//
		
		MenuButton pickCategory = new MenuButton(); pickCategory.setLayoutX(xBase+0); pickCategory.setLayoutY(yBase-yBase);pickCategory.setText("Pick category");
		
		Button load = new Button("Load"); load.setLayoutX(xBase-145);load.setLayoutY(yBase-124);
		load.setStyle("-fx-pref-width: 44");
		load.setOnAction(e -> {
			
			if (pickCategory.getItems().size() < schemas.size()){
				for (int i = 0; i < schemas.size(); i++){
					
					MenuItem temp = new MenuItem(""+(i + 1));
					pickCategory.getItems().add(temp); 
					temp.setOnAction(ev -> {
						index = 0;
						
						try {
							schema = schemas.get(Integer.parseInt(temp.getText())-1);
							feed.setText(""+schema.getQuestions().get(index).getQuestionText());
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
				
			
			
			
			
			
				String qType = schema.getQuestions().get(index).getHeader();
				
				int xRadio = xBase + 50, yRadio = 400;
				//if (qType.substring(0, qType.indexOf(",")).equals("fill-in")){
				//}
				/*else if (qType.substring(0, qType.indexOf(",")).equals("multiple-choice")){
					if (quizStarted == 1){
						for (String op : schema.getQuestions().get(index).getOptions()){
							RadioButton temp = new RadioButton(op);temp.setLayoutX(xRadio);temp.setLayoutY(yRadio);
							temp.setOnAction(ev -> {
								if (temp.isSelected()){
									schema.getAnswers().put(schema.getQuestions().get(index), temp.getText());
								}
							});
							
							
							
							root.getChildren().add(temp);
						}
					}
				}*/
				}
			else if (schemas.size() < 1){
				feed.setText("No schema found \n try loading one from file");
			}
		});

		
		Button menu = new Button("Menu"); menu.setLayoutX(xBase+300); menu.setLayoutY(yBase-yBase);
		menu.setOnAction(e->{
			stage.setScene(ctrl.getScene(0));
			
		});
	
		
		root.getChildren().addAll(feed, questBut, nextQ, prevQ, pickCategory, menu, load, setSubject,loadQuestionsFromServer, serverIn, showChat, hideChat);
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
	
	public QuestionSchema readToSchema(String content){ //Returnerer en liste av spørsmål i standard format, fra server til gjennomførbar quiz
		//Ment for bruk av Question WIndow når skal sjekke hvilke quizer som er tilgjengelige
		
		//; instead of :, | instead of \n, @ for start of new question
		//Header;header1|c;category1|op;op1|op;op2|a;answer1
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
				System.out.println(""+i);
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
	
	
	

}
