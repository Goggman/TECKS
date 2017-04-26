package testCode;

import java.util.ArrayList;

import javaClientCode.Question;
import javaClientCode.QuestionSchema;
import javaClientCode.QuestionWindow;

public class QuestionWindowTest extends junit.framework.TestCase{

	QuestionWindow qw;
	QuestionSchema qs;
	
	public void setUp(){
		qw = new QuestionWindow();
		ArrayList<ArrayList<String>> quiz = new ArrayList<>();
		ArrayList<String> question = new ArrayList<>();
		question.add("Header: TEST");
		question.add("c: test");
		question.add("q: test");
		question.add("a: test");
		quiz.add(question);
		
		qs = new QuestionSchema(quiz);
		qw.addSchema(qs);
	}
	
	public void testCreateQ(){
		QuestionSchema questions = qw.getSchema();
		
		ArrayList<Question> quiz = questions.getQuestions();
		Question testQ = new Question("test", "test", "test", "TEST");
		assertEquals(testQ.getHeader(), quiz.get(0).getHeader());
		assertEquals(testQ.getCorrectAnswer(), quiz.get(0).getCorrectAnswer());
		assertEquals(testQ.getCategory(), quiz.get(0).getCategory());
		assertEquals(testQ.getQuestionText(), quiz.get(0).getQuestionText());
		
		Question question = new Question("test", "test", "test", "TEST");
		questions.addQuestion(question);
		assertEquals(testQ.getHeader(), quiz.get(1).getHeader());
		assertEquals(testQ.getCorrectAnswer(), quiz.get(1).getCorrectAnswer());
		assertEquals(testQ.getCategory(), quiz.get(1).getCategory());
		assertEquals(testQ.getQuestionText(), quiz.get(1).getQuestionText());
		
		assertTrue(questions.getAnswers().get(question).isEmpty());
		
		questions.removeQuestion(question);
		assertTrue(questions.getQuestions().size()==1);
		assertFalse(questions.getQuestions().contains(question));
		assertFalse(questions.getAnswers().keySet().contains(question));
	}
}
