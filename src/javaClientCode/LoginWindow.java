package javaClientCode;
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

public class LoginWindow implements Window{
	Stage stage;
	GUIController ctrl;
	ServerClient client;
	Stage chat;
	LoginWindow(Stage stageInput, GUIController CtrlIn, ServerClient clientIn, Stage chatIn ){
		ctrl=CtrlIn;
		stage=stageInput;
		client=clientIn;
		chat=chatIn;
	}
	public Scene createScene(){
		int xBase=600; int yBase = 200;
		Pane root = new Pane(); root.setStyle("-fx-background-color: white");
		Label feed = new Label("Please log in"); feed.setLayoutX(xBase-500); feed.setLayoutY(yBase-150); feed.setAlignment(Pos.TOP_LEFT);
		feed.setPrefSize(400, 400); feed.setStyle("-fx-border-color:black");

		TextField username = new TextField(); username.setLayoutX(xBase); username.setLayoutY(yBase-50);
		username.setPromptText("Enter username@password");
		username.setStyle("-fx-pref-width: 160");
		username.setOnAction(e->{
			client.sendMessage("request:login\tcontent:"+username.getText());
		});

		//TextField password = new TextField();
		Button menu1 = new Button("Log in"); menu1.setLayoutX(xBase); menu1.setLayoutY(yBase-20);
		menu1.setOnAction(e->{
			client.sendMessage("request:login\tcontent:"+username.getText());
		});
		Button menu2 = new Button("Show chat"); menu2.setLayoutX(xBase+520);menu2.setLayoutY(yBase-20);
		menu2.setStyle("-fx-pref-width: 100");
		menu2.setOnAction(e->{
			chat.show();
		});
		Button menu3 = new Button("Hide chat"); menu3.setLayoutX(xBase+520);menu3.setLayoutY(yBase+10);
		menu3.setStyle("-fx-pref-width: 100");
		menu3.setOnAction(e->{
			chat.hide();
		});
		Button menu4 = new Button("Menu"); menu4.setLayoutX(xBase);menu4.setLayoutY(yBase-yBase);
		menu4.setOnAction(e->{
			stage.setScene(ctrl.getScene(0));
		});
		Button menu5 = new Button("Clear window"); menu5.setLayoutX(xBase-500);menu5.setLayoutY(yBase+250);
		menu5.setStyle("-fx-pref-width: 100");
		menu5.setOnAction(e->{
			feed.setText("Window cleared");
		});
		Button menu6 = new Button("Logout"); menu6.setLayoutX(xBase);menu6.setLayoutY(yBase+10);
		menu6.setOnAction(e->{
			client.sendMessage("request:logout\tcontent:");
		});
		root.getChildren().addAll(feed, menu1, menu2, menu3, menu4, menu5,menu6, username);
		Scene scene = new Scene(root, 1300, 700);
		scene.getStylesheets().add(getClass().getResource("GUI.css").toExternalForm());
		FeedUpdater updater = new FeedUpdater(client, feed, client.LoginWindow);
		//FeedUpdater updater = new FeedUpdater(client, feed);
		updater.start();
		return scene;
	}
	public void wakeUp(){
		
	}
	public void sleep(){
		
	}

}
