package javaClientCode;
import java.util.ArrayList;
import javafx.stage.Stage;
import javafx.scene.Scene;
public class GUIController { //Connects all different components, lots of control n stuff
	
	ArrayList<Scene> windows;
	Stage stage;
	
	GUIController(Stage stageInput){
		stage=stageInput;
		windows = new ArrayList<>();
		Window mw = new MenuWindow(stage, this);								//Create the scenes to use, then add them in the list of windows/scenes
		Window qw = new QuestionWindow(stage, new QuestionSchema(), this);
		addScene(mw.createScene()); addScene(qw.createScene());
		
	}
	
	void addScene(Scene window){
		if (! windows.contains(window)){
			windows.add(window);
		}
		
	}
	Scene getWindow(int index){
		return windows.get(index);
	}
	
}
