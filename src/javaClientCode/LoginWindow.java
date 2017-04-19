package javaClientCode;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;
import java.io.*;
public class LoginWindow implements Window{
	Stage stage;
	GUIController ctrl;
	ServerClient client;
	TextArea feed;
	Stage chat;
	int newStart = 1;
	LoginWindow(Stage stageInput, GUIController CtrlIn, ServerClient clientIn, Stage chatIn ){
		ctrl=CtrlIn;
		stage=stageInput;
		client=clientIn;
		chat=chatIn;
	}
	public Scene createScene(){
		int xBase=100, yBase = 0;
		Pane root = new Pane(); root.setStyle("-fx-background-color: white");
		feed = new TextArea("Please log in"); feed.setLayoutX(xBase); feed.setLayoutY(yBase+300); //feed.setAlignment(Pos.TOP_LEFT);
		feed.setPrefSize(400, 200); feed.setStyle("-fx-border-color:black");feed.setEditable(false);
		//Image tecboy = new Image(System.getProperties().getProperty("user.dir") +"/TECKS/src/javaClientCode/POSTER_BOY_NOEDGE_EDIT.png");
		File image = new File(System.getProperties().getProperty("user.dir") +"/TECKS/src/javaClientCode/POSTER_BOY_NOEDGE_EDIT.png");
		Image tecboy = new Image(image.toURI().toString());
		ImageView poster_boy = new ImageView(); poster_boy.setLayoutX(xBase+70); poster_boy.setLayoutY(yBase+20);
		poster_boy.setImage(tecboy); poster_boy.setFitWidth(200); poster_boy.setPreserveRatio(true);
		
		
		
		Button tab1 = new Button("Goto quiz"); tab1.setLayoutX(100); tab1.setLayoutY(0);
		tab1.setStyle("-fx-pref-width: 100");
		tab1.setOnAction(e->{
			stage.setScene(ctrl.getScene(1)); //QuestionScene at index 1 in GUIctrl
			
		});
		
		Button tab2 = new Button("Goto Qcreator"); tab2.setLayoutX(200); tab2.setLayoutY(0);
		tab2.setStyle("-fx-pref-width: 100");
		tab2.setOnAction(e->{
			stage.setScene(ctrl.getScene(2));
		});
		
		////
		
		Button tab3 = new Button("Goto login"); tab3.setLayoutX(300); tab3.setLayoutY(0);
		tab3.setStyle("-fx-pref-width: 100");
		tab3.setOnAction(e->{
			stage.setScene(ctrl.getScene(4));
		});
		
		Button tab4 = new Button("Goto profile"); tab4.setLayoutX(400); tab4.setLayoutY(0);
		tab4.setStyle("-fx-pref-widt: 100");
		tab4.setOnAction(e->{
			stage.setScene(ctrl.getScene(5));
		});
		
		
		PasswordField password = new PasswordField(); password.setLayoutX(xBase+100); password.setLayoutY(yBase+180);
		TextField username = new TextField(); username.setLayoutX(xBase+100); username.setLayoutY(yBase+140);
		username.setPromptText("username");
		username.setStyle("-fx-pref-width: 160");
		username.setOnAction(e->{
			client.sendMessage("request:login\tcontent:"+username.getText()+"@"+password.getText());
			username.clear(); password.clear();
		});
		
		password.setPromptText("password");
		password.setStyle("-fx-pref-width: 160");
		password.setOnAction(e->{
			client.sendMessage("request:login\tcontent:"+username.getText()+"@"+password.getText());
			username.clear(); password.clear();
		});

		//TextField password = new TextField();
		Button menu1 = new Button("Log in"); menu1.setLayoutX(xBase+150); menu1.setLayoutY(yBase+210);
		menu1.setPrefWidth(100);
		menu1.setOnAction(e->{
			client.sendMessage("request:login\tcontent:"+username.getText()+"@"+password.getText());
			username.clear(); password.clear();
		});
		Button menu6 = new Button("Logout"); menu6.setLayoutX(xBase+150); menu6.setLayoutY(yBase+240);
		menu6.setPrefWidth(100);
		menu6.setOnAction(e->{
			client.sendMessage("request:logout\tcontent:");
		});
		Button showChat = new Button("ShowChat"); showChat.setLayoutX(500);showChat.setLayoutY(550);
		showChat.setStyle("-fx-pref-width: 100");
		showChat.setOnAction(e->{
			ctrl.chat.wakeUp();
			chat.show();
			
		});
		Button hideChat = new Button("HideChat"); hideChat.setLayoutX(500);hideChat.setLayoutY(575);
		hideChat.setStyle("-fx-pref-width: 100");
		hideChat.setOnAction(e->{
			ctrl.chat.sleep();
			chat.hide();
		});

		Button menu5 = new Button("Clear window"); menu5.setLayoutX(xBase);menu5.setLayoutY(yBase+500);
		menu5.setStyle("-fx-pref-width: 100");
		menu5.setOnAction(e->{
			feed.setText("Window cleared");
		});

		root.getChildren().addAll(feed, menu1, showChat, hideChat, menu5, menu6, username, tab1, tab2, tab3, tab4, password, poster_boy);
		Scene scene = new Scene(root, 600, 600);
		//scene.getStylesheets().add(getClass().getResource("GUI.css").toExternalForm());
		FeedUpdater updater = new FeedUpdater(client, feed, client.LoginWindow);
		//FeedUpdater updater = new FeedUpdater(client, feed);
		updater.start();
		return scene;
	}
	public void wakeUp(){
		if (newStart == 1){
			feed.setText("Please log in");
			newStart = 0;
		}
		else{
			feed.setText("");
		}
		
	}
	public void sleep(){
		
	}

}
