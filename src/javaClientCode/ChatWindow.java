package javaClientCode;
import java.io.IOException;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;

public class ChatWindow implements Window{
	Stage stage;
	GUIController ctrl;
	ServerClient client;
	Label feed;
	ChatWindow(Stage stageInput, ServerClient clientIn){
		stage=stageInput;
		client=clientIn;
	}
	public Scene createScene(){
		int xBase=100; int yBase = 100;
		Pane root = new Pane(); root.setStyle("-fx-background-color: white");
		feed = new Label("Welcome to Chat"); feed.setLayoutX(xBase-50); feed.setLayoutY(yBase-50);
		feed.setPrefSize(100, 100);
		TextField text = new TextField("Type here"); text.setLayoutX(xBase+0); text.setLayoutY(yBase+20);
		//TextField password = new TextField();
		Button menu1 = new Button("Send"); menu1.setLayoutX(xBase+0); menu1.setLayoutY(yBase+40);
		menu1.setOnAction(e->{
			client.sendMessage("request:msg\tcontent:"+text.getText());
		});
		
		Button menu2 = new Button("Clear feed"); menu2.setLayoutX(xBase+20); menu2.setLayoutY(yBase+40);
		menu2.setOnAction(e->{
			feed.setText("Window cleared");
		});
		root.getChildren().addAll(feed, menu1,menu2, text);
		Scene scene = new Scene(root, 300, 300);
		FeedUpdater updater = new FeedUpdater(client, feed);
		updater.start();
		return scene;
	}
	
	
	
	
}