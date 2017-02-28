package javaServerCode;
import java.net.*;
import java.io.*;


public class Server {
	String hostName;
	int portNumber;
	Socket socket;
	PrintWriter out;
    BufferedReader in;
    
	Server(){
		try{
			this.socket = new Socket("", portNumber);
			this.out =new PrintWriter(socket.getOutputStream(), true);
            this.in =new BufferedReader(new InputStreamReader(socket.getInputStream()));
			}
			catch (UnknownHostException e) {
		        System.out.println("Don't know about host " + hostName);
		    } 
		    catch (IOException e) {
		        System.out.println("Couldn't get I/O for the connection to " +hostName);
		    } 
		
	}
	
	public static void main(String[] args) {
		Server server = new Server();
		while(true){
			try {
			System.out.println(""+server.in.readLine());
			}
			catch(Exception ee){
				try {
				    Thread.sleep(1000);                 //1000 milliseconds is one second.
				} catch(InterruptedException ex) {
				    Thread.currentThread().interrupt();
				}
				System.out.println("No input to print");
			}
		}
		
	}
	
	

}
