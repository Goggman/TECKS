package javaClientCode;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.geometry.Pos;
import javafx.scene.control.Button;

public class GUI extends Application{
	GUIData data = new GUIData(this);
	Asker asker = new Asker(data);
	Button questBut;
	
	Pane root;
	Label feed;
	TextField userInput;
	Scene scene1;
	//	int xBase1;
	//	int yBase1;
	//GUI(){
	//	xBase1=0;
	//	yBase1=0;
		
	//}
	public void start(Stage stage) throws Exception{
		
		int xBase=0; int yBase=0;
		root = new Pane(); 
		feed = new Label(); feed.setLayoutX(xBase+100); feed.setLayoutY(yBase+50); feed.setStyle("-fx-border-color: black"); feed.setPrefSize(500, 300); feed.setAlignment(Pos.TOP_LEFT);
		userInput = new TextField(); userInput.setPromptText("Type here"); userInput.setLayoutX(xBase+100); userInput.setLayoutY(yBase+400);
		userInput.setOnAction(e -> {
			data.setTextField(userInput.getText());
			data.setTextLabel(data.getTextField());
			userInput.clear();
			});
		questBut = new Button("Start Quiz"); questBut.setLayoutX(xBase+0); questBut.setLayoutY(yBase+50);
		questBut.setOnAction(e-> {
			initialize();
			});
		
		
		root.getChildren().addAll(userInput,feed, questBut);
		scene1 = new Scene(root,500,500);
		stage.setScene(scene1);
		stage.show();
		update();
		
		//Controller controller = new Controller();
		//controller.asker.makeQuiz();
		
		
	}
	
	public void update(){
		feed.setText(data.getTextLabel());
		userInput.setText(data.getTextField());
		
		
	}
	 void initialize(){
		data.addListener(asker);
		asker.askMany(asker.makeQuiz());
		
	}
	
	public static void main(String[] args){
		launch();
		
	}
	

}
