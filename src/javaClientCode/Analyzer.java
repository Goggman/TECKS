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
		
	}
	
	HashMap<String, Integer> getCategories(){
		return categories;
	}
	//TODO: make this prepare and/or send a add_results message to server
}
