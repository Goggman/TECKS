package javaServerCode;
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
import javaClientCode.GUI.chat;
import javafx.stage.Stage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;



public class TestingServer extends Application {
	public void start(Stage stage) throws Exception{
		//LoginWindow login = new LoginWindow(stage, null);
		stage.setScene(null);
		stage.setTitle("TECKS");
		stage.show();
	}
	
	
public static void main(String[] args){
	launch();
	
}
}
