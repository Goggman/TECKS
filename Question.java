package TECKS;

import java.util.ArrayList;

public class Question {
	
	private String question, answer, header;
	private ArrayList<String> options = new ArrayList<>();
	
	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}
	
	public Question(){
		
	}
	
	public Question(String question, String answer, String header, String... options){
		this.question = question;
		this.answer  = answer;
		this.header = header;
		if (options.length > 0){
			for (int i = 0; i < options.length; i++){
				this.getOptions().add(options[i]);
			}
		}
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

	public ArrayList<String> getOptions() {
		return options;
	}

	public void setOptions(String option) {
		this.options.add(option);
	}
	
}
