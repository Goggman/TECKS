package javaClientCode;
import java.util.ArrayList;
import javafx.stage.Stage;
import javafx.scene.Scene;
public class GUIController { //Connects all the different scenes in an array, used as and argument with the stage in all sceneCreators
	
	ArrayList<Scene> windows;
	Stage stage;
	
	GUIController(Stage stageInput){
		stage=stageInput;
		windows = new ArrayList<>();
		Window mw = new MenuWindow(stage, this);//Create the scenes to use, then add them in the list of windows/scenes
		Window qw = new QuestionWindow(stage, this, new QuestionSchema());
		Window cqw = new CreateQWindow(stage, this);
		Window lqw = new LoadQWindow(stage, this);
		addScene(mw.createScene()); addScene(qw.createScene()); addScene(cqw.createScene()); addScene(lqw.createScene());
		
	}
	
	void addScene(Scene window){
		if (! windows.contains(window)){
			windows.add(window);
		}
		
	}
	Scene getScene(int index){
		return windows.get(index);
	}
	
}
