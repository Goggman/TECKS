package javaClientCode;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;



import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;

public class CreateQWindow implements Window {
	Stage stage;
	GUIController ctrl;
	ServerClient client;
	
	CreateQWindow(Stage stageIn, GUIController ctrlIn, ServerClient clientIn){
		stage=stageIn;
		ctrl=ctrlIn;
		client=clientIn;
	}
	
	/**
	 * create a scene with create question forms
	 * @return Scene
	 */
	public Scene createScene(){
		int xBase=600, yBase=200;

		ArrayList<Question> quiz = new ArrayList<>();
		
		Pane root = new Pane();
		TextField head = new TextField(); head.setLayoutX(xBase+0); head.setLayoutY(yBase+0); head.setPromptText("Set header");
		TextField qText = new TextField(); qText.setLayoutX(xBase+0); qText.setLayoutY(yBase+50);qText.setPromptText("Question");
		TextField aText = new TextField(); aText.setLayoutX(xBase+0); aText.setLayoutY(yBase+100);aText.setPromptText("Answer");

		root.setStyle("-fx-background-color: white");
		
		Label serverFeed = new Label("Info from server"); serverFeed.setLayoutX(xBase-600); serverFeed.setLayoutY(yBase+0); serverFeed.setStyle("-fx-border-color: black");
		serverFeed.setPrefSize(300, 500); serverFeed.setAlignment(Pos.TOP_LEFT);
		TextField setSubject = new TextField(); setSubject.setPromptText("enter subject name, enter to set"); setSubject.setLayoutX(xBase);setSubject.setLayoutY(yBase-200);
		setSubject.setPrefSize(100, 30);
		setSubject.setOnAction(e->{
			client.sendMessage("request:set_subject\tcontent:"+setSubject.getText());
		});
		
		Label feed = new Label("QuestionMaker"); feed.setLayoutX(xBase-300); feed.setLayoutY(yBase+0); feed.setStyle("-fx-border-color: black");
		TextField fileName = new TextField(); fileName.setLayoutX(xBase+200); fileName.setLayoutY(yBase+100); fileName.setPromptText("filename");
		
		TextField op1 = new TextField(); op1.setLayoutX(xBase);op1.setLayoutY(yBase+130);op1.setPromptText("Option 1");op1.setVisible(false);
		
		TextField op2 = new TextField(); op2.setLayoutX(xBase);op2.setLayoutY(yBase+160);op2.setPromptText("Option 2");op2.setVisible(false);
		
		Label error = new Label("Needs filename");error.setVisible(false);
		Button save = new Button("Save quiz");save.setLayoutX(xBase+200);save.setLayoutY(yBase+150);
		//save.setDisable(true);
		
		MenuItem mulChoice = new MenuItem(); mulChoice.setText("Multiple choice");
		MenuItem fillIn = new MenuItem(); fillIn.setText("Fill in the blank");
		MenuButton qType = new MenuButton("Question type", null, mulChoice, fillIn); qType.setLayoutX(xBase+250);qType.setLayoutY(yBase);
		//choose question type
		mulChoice.setOnAction(e -> {
			op1.setVisible(true);
			op2.setVisible(true);
			qType.setText("Multiple choice");
		});
		fillIn.setOnAction(e -> {
			op1.setVisible(false);
			op2.setVisible(false);
			qType.setText("Fill in");
		});
		//set category
		TextField category = new TextField(); category.setLayoutX(xBase+200);category.setLayoutY(yBase+50);category.setPromptText("Category");
		
		
		feed.setPrefSize(300, 300); feed.setAlignment(Pos.TOP_LEFT);
		Button createQ = new Button("Create"); createQ.setLayoutX(xBase+0); createQ.setLayoutY(yBase+200);
		
		//create new question
		createQ.setOnAction(e->{
			String questionText = qText.getText();
			String answerText = aText.getText();
			String headerText = head.getText();
			String categoryText = category.getText();
			/*if (headerText.isEmpty() || questionText.isEmpty() || answerText.isEmpty() || categoryText.isEmpty()){
				feed.setText("Need values for Header, Question and answer");
			}else {
				save.setDisable(false);
			}*/
			qText.setText(""); aText.setText("");head.setText("");
			
			
			feed.setText(""+headerText+"\n"+questionText+"\n"+answerText+"\n");
			if (qType.getText().equals("Multiple choice")){
				quiz.add(new Question(categoryText, questionText, answerText, headerText, op1.getText(), op2.getText()));
				
			} else {
				quiz.add(new Question(categoryText, questionText, answerText, headerText));
			}
		});
		//go back to main menu
		Button back = new Button("Back"); back.setLayoutX(xBase+0); back.setLayoutY(yBase+250);
		back.setOnAction(e->{
			stage.setScene(ctrl.getScene(0));
		});
		
		
		//save to file
		save.setOnAction(e -> {
			/*
			if (fileName.getText().equals("")){
				error.setLayoutX(fileName.getLayoutX()+5);error.setLayoutY(fileName.getLayoutY()+30);
				error.setTextFill(Paint.valueOf("red"));error.setVisible(true);
				
			}
			else{ */
				error.setVisible(false);
				createQuestionToServer(quiz);
				/*
				try {
					saveFile(fileName.getText(), quiz);
					
				}
				catch (IOException ex){
					ex.printStackTrace();
			}
				
				}
				*/
		});
		
		
		root.getChildren().addAll(fileName, qType, qText, aText, feed, back, head, createQ, op1, op2, save, error, category, serverFeed, setSubject);
		Scene scene = new Scene(root, 1300, 700);
		FeedUpdater updater = new FeedUpdater(client, serverFeed, client.CreateQWindow);
		updater.start();
		return scene;
	}
	
	
	
	
	
	
	
	/**
	 * Load question object to file
	 * @param filename 
	 * @param q Question object to be saved
	 * @throws IOException
	 */
	/*
	public void saveFile(String filename, ArrayList<Question> q) throws IOException{
		
		String path = System.getProperty("user.dir");
		PrintWriter p = new PrintWriter(path + "/" +filename);
		
		for (int j = 0; j < q.size(); j++){
			p.println("Header: " + q.get(j).getHeader());
			p.println("c: "+q.get(j).getCategory());
			p.println("q: " + q.get(j).getQuestionText());
			for (int i = 0; i < q.get(j).getOptions().size(); i++){
				p.println("op: " + q.get(j).getOptions().get(i));
			}
			p.println("a: " + q.get(j).getCorrectAnswer());
		}
		p.close();
	}
	*/
	public void createQuestionToServer(ArrayList<Question> q){
		//String quiz = "";
		for (Question question : q){
			String questionToServer ="request:add_question\tcontent:"+"Header;"+question.getHeader()+"|"
																	+"c; "+question.getCategory()+"|"
																	+"q; "+question.getQuestionText()+"|";
			
			for (String option : question.getOptions()){
				questionToServer+="op; "+option+"|";
			}
			questionToServer+="a; "+question.getCorrectAnswer();
			
			client.sendMessage(questionToServer);
		
	}
		

}
}
