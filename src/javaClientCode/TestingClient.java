package javaClientCode;
import java.io.FileInputStream;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import java.util.Scanner;

import javaClientCode.GUIController;
import javafx.stage.Stage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;



public class TestingClient extends Application {
	public void start(Stage stage) throws Exception{
		Stage chat = new Stage();
		ServerClient client = new ServerClient();
		ChatWindow chatScene = new ChatWindow(chat, client);
		chat.setScene(chatScene.createScene());
		LoginWindow login = new LoginWindow(stage, null, client, chat);
		
		stage.setScene(login.createScene());
		stage.setTitle("TECKS");
		stage.show();
		
		
		
	}
	
	
public static void main(String[] args){
	//launch();
	ServerClient client = new ServerClient();
	client.listen();
}
}