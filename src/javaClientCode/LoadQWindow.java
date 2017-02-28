package javaClientCode;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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

public class LoadQWindow implements Window {
	Stage stage;
	GUIController ctrl;
	/**
	 * get stage and controller
	 * @param stageIn
	 * @param ctrlIn
	 */
	LoadQWindow(Stage stageIn, GUIController ctrlIn){
		stage=stageIn;
		ctrl=ctrlIn;
		
	}
	/**
	 * method for creating Load Question scene
	 * @return
	 */
	public Scene createScene(){
		int xBase=600, yBase=200;
		Pane root = new Pane(); root.setStyle("-fx-background-color: white");
		Label feed = new Label("QuestionLoader"); feed.setLayoutX(xBase-600); feed.setLayoutY(yBase+0); feed.setStyle("-fx-border-color: black");
		feed.setPrefSize(600, 300); feed.setText(""+System.getProperty("user.dir"));
		TextField path = new TextField(); path.setLayoutX(xBase+0); path.setLayoutY(yBase+50); path.setPromptText("Enter path directory");
		path.setPrefWidth(600);
		path.setOnAction(e->{
			try{
				feed.setText(OpenFile(path.getText()).toString());
				
			}
			catch (IOException ex){
				feed.setText("Error in loading question");
				ex.printStackTrace();
				path.clear();
				
			}
			
		});
		//TextField aText = new TextField(); aText.setLayoutX(xBase+0); aText.setLayoutY(yBase+100);
		
		Button back = new Button("Back"); back.setLayoutX(xBase+0); back.setLayoutY(yBase+200);
		back.setOnAction(e->{
			stage.setScene(ctrl.getScene(0));
		});
		
		root.getChildren().addAll(path, feed, back);
		Scene scene = new Scene(root, 1300, 700);
		return scene;
	}
	
	
	/**
	 * read from file and create a new question object
	 * @param path
	 * @return Arraylist matrix with strings
	 * @throws IOException
	 */
	public ArrayList<ArrayList<String>> OpenFile(String path) throws IOException{ //Returnerer en liste av spørsmål i standard format
		FileReader fr = new FileReader(path);
		BufferedReader textReader = new BufferedReader(fr);
		
		
		ArrayList<ArrayList<String>> textData = new ArrayList<>();
		int i = -1;
		 
		while (textReader.ready()){
			String next = textReader.readLine();
			if (next.contains("Header:")){
				textData.add(new ArrayList<String>());
				i++;
			}
			//System.out.println(next);
			textData.get(i).add(next);
			
		}
		
		textReader.close();
		return textData;
		
	}

}