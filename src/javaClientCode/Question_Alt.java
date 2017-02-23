package javaClientCode;

public class Question_Alt{
	String correctAnswer;
	String questionText;
	String category;
	
	Question_Alt(String categoryInput, String questionTextInput, String correctAnswerInput)
	{
		category= categoryInput;
		questionText = questionTextInput;
		correctAnswer = correctAnswerInput;
	}
	Question_Alt(){
		category= "empty";
		questionText = "empty";
		correctAnswer = "empty";
	}
	
	Question_Alt loader(){ //Load questions from file, maybe from server, maybe should be in Asker
		return new Question_Alt();
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
