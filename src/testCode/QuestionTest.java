package testCode;

import javaClientCode.Question;
import junit.framework.TestCase;

public class QuestionTest extends TestCase{

	Question q;
	Question q2;
	
	public void setUp(){
		q  = new Question(" test", " test", " test", " TEST");
		q2  = new Question(" test", " test", " test", " TEST");
		
	}
	
	public void test(){
		assertEquals(" test", q.getCorrectAnswer());
		assertEquals(" test", q.getCategory());
		assertEquals(" test", q.getQuestionText());
		assertEquals(" TEST", q.getHeader());
		assertFalse(" test".equals(q.getHeader()));
		
		assertEquals(" test", q2.getCorrectAnswer());
		assertEquals(" test", q2.getCategory());
		assertEquals(" test", q2.getQuestionText());
		assertEquals(" TEST", q2.getHeader());
		assertFalse(" test".equals(q2.getHeader()));
		
	}
}
