package testCode;

import javaClientCode.Analyzer;
import junit.framework.TestCase;


public class AnalyzerTest extends TestCase{
	
	
	/**
	 * tests the init_score() method
	 */
	public void test_init(){
		Analyzer an = new Analyzer();
		assertTrue(an.getCategories().isEmpty());
		assertTrue("0@".equals(an.prepareContent()));
		
	}
	
	/**
	 * tests the registerAnswer() method
	 */
	public void test_register(){
		Analyzer an = new Analyzer();
		//test if adding new subject works
			//test for correct answer
		an.registerAnswer("test", true);
		assertEquals(2, an.getScoreAndQuestions("test").length);
		int[] expected = new int[]{1, 1};
		assertEquals(expected.length, an.getScoreAndQuestions("test").length);
		assertEquals(expected[0], an.getScoreAndQuestions("test")[0]);
		assertEquals(expected[1], an.getScoreAndQuestions("test")[1]);
			//test for incorrect answer
		an.registerAnswer("test2", false);
		expected = new int[]{0, 1};
		assertEquals(expected.length, an.getScoreAndQuestions("test2").length);
		assertEquals(expected[0], an.getScoreAndQuestions("test2")[0]);
		assertEquals(expected[1], an.getScoreAndQuestions("test2")[1]);
		//test if overwriting old subject works
			//test for correct answer
		an.registerAnswer("test", true);
		expected = new int[]{2, 2};
		assertEquals(expected.length, an.getScoreAndQuestions("test").length);
		assertEquals(expected[0], an.getScoreAndQuestions("test")[0]);
		assertEquals(expected[1], an.getScoreAndQuestions("test")[1]);
			//test for incorrect answer
		an.registerAnswer("test2", false);
		expected = new int[]{0, 2};
		assertEquals(expected.length, an.getScoreAndQuestions("test2").length);
		assertEquals(expected[0], an.getScoreAndQuestions("test2")[0]);
		assertEquals(expected[1], an.getScoreAndQuestions("test2")[1]);
	}

	/**
	 * tests the prepareContent() method
	 */
	public void test_prepare(){
		Analyzer an = new Analyzer();
		
		//check if string is correct for one category
		an.registerAnswer("test", true);
		assertEquals("1@test|1|1", an.prepareContent());
		//check if string is correct for next question
		an.registerAnswer("test", false);
		assertEquals("2@test|1|2", an.prepareContent());
		//check if string is correct for second category
		an.registerAnswer("test", true);
		assertEquals("3@test|2|3", an.prepareContent());
	}
}
