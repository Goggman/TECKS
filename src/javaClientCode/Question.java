package javaClientCode;

public class Question{
	String correctAnswer;
	String questionText;
	String category;
	
	Question(String categoryInput, String questionTextInput, String correctAnswerInput)
	{
		category= categoryInput;
		questionText = questionTextInput;
		correctAnswer = correctAnswerInput;
	}
	Question(){
		category= "empty";
		questionText = "empty";
		correctAnswer = "empty";
	}
	
	Question loader(){ //Load questions from file, maybe from server, maybe should be in Asker
		return new Question();
	}
	String getQuestionText(){
		return questionText;
	}
	String getCategory(){
		return category;
	}
	String getCorrectAnswer(){
		return correctAnswer;
	}
}
