package javaClientCode;
import java.util.ArrayList;
import java.util.HashMap;
public class QuestionSchema {
	HashMap<Question, String> answers = new HashMap<>();
	ArrayList<Question> questions = new ArrayList<>();
	
	/**
	 * creates new question; generates empty strings to be filled in
	 * @param questionQuiz list of Question objects 
	 */
	

	
	QuestionSchema(ArrayList<ArrayList<String>> quizList){
		
		for (ArrayList<String> list : quizList){
			String header="";
			String questionText="";
			String correctAnswer="";
			String category="";
			ArrayList<String> options=new ArrayList<>();
			for (String s : list){
			
				String[] temp = s.split(":");
			
				if (temp.length < 1){
					//do nothing
				}
				else if (s.contains("Header:")){
					
					header=temp[1].trim();
				}
				else if (s.contains("q:")){
					
					questionText=temp[1].trim();
					
				}
				else if (s.contains("a:")){
					
				correctAnswer=temp[1].trim();
				}
				else if (s.contains("op:")){
				
					options.add(temp[1].trim());
				}
				else if (s.contains("c:")){
					
					category=temp[1].trim();
					//this.category = category;
				}
				}
			Question q = new Question(category, questionText, correctAnswer,header, options.toArray());
			questions.add(q);
			answers.put(q, "");
			
		}
	
	}

	QuestionSchema(ArrayList<Question> questionlist, int dummy){
		for (Question q : questionlist){
			addQuestion(q);
		}
	}
	
	
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
	
	void removeQuestion(Question q){
		questions.remove(q);
		answers.remove(q);
		
	}
	void addQuestion(Question q){
		questions.add(q);
		answers.put(q, "");
	}
	

	
}
