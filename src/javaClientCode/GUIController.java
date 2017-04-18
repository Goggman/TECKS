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
	ArrayList<Window> vindauge; 
	
	Stage stage;
	ServerClient client;
	Window chat;
	Stage chatStage;
	/**
	 * contains all the scenes that are accessable in the GUI
	 * @param stageInput
	 */
	GUIController(){
		
	}
	GUIController(Stage stageInput){
		client=new ServerClient();
		chatStage = new Stage(); chatStage.setTitle("Chat"); chatStage.setResizable(false);
		chatStage.hide();
		chat = new ChatWindow(chatStage, client);
		chatStage.setScene(chat.createScene());
		stage=stageInput; stage.setResizable(false);
		

		windows = new ArrayList<Scene>();
		vindauge = new ArrayList<Window>();
		
		Window mw = new MenuWindow(stage, this);//Create the scenes to use, then add them in the list of windows/scenes

		Window qw = new QuestionWindow(stage, this, client, chatStage);
		Window cqw = new CreateQWindow(stage, this, client, chatStage);
		Window pw = new ProfileWindow(stage, this, client, chatStage);
		Window dummy = new DummyWindow(stage, this, client, chatStage);
		Window linw = new LoginWindow(stage, this, client, chatStage);
		
		addScene(mw.createScene()); addScene(qw.createScene()); addScene(cqw.createScene());addScene(dummy.createScene());
		addScene(linw.createScene()); addScene(pw.createScene());
		
		addWindow(mw); addWindow(qw); addWindow(cqw);addWindow(dummy);
		addWindow(linw); addWindow(pw);
		
	}
	
	private void addWindow(Window mw) {
		vindauge.add(mw);
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
		((Window)vindauge.get(index)).wakeUp();
		return windows.get(index);
	}
	
}
