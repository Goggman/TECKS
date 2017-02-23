package javaClientCode;
import java.util.ArrayList;
import java.util.HashMap;
public class QuestionSchema {
	ArrayList<String> answers;
	ArrayList<Question_Alt> questions;
	
	/**
	 * creates new question; generates empty strings to be filled in
	 * @param questionQuiz list of Question objects 
	 */
	QuestionSchema(ArrayList<Question_Alt> questionQuiz){
		questions=questionQuiz;
		answers=new ArrayList<String>();
		for (int x=0;x<questions.size();x++){
			answers.add("");
		}
	}
	
	/**
	 * creates new question if no params are specified
	 */
	QuestionSchema(){
		questions=makeQuiz();
		answers=new ArrayList<String>();
		for (int x=0;x<questions.size();x++){
			answers.add("");
		}
	}
	
	/**
	 * getter method for questions
	 * @return ArrayList of questions
	 */
	ArrayList<Question_Alt> getQuestions(){
		return questions;
	}
	
	/**
	 * getter method for answers
	 * @return Strings containing the correct answers
	 */
	ArrayList<String> getAnswers(){
		return answers;
	}

	ArrayList<Question_Alt> makeQuiz(){
		ArrayList<Question_Alt> quiz = new ArrayList<Question_Alt>();
		Question_Alt question1 = new Question_Alt("String_Formatting", "Which method do one use when comparing strings in Java 8?","equals");
		Question_Alt question2 = new Question_Alt("If_else","If an if condition is not met, which statement\n does one use after to have something else happen?","else");
		Question_Alt question3 = new Question_Alt("Loops","Fill in correct initialization of the loop: for(_ x;x<10;x++)","int");
		Question_Alt question4 = new Question_Alt("Scope","If a variable is declared within a method, and nowhere else,\n is it accessible from outside the method in question?","no");
		Question_Alt question5 = new Question_Alt("Conventions","If one wants to keep the fields of an object \'private\', \n which keyword is used before the variable?","private");
		
		quiz.add(question1);
		quiz.add(question2);
		quiz.add(question3);
		quiz.add(question4);
		quiz.add(question5);
		
		return quiz;
	}
	
}
