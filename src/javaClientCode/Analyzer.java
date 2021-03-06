package javaClientCode;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 
 *  //Should process user input, answered questions, general performance, maybe categorize problems
 * @author Martin
 *
 */
public class Analyzer {
	HashMap<String, int[]> categories;
	int totalNumberOfQuestions;
	
	public Analyzer(){
		init_score();
	}
	
	/**
	 * initilialize categories
	 */
	
	void init_score(){
		categories = new HashMap<String, int[]>();
		totalNumberOfQuestions = 0;
	}
	/**
	 * Insert score of subject
	 * @param categoryIn
	 * @param isCorrect
	 */
	public void registerAnswer(String categoryIn, boolean isCorrect){
		if (getCategories().containsKey(categoryIn)){
			int scoreOld = categories.get(categoryIn)[0];
			int numberOfQuestionsOld = categories.get(categoryIn)[1];
			if (isCorrect){
				int[] newArray = new int[]{scoreOld+1, numberOfQuestionsOld+1};
				categories.put(categoryIn, newArray);
				totalNumberOfQuestions += 1;
			}
			else{
				int[] newArray = new int[]{scoreOld, numberOfQuestionsOld+1};
				categories.put(categoryIn, newArray);
				totalNumberOfQuestions += 1;
				
			}
			
		}
		else{
			
			if(isCorrect){
				int[] newArray = new int[]{1, 1};
				categories.put(categoryIn, newArray);
				totalNumberOfQuestions += 1;
			}
			else{
				int[] newArray = new int[]{0, 1};
				categories.put(categoryIn, newArray);
				totalNumberOfQuestions += 1;
			}
			
		}
	}
	
	public HashMap<String, int[]> getCategories(){
		return categories;
	}
	/**
	 * 
	 * @param categoryIn String key of the hashmap
	 * @return int[] in format [questions answered correctly, questions asked]
	 */
	public int[] getScoreAndQuestions(String categoryIn){
		return categories.get(categoryIn);
	}
	
	/**
	 * 
	 * @return a String with format: numberOfQuestions@category1|score1|score2@category2...
	 */
	public String prepareContent(){
		String content = totalNumberOfQuestions+"@";
		Iterator category_it = categories.keySet().iterator();
		
		while (category_it.hasNext()){
			String category = (String) category_it.next();
			int[] array = categories.get(category);
			content+=category+"|"+array[0]+"|"+array[1];
			if (category_it.hasNext()){
				content+="@";
			}
			
			
		}
		return content;
	}
	
}
