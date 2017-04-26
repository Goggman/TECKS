package testCode;

import javaServerCode.PropertiesHandler;

public class PropertiesHandlerTest extends junit.framework.TestCase{

	PropertiesHandler ph;
	
	public void setUp(){
		ph = new PropertiesHandler();
	}
	
	public void testCreateQ(){
		assertTrue(ph.getPath().endsWith("serverSave.ser"));
		
	}
}
