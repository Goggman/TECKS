package testCode;

import javaClientCode.Question;
import junit.framework.TestCase;

public class QuestionTest extends TestCase{

	Question q;
	
	public void setUp(){
		q  = new Question(" test", " test", " test", " TEST");
		
	}
	
	public void test(){
		assertEquals(" test", q.getCorrectAnswer());
		assertEquals(" test", q.getCategory());
		assertEquals(" test", q.getQuestionText());
		assertEquals(" TEST", q.getHeader());
	}
}
