package javaClientCode;
import java.io.IOException;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;


public class ChatWindow implements Window{
	Stage stage;
	GUIController ctrl;
	ServerClient client;
	TextArea serverIn;
	FeedUpdater updater1;
	FeedUpdater updater2;
	public TextArea messageFeed;
	public TextField text;
	
	public ChatWindow(Stage stageInput, ServerClient clientIn){
		stage=stageInput;
		client=clientIn;
	}
	
	public Scene createScene(){
		int xBase=200; int yBase = 200;
		Pane root = new Pane(); root.setStyle("-fx-background-color: white");
		messageFeed = new TextArea("Welcome to Chat"); messageFeed.setLayoutX(xBase-195); messageFeed.setLayoutY(yBase-200); //messageFeed.setAlignment(Pos.TOP_LEFT);
		messageFeed.setPrefSize(200, 350); messageFeed.setStyle("-fx-border-color:black");messageFeed.setEditable(false);
		text = new TextField(); text.setLayoutX(xBase-195); text.setLayoutY(yBase+150);
		text.setStyle("-fx-pref-width: 200");
		text.setPromptText("TypeHere");
		//TODO: test messageFeed
		text.setOnAction(e->{
			messageFeed.setText(messageFeed.getText()+"\n"+text.getText());
			client.sendMessage("request:msg\tcontent:"+text.getText());
		});
		
		Button menu1 = new Button("Create room"); menu1.setLayoutX(xBase+40); menu1.setLayoutY(yBase+40);
		menu1.setStyle("-fx-pref-width: 120");
		menu1.setOnAction(e->{
			client.sendMessage("request:create_chatroom\tcontent:"+text.getText());
		});
		Button menu6 = new Button("Login"); menu6.setLayoutX(xBase+40); menu6.setLayoutY(yBase+100);
		menu6.setStyle("-fx-pref-width: 120");
		menu6.setOnAction(e->{
			client.sendMessage("request:login\tcontent:"+text.getText());
		});

		Button menu7 = new Button("Choose room"); menu7.setLayoutX(xBase+40); menu7.setLayoutY(yBase+70);
		menu7.setStyle("-fx-pref-width: 120");
		menu7.setOnAction(e->{
			client.sendMessage("request:set_chatroom\tcontent:"+text.getText());
		});
		
		
		Button menu3 = new Button("History"); menu3.setLayoutX(xBase-195); menu3.setLayoutY(yBase+180);
		menu3.setStyle("-fx-pref-width: 97");
		menu3.setOnAction(e->{
			client.sendMessage("request:history\tcontent:");
		});
		
		Button menu5 = new Button("Logout"); menu5.setLayoutX(xBase+40);menu5.setLayoutY(yBase+130);
		menu5.setStyle("-fx-pref-width: 120");
		menu5.setOnAction(e->{
			client.sendMessage("request:logout\tcontent:");
		});

		//TODO: test
		Button menu2 = new Button("Clear mess"); menu2.setLayoutX(xBase-94); menu2.setLayoutY(yBase+180);
		menu2.setStyle("-fx-pref-width: 97");
		menu2.setOnAction(e->{
			messageFeed.setText("Window cleared");
			serverIn.setText("Window cleared");
		});
		

		
		serverIn = new TextArea("info messages from server");  serverIn.setLayoutX(xBase+5); serverIn.setLayoutY(yBase-200); //serverIn.setAlignment(Pos.TOP_LEFT);
		serverIn.setPrefSize(200, 230); serverIn.setStyle("-fx-border-color:black");serverIn.setEditable(false);
		serverIn.setWrapText(true);

		
		
		root.getChildren().addAll(messageFeed, menu1, menu2, menu3,/* menu5, menu6*/ menu7, text, serverIn);
		Scene scene = new Scene(root, 410, 420);
		scene.getStylesheets().add(getClass().getResource("GUI.css").toExternalForm());
		updater1 = new FeedUpdater(client, messageFeed, client.ChatWindow);

		updater1.start();
		updater2 = new FeedUpdater(client, serverIn, client.ChatWindowInfo);
		updater2.start();
		return scene;
	}
	
	
	public void wakeUp(){
		serverIn.setText("Logged into ");
		messageFeed.setText("Received and sent messages will display here");
		client.sendMessage("request:get_chatroom\tcontent:");
	}
	public void sleep(){
		
	}
}