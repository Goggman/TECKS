package javaClientCode;
import java.net.*;
import java.io.*;
public class ServerCom {
	String localIP="10.22.40.214";
	
	Socket echoSocket;
    PrintWriter out;
    BufferedReader in;
    
    ServerCom(){
    	init(new String[]{localIP, "12000"});
    	
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
    	System.out.println("echo: " + in.readLine());
		}
		catch (IOException e) {
            System.out.println("Couldn't get I/O for the connection <hostname>");
        }  
	}
	
	void request(){//Fetch user info and status on login maybe
		
	}
	public static void main(String[] args) {
		
	}
}
