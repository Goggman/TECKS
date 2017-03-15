package javaClientCode;
import java.util.ArrayList;
import java.util.HashMap;
public class QuestionSchema {
	HashMap<Question, String> answers = new HashMap<>();
	ArrayList<Question> questions = new ArrayList<>();
	String category;
	
	/**
	 * creates new question; generates empty strings to be filled in
	 * @param questionQuiz list of Question objects 
	 */
	/*
	QuestionSchema(ArrayList<Question_Alt> questionQuiz){
		questions=questionQuiz;
		answers=new ArrayList<String>();
		for (int x=0;x<questions.size();x++){
			answers.add("");
		}
	}*/
	
	QuestionSchema(ArrayList<ArrayList<String>> quizList){
		
		for (ArrayList<String> list : quizList){
			Question q = new Question();
			for (String s : list){
				
				String[] temp = s.split(":");
				if (temp.length < 1){
					//do nothing
				}
				else if (s.contains("Header: ")){
					q.setHeader(temp[1].trim());
				}
				else if (s.contains("q:")){
					q.setQuestion(temp[1].trim());
				}
				else if (s.contains("a:")){
					q.setAnswer(temp[1].trim());
				}
				else if (s.contains("op:")){
					q.addOptions(temp[1].trim());
				}
				else if (s.contains("c:")){
					q.setCategory(temp[1].trim());
				}
			}
			questions.add(q);
			
			
		}
	
	}
	
	QuestionSchema(){
		
	}
	
	/**
	 * creates new question if no params are specified
	 *//*
	QuestionSchema(){
		questions=makeQuiz();
		answers=new ArrayList<String>();
		for (int x=0;x<questions.size();x++){
			answers.add("");
		}
	}*/
	
	/**
	 * getter method for questions
	 * @return ArrayList of questions
	 */
	ArrayList<Question> getQuestions(){
		return questions;
	}
	
	/**
	 * getter method for answers
	 * @return Strings containing the correct answers
	 */
	HashMap<Question, String> getAnswers(){
		return answers;
	}
	
	
	
	/*
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
	}*/
	
}
