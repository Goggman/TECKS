package TECKS;

import java.util.ArrayList;

public class Question {
	
	private String questionText, correctAnswer, header;
	private ArrayList<String> options = new ArrayList<>();
	
	public String getQuestion() {
		return questionText;
	}

	public String getAnswer() {
		return correctAnswer;
	}

	public String getHeader() {
		return header;
	}
	
	public Question(){
		
	}
	
	public Question(String question, String answer, String header, String... options){
		this.questionText = question;
		this.correctAnswer  = answer;
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
	
}
