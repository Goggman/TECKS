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

public class CreateQWindow implements Window {
	Stage stage;
	GUIController ctrl;
	
	CreateQWindow(Stage stageIn, GUIController ctrlIn){
		stage=stageIn;
		ctrl=ctrlIn;
		
	}
	public Scene createScene(){
		int xBase=600, yBase=200;
		Pane root = new Pane();
		TextField qText = new TextField(); qText.setLayoutX(xBase+0); qText.setLayoutY(yBase+50);
		TextField aText = new TextField(); aText.setLayoutX(xBase+0); aText.setLayoutY(yBase+100);
		Label feed = new Label("QuestionMaker"); feed.setLayoutX(xBase+0); feed.setLayoutY(yBase+0); feed.setStyle("-fx-border-color: black");
		Button back = new Button("Back"); back.setLayoutX(xBase+0); back.setLayoutY(yBase+200);
		back.setOnAction(e->{
			stage.setScene(ctrl.getScene(0));
		});
		
		root.getChildren().addAll(qText, aText, feed, back);
		Scene scene = new Scene(root, 1300, 700);
		return scene;
	}

}
