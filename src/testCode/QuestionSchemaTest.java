package testCode;

import java.util.ArrayList;

import javaClientCode.Question;
import javaClientCode.QuestionSchema;

public class QuestionSchemaTest extends junit.framework.TestCase{

	QuestionSchema qs;
	public void setUp(){
		ArrayList<ArrayList<String>> quiz = new ArrayList<>();
		ArrayList<String> question = new ArrayList<>();
		question.add("Header: TEST");
		question.add("c: test");
		question.add("q: test");
		question.add("a: test");
		quiz.add(question);
		
		qs = new QuestionSchema(quiz);
	}
	
	public void testCreateQ(){
		ArrayList<Question> quiz = qs.getQuestions();
		Question testQ = new Question("test", "test", "test", "TEST");
		assertEquals(testQ.getHeader(), quiz.get(0).getHeader());
		assertEquals(testQ.getCorrectAnswer(), quiz.get(0).getCorrectAnswer());
		assertEquals(testQ.getCategory(), quiz.get(0).getCategory());
		assertEquals(testQ.getQuestionText(), quiz.get(0).getQuestionText());
		
		Question question = new Question("test", "test", "test", "TEST");
		qs.addQuestion(question);
		assertEquals(testQ.getHeader(), quiz.get(1).getHeader());
		assertEquals(testQ.getCorrectAnswer(), quiz.get(1).getCorrectAnswer());
		assertEquals(testQ.getCategory(), quiz.get(1).getCategory());
		assertEquals(testQ.getQuestionText(), quiz.get(1).getQuestionText());
		
		assertTrue(qs.getAnswers().get(question).isEmpty());
		
		qs.removeQuestion(question);
		assertTrue(qs.getQuestions().size()==1);
		assertFalse(qs.getQuestions().contains(question));
		assertFalse(qs.getAnswers().keySet().contains(question));
		
		
		
	}
}
