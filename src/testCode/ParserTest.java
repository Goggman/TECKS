package testCode;

import java.io.IOException;
import java.util.ArrayList;

import javaClientCode.CustomParser;
import javaClientCode.Question;
import junit.framework.TestCase;

public class ParserTest extends TestCase{

	CustomParser cp;
	Question question;
	ArrayList<String> qString;
	
	protected void setUp(){
		CustomParser cp = new CustomParser();
		qString = new ArrayList<>();
		qString.add("Header: TEST");
		qString.add("c: test");
		qString.add("q: test");
		qString.add("a: test");
		qString.add("op: test2");
		qString.add("op: test3");
		question = cp.createQuestion(qString);
	}
	
	public void test_createQ(){
		assertEquals(" test", question.getCorrectAnswer());
		assertEquals(" test", question.getCategory());
		assertEquals(" test", question.getQuestionText());
		assertEquals(" TEST", question.getHeader());
		
		
		
	}
	
	public void testOpenFile(){
		try{ 
		ArrayList<ArrayList<String>> questionArray = cp.OpenFile(System.getProperty("user.dir")+"/test.txt");
		
		}
		catch(Exception e){
			assertEquals(IOException.class, e.getClass());
		}
	}
	
	/*
	public void testIOException(){
		try {
			cp.OpenFile(System.getProperty("user.dir") + "/exception.txt");
			fail();
			
		}
		catch (Exception e){
			System.out.println(System.getProperty("user.dir"));
			System.out.println(e.getClass().toString());
			assertTrue(e.getClass().equals(IOException.class));
		}
	}*/
	
	
	
	
	
	
	/*public void test(){
		assertTrue(true);
	}*/
}
