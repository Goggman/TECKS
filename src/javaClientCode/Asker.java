package javaClientCode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;
import java.util.Iterator;
public class Asker {
	
	
	
	
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
