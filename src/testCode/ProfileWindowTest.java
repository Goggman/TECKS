package testCode;

import javaClientCode.ProfileWindow;

public class ProfileWindowTest extends junit.framework.TestCase{

	ProfileWindow pw;
	
	
	public void setUp(){
		pw = new ProfileWindow();
		
	}
	
	public void testCreateQ(){
		assertTrue(pw.getAddSubjectNeedsUpdate());
		assertTrue(pw.getSubjectNeedsUpdate());
	}
	
	
}
