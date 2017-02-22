package javaClientCode;

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
	QuestionWindow(Stage stageInput, QuestionSchema schema1, GUIController ctrlIn){
		schema=schema1;
		index=0;
		quizStarted = 0;
		stage=stageInput;
		ctrl=ctrlIn;
		
	}

	public Scene createScene(){
		int xBase=300; int yBase=200;
		Pane root = new Pane(); 
		Label feed = new Label(); feed.setLayoutX(xBase+100); feed.setLayoutY(yBase+50); feed.setStyle("-fx-border-color: black"); feed.setPrefSize(500, 300); feed.setAlignment(Pos.TOP_LEFT);
		TextField userInput = new TextField(); userInput.setPromptText("Type here"); userInput.setLayoutX(xBase+100); userInput.setLayoutY(yBase+400);
		userInput.setOnAction(e -> {
			if (quizStarted == 1){
				
				schema.getAnswers().set(index, userInput.getText());
				feed.setText(""+schema.getQuestions().get(index).getQuestionText()+"\n Answer Given: "+userInput.getText());
				userInput.clear();
			}
		});
		Button questBut = new Button("Confirm Answer"); questBut.setLayoutX(xBase+300); questBut.setLayoutY(yBase+400);
		questBut.setOnAction(e-> {
			Analyzer analyzer = new Analyzer();
			for (int i=0;i<schema.getQuestions().size();i++){
				Question question=schema.getQuestions().get(i);
				String correctAnswer = question.getCorrectAnswer();
				String answer = schema.getAnswers().get(i);
				String category = question.getCategory();
				if (correctAnswer.equals(answer)){
					analyzer.getCategories().put(category, analyzer.getCategories().get(category)+1);
				}
			}
			feed.setText("Your results: \n"+analyzer.getCategories().toString());
		});
		Button nextQ = new Button("Next Question"); nextQ.setLayoutX(xBase+0); nextQ.setLayoutY(yBase+100);
		nextQ.setOnAction(e->{
			if (quizStarted==1){
			if (index < schema.getQuestions().size()-1){
				index++;
			}
			
			feed.setText(schema.getQuestions().get(index).getQuestionText()+"\n Answer given: "+schema.getAnswers().get(index));
			}
		});
		Button prevQ = new Button("Prev Question"); prevQ.setLayoutX(xBase+0); prevQ.setLayoutY(yBase+50);
		prevQ.setOnAction(e->{
			if (quizStarted==1){
			if (index!=0){
				index--;
			}
			feed.setText(schema.getQuestions().get(index).getQuestionText()+"\n Answer given: "+schema.getAnswers().get(index));
			}
		});
		MenuItem item1 = new MenuItem("quiz1"); item1.setOnAction(e->{
			index=0;
			quizStarted=1;
			feed.setText(""+schema.getQuestions().get(index).getQuestionText());
			
		});
		MenuButton pickCategory = new MenuButton("Category"); pickCategory.getItems().add(item1); pickCategory.setLayoutX(xBase+0); pickCategory.setLayoutY(yBase+0);
		
		Button menu = new Button("Menu"); menu.setLayoutX(xBase+0); menu.setLayoutY(yBase+100);
		menu.setOnAction(e->{
			stage.setScene(ctrl.getWindow(0));
			
		});
	
		
		root.getChildren().addAll(userInput,feed, questBut, nextQ, prevQ, pickCategory, menu);
		Scene scene1 = new Scene(root, 1300, 700);
		return scene1;
	}
	
	QuestionSchema getSchema(){
		return schema;
	}

}
