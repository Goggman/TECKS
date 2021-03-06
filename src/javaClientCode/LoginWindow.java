package javaClientCode;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
	String type;
	LoginWindow(Stage stageInput, GUIController CtrlIn, ServerClient clientIn, Stage chatIn ){
		ctrl=CtrlIn;
		stage=stageInput;
		client=clientIn;
		chat=chatIn;
	}
	public Scene createScene(){
		int xBase=100, yBase = 80;
		Pane root = new Pane(); root.setStyle("-fx-background-color: white");
		feed = new TextArea("Please log in"); feed.setLayoutX(xBase); feed.setLayoutY(300); //feed.setAlignment(Pos.TOP_LEFT);
		Label title = new Label("Login");title.setLayoutX(100);title.setLayoutY(50);title.setStyle("-fx-font-size: 30px");
		feed.setPrefSize(400, 100); feed.setStyle("-fx-border-color:black"); feed.setLayoutX(100); feed.setLayoutY(500);
		feed.setWrapText(true);
		feed.setEditable(false);
		File image = new File("POSTER_BOY_NOEDGE_EDIT.png");
		Image tecboy = new Image(image.toURI().toString());
		ImageView poster_boy = new ImageView(); poster_boy.setLayoutX(xBase+70); poster_boy.setLayoutY(yBase+20);
		poster_boy.setImage(tecboy); poster_boy.setFitWidth(200); poster_boy.setPreserveRatio(true);
		
		

		Button tab1 = new Button("Quiz"); tab1.setLayoutX(92); tab1.setLayoutY(2); 
		tab1.setStyle("-fx-background-color: -fx-outer-border, -fx-inner-border, -fx-body-color; -fx-background-insets: 0, 1, 2;-fx-background-radius: 5, 4, 3;");

		tab1.setPrefWidth(100);
		tab1.setOnAction(e->{
			stage.setScene(ctrl.getScene(1)); //QuestionScene at index 1 in GUIctrl
			
		});
		


		Button tab2 = new Button("Qcreator"); tab2.setLayoutX(196); tab2.setLayoutY(2); 
		tab2.setStyle("-fx-background-color: -fx-outer-border, -fx-inner-border, -fx-body-color; -fx-background-insets: 0, 1, 2;-fx-background-radius: 5, 4, 3;");

		tab2.setPrefWidth(100);
		tab2.setOnAction(e->{
			stage.setScene(ctrl.getScene(2));
		});
		

		

		Button tab3 = new Button("Login"); tab3.setLayoutX(300); tab3.setLayoutY(2); 
		tab3.setStyle("-fx-background-color: -fx-outer-border, -fx-inner-border, -fx-body-color; -fx-background-insets: 0, 1, 2;-fx-background-radius: 5, 4, 3;");

		tab3.setPrefWidth(100);
		tab3.setOnAction(e->{
			stage.setScene(ctrl.getScene(4));
		});
		


		Button tab4 = new Button("Profile"); tab4.setLayoutX(404); tab4.setLayoutY(2); 
		tab4.setStyle("-fx-background-color: -fx-outer-border, -fx-inner-border, -fx-body-color; -fx-background-insets: 0, 1, 2;-fx-background-radius: 5, 4, 3;");

		tab4.setPrefWidth(100);
		tab4.setOnAction(e->{
			stage.setScene(ctrl.getScene(5));
		});
		
		

		PasswordField password = new PasswordField(); password.setLayoutX(220); password.setLayoutY(yBase+180);
		TextField username = new TextField(); username.setLayoutX(220); username.setLayoutY(yBase+145);

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
		Button menu1 = new Button("Login"); menu1.setLayoutX(xBase+150); menu1.setLayoutY(yBase+210);
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
		
		type = "student";
		MenuButton userType = new MenuButton("PickType"); userType.setLayoutX(xBase+150); userType.setLayoutY(yBase+300); userType.setPrefWidth(100); userType.setPrefHeight(30);
		MenuItem student = new MenuItem("student");
		MenuItem lecturer = new MenuItem("lecturer");
		MenuItem admin = new MenuItem("admin");
		userType.getItems().addAll(student, lecturer, admin);
		student.setOnAction(e->{
			type = "student";
			userType.setText(type);
		});
		lecturer.setOnAction(e->{
			type = "lecturer";
			userType.setText(type);
		});
		admin.setOnAction(e->{
			type = "admin";
			userType.setText(type);
		});
		
		Button createUser = new Button("CreateUser"); createUser.setLayoutX(xBase+150); createUser.setLayoutY(yBase+270);
		createUser.setPrefWidth(100);
		createUser.setOnAction(e->{
			client.sendMessage("request:login\tcontent:"+username.getText()+"@"+password.getText()+"@"+type);
			type="student";
			username.clear();
			password.clear();
			
		});
		
		Button showChat = new Button("ShowChat"); showChat.setLayoutX(502);showChat.setLayoutY(543);
		showChat.setStyle("-fx-pref-width: 95");
		showChat.setOnAction(e->{
			ctrl.chat.wakeUp();
			chat.show();
			
		});
		Button hideChat = new Button("HideChat"); hideChat.setLayoutX(502);hideChat.setLayoutY(573);
		hideChat.setStyle("-fx-pref-width: 95");
		hideChat.setOnAction(e->{
			ctrl.chat.sleep();
			chat.hide();
		});

		root.getChildren().addAll(feed, menu1, showChat, hideChat, menu6, username, tab1, tab2, tab3, tab4, password, poster_boy, createUser, userType, title);
		Scene scene = new Scene(root, 600, 600);
		scene.getStylesheets().add(getClass().getResource("GUI.css").toExternalForm());
		FeedUpdater updater = new FeedUpdater(client, feed, client.LoginWindow);
		updater.start();
		return scene;
	}
	public void wakeUp(){
		if (newStart == 1){
			feed.setText("Please log in");
			newStart = 0;
		}
		else{
			feed.clear();
		}
		
	}
	public void sleep(){
		
	}

}
