package javaClientCode;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.filechooser.FileFilter;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;

public class LoadQWindow implements Window {
	Stage stage;
	GUIController ctrl;
	ArrayList<ArrayList<String>> quizHolder = new ArrayList<>();
	private QuestionWindow qw;
	String currentPath = System.getProperty("user.dir");
	
	
	public ArrayList<ArrayList<String>> getQuizHolder(){
		System.out.println(quizHolder.toString());
		return quizHolder;
	}
	/**
	 * get stage and controller
	 * @param stageIn
	 * @param ctrlIn
	 */
	LoadQWindow(Stage stageIn, GUIController ctrlIn, QuestionWindow qw){
		this.qw = qw;
		stage=stageIn;
		ctrl=ctrlIn;
		
	}
	/**
	 * method for creating Load Question scene
	 * @return
	 */
	public Scene createScene(){
		int xBase=600, yBase=200;
		Pane root = new Pane();
		Label feed = new Label("QuestionLoader"); feed.setLayoutX(xBase-600); feed.setLayoutY(yBase+0); feed.setStyle("-fx-border-color: black");
		feed.setPrefSize(600, 300); feed.setText(""+System.getProperty("user.dir"));
		
		File[] listOfFiles = new File(currentPath).listFiles((dir, name) -> {
			if (! name.endsWith("txt")){
				return false;
			} return true;
		});
		ArrayList<String> files = new ArrayList<>();
		int xBox = xBase, yBox = 200; //x- and y-base for checkboxes
		for (File f : listOfFiles){
			CheckBox temp = new CheckBox(f.getName());
			temp.setLayoutX(xBox);temp.setLayoutY(yBox);
			temp.setOnAction(e -> {
				
				if (temp.isSelected()){
					files.add(temp.getText());
				}
				else {
					files.remove(temp.getText());
				}
				
			});
			yBox += 20;
			root.getChildren().add(temp);
		}
		
		
		
		TextField path = new TextField(); path.setLayoutX(xBase+0); path.setLayoutY(yBase+100); path.setPromptText("Enter path directory");
		path.setPrefWidth(600);
		path.setOnAction(e->{
			try{
				
				quizHolder = OpenFile(path.getText());
				qw.addSchema(new QuestionSchema(quizHolder));
				feed.setText(OpenFile(path.getText()).toString());
				
			}
			catch (IOException ex){
				feed.setText("Error in loading question");
				ex.printStackTrace();
				path.clear();
				
			}
			
		});
		
		//TextField aText = new TextField(); aText.setLayoutX(xBase+0); aText.setLayoutY(yBase+100);
		Button load = new Button("Load");load.setLayoutX(xBase);load.setLayoutY(yBase+170);
		load.setOnAction(e -> {
			for (String filePath : files){
				try {
					quizHolder = OpenFile(filePath);
					qw.addSchema(new QuestionSchema(quizHolder));
					feed.setText(quizHolder.toString());
				} 
				catch (IOException ex){
					feed.setText("could not find file " + filePath);
					ex.printStackTrace();
				}
			}
		});
		Button back = new Button("Back"); back.setLayoutX(xBase+0); back.setLayoutY(yBase+200);
		back.setOnAction(e->{
			stage.setScene(ctrl.getScene(0));
		});
		
		root.getChildren().addAll(path, feed, back, load);
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
			textData.get(i).add(next);
			
		}
	
		
		textReader.close();
		return textData;
		
	}

}