package javaClientCode;
import java.util.ArrayList;
public class Controller { //Connects all different components, lots of control n stuff
	
	Analyzer analyzer;
	Asker asker;
	GUIData guiData;
	
	Controller(){
		init();
		
	}
	
	
	void init(){
		
		analyzer = new Analyzer();
		//asker = new Asker();
	}
	void run(){
		init();
		asker.askMany(asker.makeQuiz());
		
		
	}
}
