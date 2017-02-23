package javaClientCode;

import java.util.ArrayList;

public class Question {
	
	private String questionText, correctAnswer, header, category; 
	private ArrayList<String> options = new ArrayList<>();
	
	public String getQuestionText() {
		return questionText;
	}

	public String getCorrectAnswer() {
		return correctAnswer;
	}
	public String getCategory(){
		return category;
	}
	public String getHeader() {
		return header;
	}
	
	public Question(){
		
	}
	
	public Question(String categoryInput, String question, String answer, String header, String... options){
		this.questionText = question;
		this.correctAnswer  = answer;
		this.category=categoryInput;
		this.header = header;
		if (options.length > 0){
			for (int i = 0; i < options.length; i++){
				this.getOptions().add(options[i]);
			}
		}
	}
	
	public ArrayList<String> getOptions() {
		return options;
	}

	public void setOptions(String option) {
		this.options.add(option);
	}

	public void setHeader(String string) {
		this.header = string;
	}
	
	public void setAnswer(String string) {
		this.correctAnswer = string;
	}
	
	public void setQuestion(String string) {
		this.questionText = string;
	}
}
