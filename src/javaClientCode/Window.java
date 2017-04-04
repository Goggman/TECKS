package javaClientCode;
import javafx.scene.Scene;

public interface Window {
	Scene createScene();
	void wakeUp(); // whenever getScene from GUI controller is called, this method is invoked, should rouse all threads needed in the curretn scene
	void sleep(); // whenever one switches the current scene, this should be invoked. Should put every thread not in use to inactive mode
}
