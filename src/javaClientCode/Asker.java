package javaClientCode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;
import java.util.Iterator;
public class Asker implements Listener {
	HashMap<String, ArrayList<Question>> questionMap; //A dictionary of questions sorted by category could be nice
	//ArrayList<Question> sampleQuestions;
	//HashMap<String, Integer> score;
	Analyzer analyzer;
	GUIData data;
	ArrayList<Object> listener;
	int change;
	
	
	Asker(HashMap<String, Integer> categoryMap, ArrayList<Question> listQuestions){//Maybe sort lists of questions by category, WIP
		Set<String> keys= categoryMap.keySet();
		Iterator<String> keyIterate = keys.iterator();
		while  ( ! keys.isEmpty()){
			questionMap.put(keyIterate.next(), null);
		}
	}
	Asker (GUIData dataInit){
		analyzer = new Analyzer();
		data = dataInit;
		change=0;

	}
	
	public	void trigger(){
		change=1;
		
	}
	void resetTrigger(){
		change=0;
	}
	
	void ask(Question question){//the main method of this class maybe, should print question text and wait for user input, check against the correct answer, etc
		resetTrigger();
		data.setTextLabel( question.getQuestionText());
		data.setTextLabel("Enter Answer: ");
		
		String input = data.getTextField();
		
		if (input.equals(question.getCorrectAnswer()))
		{
			data.setTextLabel("Correct answer!");
			//System.out.println("Category: " + question.getCategory());
			int value = analyzer.getCategories().get(question.getCategory());
			analyzer.getCategories().put(question.getCategory(),
										 value + 1);
			
		}
		else{
			data.setTextLabel("Would you like a hint?");
		}
		resetTrigger();
		
	}
	
	void askMany(ArrayList<Question> listOfQuestions){
		Iterator<Question> questionIterator = listOfQuestions.iterator();
		while (questionIterator.hasNext()){
			ask(questionIterator.next());
			
		}
		data.setTextLabel("Your results: ");
		data.setTextLabel(""+analyzer.getCategories());
		
	}
	
	ArrayList<Question> makeQuiz(){
		ArrayList<Question> quiz = new ArrayList<Question>();
		Question question1 = new Question("String_Formatting", "Wich method do one use when comparing strings in Java 8?","equals");
		Question question2 = new Question("If_else","If an if condition is not met, which statement does one use after to have something else happen?","else");
		Question question3 = new Question("Loops","Fill in correct initialization of the loop: for(_ x;x<10;x++)","int");
		Question question4 = new Question("Scope","If a variable is declared within a method, and nowhere else, is it accessible from outside the method in question?","no");
		Question question5 = new Question("Conventions","If one wants to keep the fields of an object \'private\', wich keyword is used before the variable?","private");
		
		quiz.add(question1);
		quiz.add(question2);
		quiz.add(question3);
		quiz.add(question4);
		quiz.add(question5);
		
		return quiz;
	}
	

}
