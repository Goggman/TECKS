package javaClientCode;
import java.util.ArrayList;
import javafx.stage.Stage;
import javafx.scene.Scene;
/**
 * 
 * //Connects all the different scenes in an array, used as and argument with the stage in all sceneCreators
 *
 */
public class GUIController { 
	
	ArrayList<Scene> windows;
	Stage stage;
	
	/**
	 * contains all the scenes that are accessable in the GUI
	 * @param stageInput
	 */
	GUIController(Stage stageInput){
		stage=stageInput;
		windows = new ArrayList<>();
		Window mw = new MenuWindow(stage, this);//Create the scenes to use, then add them in the list of windows/scenes
		Window qw = new QuestionWindow(stage, this, new QuestionSchema());
		Window cqw = new CreateQWindow(stage, this);
		Window lqw = new LoadQWindow(stage, this);
		addScene(mw.createScene()); addScene(qw.createScene()); addScene(cqw.createScene()); addScene(lqw.createScene());
		
	}
	
	/**
	 * add new scene
	 * @param window
	 */
	void addScene(Scene window){
		if (! windows.contains(window)){
			windows.add(window);
		}
		
	}
	/**
	 * getter method for scene
	 * @param index
	 * @return Scene object
	 */
	Scene getScene(int index){
		return windows.get(index);
	}
	
}
