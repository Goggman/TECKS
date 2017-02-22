package javaClientCode;
import java.util.ArrayList;
import java.util.HashMap;
public class QuestionSchema {
	ArrayList<String> answers;
	ArrayList<Question> questions;
	
	QuestionSchema(ArrayList<Question> questionQuiz){
		questions=questionQuiz;
		answers=new ArrayList<String>();
		for (int x=0;x<questions.size();x++){
			answers.add("");
		}
	}
	QuestionSchema(){
		questions=makeQuiz();
		answers=new ArrayList<String>();
		for (int x=0;x<questions.size();x++){
			answers.add("");
		}
	}
	
	ArrayList<Question> getQuestions(){
		return questions;
	}
	ArrayList<String> getAnswers(){
		return answers;
	}

	ArrayList<Question> makeQuiz(){
		ArrayList<Question> quiz = new ArrayList<Question>();
		Question question1 = new Question("String_Formatting", "Which method do one use when comparing strings in Java 8?","equals");
		Question question2 = new Question("If_else","If an if condition is not met, which statement\n does one use after to have something else happen?","else");
		Question question3 = new Question("Loops","Fill in correct initialization of the loop: for(_ x;x<10;x++)","int");
		Question question4 = new Question("Scope","If a variable is declared within a method, and nowhere else,\n is it accessible from outside the method in question?","no");
		Question question5 = new Question("Conventions","If one wants to keep the fields of an object \'private\', \n which keyword is used before the variable?","private");
		
		quiz.add(question1);
		quiz.add(question2);
		quiz.add(question3);
		quiz.add(question4);
		quiz.add(question5);
		
		return quiz;
	}
	
}
