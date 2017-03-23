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
		feed.setPrefSize(400, 400);
		TextField username = new TextField("Enter username"); username.setLayoutX(xBase+0); username.setLayoutY(yBase-50);
		username.setOnAction(e->{
			client.sendMessage("request:login\tcontent:"+username.getText());
		});
		//TextField password = new TextField();
		Button menu1 = new Button("Log in"); menu1.setLayoutX(xBase+0); menu1.setLayoutY(yBase+20);
		menu1.setOnAction(e->{
			client.sendMessage("request:login\tcontent:"+username.getText());
		});
		Button menu2 = new Button("Show chat"); menu2.setLayoutX(xBase+0);menu2.setLayoutY(yBase+50);
		menu2.setOnAction(e->{
			chat.show();
		});
		Button menu3 = new Button("Hide chat"); menu3.setLayoutX(xBase+0);menu3.setLayoutY(yBase+100);
		menu3.setOnAction(e->{
			chat.hide();
		});
		Button menu4 = new Button("To Menu"); menu4.setLayoutX(xBase+0);menu4.setLayoutY(yBase+150);
		menu4.setOnAction(e->{
			stage.setScene(ctrl.getScene(0));
		});
		Button menu5 = new Button("Clear window"); menu5.setLayoutX(xBase+0);menu5.setLayoutY(yBase+200);
		menu5.setOnAction(e->{
			feed.setText("Window cleared");
		});
		root.getChildren().addAll(feed, menu1, menu2, menu3, menu4, menu5, username);
		Scene scene = new Scene(root, 1300, 700);
		FeedUpdater updater = new FeedUpdater(client, feed, client.LoginWindow);
		//FeedUpdater updater = new FeedUpdater(client, feed);
		updater.start();
		return scene;
	}


}
