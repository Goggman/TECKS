package testCode;

import java.io.IOException;
import java.net.Socket;
import javaClientCode.ServerClient;
import javaServerCode.ClientHandler;
import javaServerCode.ServerHost;
import junit.framework.TestCase;

public class ClientHandlerTest extends TestCase{

	ClientHandler cHandler;
	ServerClient client;
	ServerHost sh;
	public void setUp(){
		sh = new ServerHost();
		cHandler = new ClientHandler();
		
		try {
			client = new ServerClient();
			cHandler = new ClientHandler(sh, sh.serverSocket.accept());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		;
		client.sendMessage("request:help\tcontent:");
		cHandler.setCurrentSubject("test");
		cHandler.setUsername("test");
		cHandler.setChat("test");
		cHandler.setUserType("test");
		client.sendMessage("request:create_subject\tcontent:TEST");
	}
	
	public void tearDown(){
		try {
			sh.serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void test(){
		assertTrue(true);
	}
	
	public void testProperties(){
		assertEquals("test", cHandler.getCurrentSubject());
		assertEquals("test", cHandler.getUsername());
		assertEquals("test", cHandler.getChat());
		assertEquals("test", cHandler.getUserType());
		
		
	}
	
	
	public void testParsers() throws InterruptedException{
		client.sendMessage("request:create_subject\tcontent:TEST");
		assertEquals(null, cHandler.getReturnToClient());
	}
	
	
}

	