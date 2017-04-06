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
	
	ChatWindow(Stage stageInput, ServerClient clientIn){
		stage=stageInput;
		client=clientIn;
	}
	public Scene createScene(){
		int xBase=200; int yBase = 200;
		Pane root = new Pane(); root.setStyle("-fx-background-color: white");
		Label feed = new Label("Welcome to Chat"); feed.setLayoutX(xBase-195); feed.setLayoutY(yBase-200); feed.setAlignment(Pos.TOP_LEFT);
		feed.setPrefSize(200, 350); feed.setStyle("-fx-border-color:black");
		TextField text = new TextField(); text.setLayoutX(xBase-195); text.setLayoutY(yBase+150);
		text.setStyle("-fx-pref-width: 200");
		text.setPromptText("TypeHere");
		text.setOnAction(e->{
			feed.setText(feed.getText()+"\n"+text.getText());
			client.sendMessage("request:msg\tcontent:"+text.getText());
		});
		//TextField password = new TextField();
		
		Button menu1 = new Button("Create room"); menu1.setLayoutX(xBase+40); menu1.setLayoutY(yBase+40);
		menu1.setStyle("-fx-pref-width: 120");
		menu1.setOnAction(e->{
			//feed.setText(feed.getText()+"\n"+text.getText());
			client.sendMessage("request:add_chatroom\tcontent:"+text.getText());
		});
		Button menu6 = new Button("Login to room"); menu6.setLayoutX(xBase+40); menu6.setLayoutY(yBase+100);
		menu6.setStyle("-fx-pref-width: 120");
		menu6.setOnAction(e->{
			//feed.setText(feed.getText()+"\n"+text.getText());
			client.sendMessage("request:login\tcontent:"+text.getText());
		});
		Button menu7 = new Button("Choose room"); menu7.setLayoutX(xBase+40); menu7.setLayoutY(yBase+70);
		menu7.setStyle("-fx-pref-width: 120");
		menu7.setOnAction(e->{
			//feed.setText(feed.getText()+"\n"+text.getText());
			client.sendMessage("request:set_chatroom\tcontent:"+text.getText());
		});
		
		
		Button menu3 = new Button("History"); menu3.setLayoutX(xBase-195); menu3.setLayoutY(yBase+180);
		menu3.setStyle("-fx-pref-width: 97");
		menu3.setOnAction(e->{
			client.sendMessage("request:history\tcontent:");
		});
		
		
		Button menu5 = new Button("Logout of room"); menu5.setLayoutX(xBase+40);menu5.setLayoutY(yBase+130);
		menu5.setStyle("-fx-pref-width: 120");
		menu5.setOnAction(e->{
			client.sendMessage("request:logout\tcontent:");
		});
		
		Label serverIn = new Label("info messages from server");  serverIn.setLayoutX(xBase+5); serverIn.setLayoutY(yBase-200); serverIn.setAlignment(Pos.TOP_LEFT);
		serverIn.setPrefSize(200, 230); serverIn.setStyle("-fx-border-color:black");
		
		Button menu2 = new Button("Clear feed"); menu2.setLayoutX(xBase-93); menu2.setLayoutY(yBase+180);
		menu2.setStyle("-fx-pref-width: 98");
		menu2.setOnAction(e->{
			feed.setText("Window cleared");
			serverIn.setText("Window cleared");
		});
		
		/*
		Button menu4 = new Button("Show more"); menu4.setLayoutX(xBase+40); menu4.setLayoutY(yBase+100);
		menu4.setStyle("-fx-pref-width: 100");
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
		*/

		
		
		root.getChildren().addAll(feed, menu1, menu2, menu3,/* menu4*/ menu5, menu6, menu7, text, serverIn);
		Scene scene = new Scene(root, 410, 420);
		scene.getStylesheets().add(getClass().getResource("GUI.css").toExternalForm());
		FeedUpdater updater = new FeedUpdater(client, feed, client.ChatWindow);
		updater.start();
		FeedUpdater updater1 = new FeedUpdater(client, serverIn, client.ChatWindowInfo);
		updater1.start();
		return scene;
	}
	
	
	public void wakeUp(){
		
	}
	public void sleep(){
		
	}
}