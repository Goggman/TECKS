package javaClientCode;

import java.util.ArrayList;

public class Question {
	
	private String questionText, correctAnswer, header, category; 
	private ArrayList<String> options = new ArrayList<>();
	
	/**
	 * getter method for the question text
	 * @return String question text
	 */
	public String getQuestionText() {
		return questionText;
	}

	/**
	 * getter method for correct answer
	 * @return String with correct answer
	 */
	public String getCorrectAnswer() {
		return correctAnswer;
	}
	
	/**
	 * getter method for category of question
	 * @return String with category
	 */
	public String getCategory(){
		return category;
	}
	
	/**
	 * getter method for header
	 * @return String with header
	 */
	public String getHeader() {
		return header;
	}
	
	/**
	 * create empty question object
	 */
	public Question(){
		
	}
	
	/**
	 * Create Question object
	 * @param categoryInput category of the question
	 * @param question question text
	 * @param answer answer text
	 * @param header header text
	 * @param options list of options
	 */
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
	
	/**
	 * getter method for options
	 * @return ArrayList of options
	 */
	public ArrayList<String> getOptions() {
		return options;
	}
	/**
	 * setter method for category
	 * @param text
	 */
	public void setCategory(String text){
		this.category=text;
		
	}

	/**
	 * add method for options
	 * @param option
	 */
	public void setOptions(String option) {
		this.options.add(option);
	}

	/**
	 * setter method for header
	 * @param string
	 */
	public void setHeader(String string) {
		this.header = string;
	}
	
	/**
	 * setter method for answer
	 * @param string
	 */
	public void setAnswer(String string) {
		this.correctAnswer = string;
	}
	
	/**
	 * setter method for question
	 * @param string
	 */
	public void setQuestion(String string) {
		this.questionText = string;
	}
}
