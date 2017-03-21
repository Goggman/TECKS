package javaClientCode;
import java.io.IOException;

import javafx.application.Platform;
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
		int xBase=200; int yBase = 200;
		Pane root = new Pane(); root.setStyle("-fx-background-color: white");
		feed = new Label("Welcome to Chat"); feed.setLayoutX(xBase-195); feed.setLayoutY(yBase-200); feed.setAlignment(Pos.TOP_LEFT);
		feed.setPrefSize(200, 400);
		TextField text = new TextField("Type here"); text.setLayoutX(xBase+0); text.setLayoutY(yBase+20);
		//TextField password = new TextField();
		
		Button menu1 = new Button("Send"); menu1.setLayoutX(xBase+0); menu1.setLayoutY(yBase+40);
		menu1.setOnAction(e->{
			feed.setText(feed.getText()+"\n"+text.getText());
			client.sendMessage("request:msg\tcontent:"+text.getText());
		});
		
		Button menu2 = new Button("Clear feed"); menu2.setLayoutX(xBase+40); menu2.setLayoutY(yBase+40);
		menu2.setOnAction(e->{
			feed.setText("Window cleared");
		});
		Button menu3 = new Button("History"); menu3.setLayoutX(xBase+40); menu3.setLayoutY(yBase+70);
		menu3.setOnAction(e->{
			client.sendMessage("request:history\tcontent:");
		});
		
		Button menu4 = new Button("Show more"); menu4.setLayoutX(xBase+40); menu4.setLayoutY(yBase+120);
		menu4.setOnAction(e->{
			String feedtext = feed.getText();
			if (feedtext.length()>100){
				int counter = 0;
				for(int x=0;x<feedtext.length();x++){
					if(feedtext.charAt(x)=='\n'){
						counter+=1;
						if (counter==3){
							feedtext=feedtext.substring(x);
							feed.setText(feedtext);
							break;
						}
					}
				}
				}
		});
		
		root.getChildren().addAll(feed, menu1, menu2, menu3, menu4, text);
		Scene scene = new Scene(root, 400, 400);
		FeedUpdater updater = new FeedUpdater(client, feed, client.messageIn);
		updater.start();
		return scene;
	}
	
	
	
	
}