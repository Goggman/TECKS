package javaClientCode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;
import java.util.Iterator;
public class Asker {
	HashMap<String, ArrayList<Question>> questionMap; //A dictionary of questions sorted by category could be nice
	//ArrayList<Question> sampleQuestions;
	//HashMap<String, Integer> score;
	Analyzer analyzer;
	
	Asker(HashMap<String, Integer> categoryMap, ArrayList<Question> listQuestions){
		Set<String> keys= categoryMap.keySet();
		Iterator<String> keyIterate = keys.iterator();
		while  ( ! keys.isEmpty()){
			questionMap.put(keyIterate.next(), null);
		}
	}
	Asker (Analyzer analyzerInput){
		analyzer = analyzerInput;
		
		
	}
	
	void ask(Question question){//the main method of this class maybe, should print question text and wait for user input, check against the correct answer, etc
		Scanner scanner = new Scanner(System.in);
		System.out.println(question.getQuestionText());
		System.out.println("Enter Answer: ");
		String input = scanner.nextLine();
		
		if (input.equals(question.getCorrectAnswer()))
		{
			System.out.println("Correct answer!");
			//System.out.println("Category: " + question.getCategory());
			int value = analyzer.getCategories().get(question.getCategory());
			analyzer.getCategories().put(question.getCategory(),
										 value + 1);
			
		}
		else{
			System.out.println("Would you like a hint?");
		}
		//scanner.close();
		
	}
	
	void askMany(ArrayList<Question> listOfQuestions){
		Iterator<Question> questionIterator = listOfQuestions.iterator();
		while (questionIterator.hasNext()){
			ask(questionIterator.next());
			
		}
		System.out.println("Your results: ");
		System.out.println(analyzer.getCategories());
		
	}
	

}
