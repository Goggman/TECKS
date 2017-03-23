package javaClientCode;
import java.util.HashMap;


/**
 * 
 *  //Should process user input, answered questions, general performance, maybe categorize problems
 * @author Martin
 *
 */
public class Analyzer {
	HashMap<String, Integer> categories;
	
	Analyzer(){
		init_score();
	}
	
	/**
	 * initilialize categories
	 */
	void init_score(){
		categories = new HashMap<String, Integer>();
		/*
		categories.put("String_Formatting", 0);
		categories.put("If_else", 0);
		categories.put("Loops",0);
		categories.put("Scope",0);
		categories.put("Conventions",0);*/
		
	}
	
	HashMap<String, Integer> getCategories(){
		return categories;
	}
	
	/**
	 * Maybe some methods to decide what category to ask, or maybe even find wich questions to ask
	 */

	
	/**
	 * method to request files from central server, maybe needed 
	 */
	
}
