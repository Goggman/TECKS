package javaClientCode;

import java.util.ArrayList;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;

public class QuestionWindow implements Window {
	QuestionSchema schema;
	int index;
	int quizStarted;
	Stage stage;
	GUIController ctrl;
	ArrayList<QuestionSchema> schemas = new ArrayList<>();
	ArrayList<String> answers = new ArrayList<>(); //input answers from user
	
	public void addSchema(QuestionSchema qs ){
		
		schemas.add(qs);
		schema = qs;
	}
	
	QuestionWindow(Stage stageInput, GUIController ctrlIn){
		//System.out.println(lqw.getQuizHolder().toString());
		
		//schema=new QuestionSchema(lqw.getQuizHolder());
		index=0;
		quizStarted = 0;
		stage=stageInput;
		ctrl=ctrlIn;
		
	}

	/**
	 * Method for creating a scene in gui controller
	 * @return scene1 Scene objekt
	 */
	public Scene createScene(){
		int xBase=300, yBase=200;
		Pane root = new Pane(); 
		Label feed = new Label(); feed.setLayoutX(xBase+100); feed.setLayoutY(yBase+50); feed.setStyle("-fx-border-color: black"); feed.setPrefSize(500, 300); feed.setAlignment(Pos.TOP_LEFT);
		TextField userInput = new TextField(); userInput.setPromptText("Type here"); userInput.setLayoutX(xBase+100); userInput.setLayoutY(yBase+400);
		userInput.setOnAction(e -> {
			if (quizStarted == 1){
				
				schema.getAnswers().put(schema.getQuestions().get(index), userInput.getText());
				feed.setText(""+schema.getQuestions().get(index).getQuestionText()+"\n Answer Given: "+userInput.getText());
				userInput.clear();
			}
		});
		
		//
		Button questBut = new Button("Confirm Answer"); questBut.setLayoutX(xBase+300); questBut.setLayoutY(yBase+400);
		questBut.setOnAction(e-> {
			Analyzer analyzer = new Analyzer();
			if (quizStarted == 1){
				
				for (int i = 0; i < schema.getQuestions().size(); i++){
					Question question = schema.getQuestions().get(i);
					String correctAnswer = question.getCorrectAnswer();//
					String answer = schema.getAnswers().get(question); //user input
					String category = question.getCategory();
					System.out.println(answer + " | " + correctAnswer);
					analyzer.getCategories().putIfAbsent(category, 0);
					if (correctAnswer.equals(answer)){
						try {
						analyzer.getCategories().put(category, analyzer.getCategories().put(category, 0)+1);
						} catch (NullPointerException ex){
							analyzer.getCategories().put(category, 1);
						}
					}
					else {System.out.println("not correct");}
					
				}
				feed.setText("Your results: \n"+analyzer.getCategories().toString());
			}
		});
		
		//
		Button nextQ = new Button("Next Question"); nextQ.setLayoutX(xBase+0); nextQ.setLayoutY(yBase+100);
		nextQ.setOnAction(e->{
			
			if (quizStarted==1){
				if (index < schema.getQuestions().size()-1){
					index++;
				}
			Question q = schema.getQuestions().get(index);
			feed.setText(q.getQuestionText()+"\n Answer given: "+schema.getAnswers().get(q));
			}
		});
		//
		Button prevQ = new Button("Prev Question"); prevQ.setLayoutX(xBase+0); prevQ.setLayoutY(yBase+50);
		prevQ.setOnAction(e->{
			if (quizStarted==1){
			if (index!=0){
				index--;
			}
			Question q = schema.getQuestions().get(index);
			feed.setText(q.getQuestionText()+"\n Answer given: "+schema.getAnswers().get(q));
			}
		});
		//TODO: fix triple click
		
		MenuButton pickCategory = new MenuButton(); pickCategory.setLayoutX(xBase+0); pickCategory.setLayoutY(yBase+0);pickCategory.setText("Pick category");
		/*pickCategory.setOnMouseClicked(e -> {
			System.out.println("size: " + schemas.size());
			System.out.println("categories: " + pickCategory.getItems().size());
			if (pickCategory.getItems().size() < schemas.size()){
				for (int i = 0; i < schemas.size(); i++){
					System.out.println("i: " + i);
					MenuItem temp = new MenuItem("quiz" + (i + 1));
					pickCategory.getItems().add(temp); 
					temp.setOnAction(ev -> {
						index = 0;
						
						try {
							schema = schemas.get(0);
							feed.setText(""+schema.getQuestions().get(index).getQuestionText());
							quizStarted=1;
							System.out.println("item successfully added");
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
		});*/
		Button load = new Button("Load"); load.setLayoutX(xBase+300);load.setLayoutY(yBase);
		load.setOnAction(e -> {
			if (pickCategory.getItems().size() < schemas.size()){
				for (int i = 0; i < schemas.size(); i++){
					System.out.println("i: " + i);
					MenuItem temp = new MenuItem("quiz" + (i + 1));
					pickCategory.getItems().add(temp); 
					temp.setOnAction(ev -> {
						index = 0;
						
						try {
							schema = schemas.get(0);
							feed.setText(""+schema.getQuestions().get(index).getQuestionText());
							quizStarted=1;
							System.out.println("item successfully added");
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
		});
//		MenuItem item1 = new MenuItem("quiz1"); item1.setOnAction(e->{
//			index=0;
//			try {
//				feed.setText(""+schema.getQuestions().get(index).getQuestionText());
//				quizStarted=1;
//			}
//			catch (IndexOutOfBoundsException ex){
//				feed.setText("no quiz selected");
//			}
//			
//		});
		
		Button menu = new Button("Menu"); menu.setLayoutX(xBase+0); menu.setLayoutY(yBase+150);
		menu.setOnAction(e->{
			stage.setScene(ctrl.getScene(0));
			
		});
	
		
		root.getChildren().addAll(userInput,feed, questBut, nextQ, prevQ, pickCategory, menu, load);
		Scene scene1 = new Scene(root, 1300, 700);
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
	
	
	

}
