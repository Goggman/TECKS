package javaClientCode;
import java.net.*;
import java.util.Scanner;
import java.io.*;
public class ServerClient {
	String localIP="10.22.41.239"; //Will change frequently
	Socket echoSocket;
    PrintWriter out;
    BufferedReader in;
    
    ServerClient(){
    	init(new String[]{"localhost", "12000"});
    	System.out.println("Reay to send");
    }
	    void init(String[] args) {
	        
	        if (args.length != 2) {
	            System.out.println("Usage: java EchoClient <host name> <port number>");}

	        String hostName = args[0];
	        int portNumber = Integer.parseInt(args[1]);

	        try {
	        	this.echoSocket = new Socket(hostName, portNumber);
	            this.out =new PrintWriter(echoSocket.getOutputStream(), true);
	            this.in =new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
	            
	        	
	            
	        } 
	        catch (UnknownHostException e) {
	            System.out.println("Don't know about host " + hostName);
	        } 
	        catch (IOException e) {
	            System.out.println("Couldn't get I/O for the connection to " +hostName);
	        } 
	    }
	
	void send(String message){//Should send user specific data and updates into socket towards server
		try{
		out.println(message);
    	
		}
		catch (Exception e) {
            System.out.println("Something happened during excecution of send");
        }  
	}
	
	void receive(){//Fetch user info and status on login maybe
		try{
			System.out.println("Received message: " + in.readLine());
			
		}
		catch (IOException ae){
			System.out.println("Couldn't get I/O for the connection to host");
		}
	}
	public static void main(String[] args) {
		ServerClient sender = new ServerClient();
		Scanner scanner = new Scanner(System.in);
		while(true){
			String input = scanner.nextLine();
			sender.send(""+input);
			sender.receive();
		}
		
	}
}
