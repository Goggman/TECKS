package javaClientCode;

import java.io.IOException;
import java.io.PrintWriter;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;

public class CreateQWindow implements Window {
	Stage stage;
	GUIController ctrl;
	
	CreateQWindow(Stage stageIn, GUIController ctrlIn){
		stage=stageIn;
		ctrl=ctrlIn;
		
	}
	
	/**
	 * create a scene with create question forms
	 * @return Scene
	 */
	public Scene createScene(){
		int xBase=600, yBase=200;
		Pane root = new Pane(); root.setStyle("-fx-background-color: white");
		TextField head = new TextField(); head.setLayoutX(xBase+0); head.setLayoutY(yBase+0);
		TextField qText = new TextField(); qText.setLayoutX(xBase+0); qText.setLayoutY(yBase+50);
		TextField aText = new TextField(); aText.setLayoutX(xBase+0); aText.setLayoutY(yBase+100);
		Label feed = new Label("QuestionMaker"); feed.setLayoutX(xBase-400); feed.setLayoutY(yBase+0); feed.setStyle("-fx-border-color: black");
		
		feed.setPrefSize(300, 300); feed.setAlignment(Pos.TOP_LEFT);
		Button createQ = new Button("Create"); createQ.setLayoutX(xBase+0); createQ.setLayoutY(yBase+200);
		createQ.setOnAction(e->{
			String questionText = qText.getText();
			String answerText = aText.getText();
			String headerText = head.getText();
			
			feed.setText(""+headerText+"\n"+questionText+"\n"+answerText+"\n");
			Question newQuestion = new Question("categoryInput", questionText, answerText, headerText);
		});
		Button back = new Button("Back"); back.setLayoutX(xBase+0); back.setLayoutY(yBase+250);
		back.setOnAction(e->{
			stage.setScene(ctrl.getScene(0));
		});
		
		
		
		root.getChildren().addAll(qText, aText, feed, back, head, createQ);
		Scene scene = new Scene(root, 1300, 700);
		return scene;
	}
	
	
	
	
	
	
	
	/**
	 * Load question object to file
	 * @param filename 
	 * @param q Question object to be saved
	 * @throws IOException
	 */
	public void saveFile(String filename, Question q) throws IOException{
		
		PrintWriter p = new PrintWriter(filename);
		
		p.println("Header: " + q.getHeader());
		p.println("c: "+q.getCategory());
		p.println("q: " + q.getQuestionText());
		for (int i = 0; i < q.getOptions().size(); i++){
			p.println(q.getOptions().get(i));
		}
		p.println("a: " + q.getCorrectAnswer());
		p.close();
	}

}
