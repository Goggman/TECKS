package javaClientCode;
import java.util.HashMap;



public class Analyzer { //Should process user input, answered questions, general performance, maybe categorize problems
	
	HashMap<String, Integer> categories;
	
	Analyzer(){
		init_score();
	}
	void init_score(){
		categories = new HashMap<String, Integer>();
		categories.put("String_Formatting", 0);
		categories.put("If_else", 0);
		categories.put("Loops",0);
		categories.put("Scope",0);
		categories.put("Conventions",0);
		
	}
	
	HashMap<String, Integer> getCategories(){
		return categories;
	}
	
	String findCategory(){  //Maybe some methods to decide what category to ask, or maybe even find wich questions to ask
		return "programming";
		
	}
	
	void loadFromServer(){ //method to request files from central server, maybe needed
		
	}
	
}
