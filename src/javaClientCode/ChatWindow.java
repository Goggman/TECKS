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
	TextArea messageFeed;
	ChatWindow(Stage stageInput, ServerClient clientIn){
		stage=stageInput;
		client=clientIn;
	}
	public Scene createScene(){
		int xBase=200; int yBase = 200;
		Pane root = new Pane(); root.setStyle("-fx-background-color: white");
		messageFeed = new TextArea("Welcome to Chat"); messageFeed.setLayoutX(xBase-195); messageFeed.setLayoutY(yBase-200); //messageFeed.setAlignment(Pos.TOP_LEFT);
		messageFeed.setPrefSize(200, 350); messageFeed.setStyle("-fx-border-color:black");
		TextField text = new TextField(); text.setLayoutX(xBase+20); text.setLayoutY(yBase+150);
		text.setPromptText("TypeHere");
		text.setOnAction(e->{
			messageFeed.setText(messageFeed.getText()+"\n"+text.getText());
			client.sendMessage("request:msg\tcontent:"+text.getText());
		});
		//TextField password = new TextField();
		
		Button menu1 = new Button("CreateChat"); menu1.setLayoutX(xBase-170); menu1.setLayoutY(yBase+150);
		menu1.setOnAction(e->{
			//messageFeed.setText(messageFeed.getText()+"\n"+text.getText());
			client.sendMessage("request:add_chatroom\tcontent:"+text.getText());
		});
		Button menu6 = new Button("Login"); menu6.setLayoutX(xBase-100); menu6.setLayoutY(yBase+150);
		menu6.setOnAction(e->{
			//messageFeed.setText(messageFeed.getText()+"\n"+text.getText());
			client.sendMessage("request:login\tcontent:"+text.getText());
		});
		Button menu7 = new Button("SetChat"); menu7.setLayoutX(xBase-50); menu7.setLayoutY(yBase+150);
		menu7.setOnAction(e->{
			//messageFeed.setText(messageFeed.getText()+"\n"+text.getText());
			client.sendMessage("request:set_chatroom\tcontent:"+text.getText());
		});
		
		
		Button menu3 = new Button("History"); menu3.setLayoutX(xBase+40); menu3.setLayoutY(yBase+70);
		menu3.setOnAction(e->{
			client.sendMessage("request:history\tcontent:");
		});
		
		Button menu4 = new Button("Show more"); menu4.setLayoutX(xBase+40+30); menu4.setLayoutY(yBase+120);
		menu4.setOnAction(e->{
			String messageFeedtext = messageFeed.getText();
			if (messageFeedtext.length()>100){
				int counter = 0;
				for(int x=0;x<messageFeedtext.length();x++){
					if(messageFeedtext.charAt(x)=='\n'){
						counter+=1;
						if (counter==3){
							messageFeedtext=messageFeedtext.substring(x);
							messageFeed.setText(messageFeedtext);
							break;
						}
					}
				}
				}
		});
		Button menu5 = new Button("Logout"); menu5.setLayoutX(xBase-10+30);menu5.setLayoutY(yBase+120);
		menu5.setOnAction(e->{
			client.sendMessage("request:logout\tcontent:");
		});
		
		serverIn = new TextArea("info messages from server");  serverIn.setLayoutX(xBase+5); serverIn.setLayoutY(yBase-200); //serverIn.setAlignment(Pos.TOP_LEFT);
		serverIn.setPrefSize(200, 230); serverIn.setStyle("-fx-border-color:black");
		
		Button menu2 = new Button("Clear messageFeed"); menu2.setLayoutX(xBase+40); menu2.setLayoutY(yBase+40);
		menu2.setOnAction(e->{
			messageFeed.setText("Window cleared");
			serverIn.setText("Window cleared");
		});
		
		
		root.getChildren().addAll(messageFeed, menu1, menu2, menu3, menu4, menu5, menu6, menu7, text, serverIn);
		Scene scene = new Scene(root, 400, 400);
		updater1 = new FeedUpdater(client, messageFeed, client.ChatWindow);
		updater1.start();
		updater2 = new FeedUpdater(client, serverIn, client.ChatWindowInfo);
		updater2.start();
		return scene;
	}
	
	
	public void wakeUp(){
		serverIn.setText("");
		messageFeed.setText("");
	}
	public void sleep(){
		
	}
}