package javaServerCode;
import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.ArrayList;

public class ServerHost {
	HashMap data;
	String hostName;
	int portNumber;
	ServerSocket serverSocket;
	PrintWriter out;
    BufferedReader in;
    Socket clientSocket;
    Protocol protocol;
    
	ServerHost(){
		setupData();
		protocol= new Protocol(this);
		portNumber=12000;
		try{
		serverSocket = new ServerSocket(portNumber);
		}
		catch (IOException e){
			System.out.println("Error in instancing serverSocket");
		}
		System.out.println("Ready to serve");
			
	}
	void accept(){
		try{
			clientSocket = serverSocket.accept();
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		}
		catch (UnknownHostException e) {
	        System.out.println("Don't know about host " + hostName);
	    } 
	    catch (IOException e) {
	        System.out.println("Couldn't get I/O for the connection to " +hostName);
	    } 
	}
	
	void bounce(String message){
		out.println(message);
	}
	void setupData(){
		this.data= new HashMap();
		HashMap users = new HashMap();
		data.put("users", users);
		createUser(new String[]{
				"frimy", "duplex", "tdt4100", "student"
		});
		createUser(new String[]{
				"myris3", "1337", "tdt4100", "lecturer"
		});
		
		ArrayList questions = new ArrayList();
		
	}
	//username, pw, subject
	void createUser(String[] userArgs){
		HashMap userMap = (HashMap) data.get("users");
		HashMap data = new HashMap();
		data.put("pw", userArgs[1]);
		data.put("subject", userArgs[2]);
		data.put("type", userArgs[3]);
		userMap.put(userArgs[0],data );
		
	}
	void createQuestion(String qArgs){
		ArrayList q = (ArrayList) data.get("questions");
		q.add(qArgs.toString());
	}
	
	void listen(){
		while(true){
			try {
			String rawText = in.readLine();
			String[] textArray = protocol.processInput(rawText);
			System.out.println("Raw text: "+rawText);
			String text = textArray[0]+"\t"+textArray[1];
			System.out.println("Received from protocol: "+text);
			bounce(text);
			}
			catch(IOException ee){
				System.out.println("Error in listen method, tried printing from in");
			}
		}
		
	}
	
	public static void main(String[] args) {
		ServerHost server = new ServerHost();
		
		server.accept();
		server.listen();
		
	}
	
	

}
