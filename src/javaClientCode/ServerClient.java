package javaClientCode;
import java.net.*;
import java.time.LocalTime;
import java.util.Scanner;
import java.io.*;


public class ServerClient {
	Socket echoSocket;
    PrintWriter out;
    BufferedReader in;
    MessageReceiver receiver;
    
    ServerClient(){
    	//System.out.println(System.getProperty("user.dir"));
    	init(new String[]{"localhost", "12000"});
    	
    	listen();
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
	        receiver = new MessageReceiver(this);
	        receiver.start();
	        
	        System.out.println("Reay to send\n");
	    }
	    
	
	void parse(String payload){
		if (get_response(payload).equals("info")){
			parse_info(payload);
			
		}
		else if(get_response(payload).equals("message")){
			parse_message(payload);
		}
		else if(get_response(payload).equals("history")){
			parse_history(payload);
			
		}
		else if(get_response(payload).equals("error")){
			parse_error(payload);
		}
		else if(get_response(payload).equals("question")){
			parse_question(payload);
		}
		else{
			System.out.println("Bad request");
		}
	}
	String get_response(String payload){
		return payload.split("\t")[2].split(":")[1];
	}
	String get_content(String payload){
		try{
			return payload.split("\t")[3].split(":")[1];
		}
		catch(ArrayIndexOutOfBoundsException e){
			return "";
		}
	}
	String get_sender(String payload){
		return payload.split("\t")[1].split(":")[1];
	}
	String get_timestamp(String payload){
		return payload.split("\t")[0].split("timestamp:")[1];
		//timestamp:
		
	}
	void parse_error(String payload){
		printPrettyMessageGeneral(payload);
	}
	void parse_message(String payload){
		printPrettyMessageGeneral(payload);
	}
	void parse_info(String payload){
		printPrettyMessageGeneral(payload);
	}
	void parse_history(String payload){
		printPrettyMessageGeneral(payload);
	}
	
	void parse_question(String payload){
		printPrettyMessageGeneral(payload);
	}
	void printPrettyMessageGeneral(String payload){
		System.out.println(
				"time:\t\t"+get_timestamp(payload)+"\n"+
				"sender:\t\t"+get_sender(payload)+"\n"+
				"response:\t"+get_response(payload)+"\n"+
				"content:\t"+get_content(payload)+"\n"
				
						);
	}
	void listen(){
		Scanner scanner = new Scanner(System.in); //When scenes are properly set up, all will println to some kind of outputstream
		while(true){
			String line = scanner.nextLine();
			if (line.equals("exit")){
				System.out.println("Closing client");
				scanner.close();
				break;
			}
			else if (line.equals("connect")){
				
				init(new String[]{"localhost","12000"});
				continue;
			}
			String[] user_input=line.split(" ");
			String req=user_input[0];
			String con = "";
			for(int x=1;x<user_input.length;x++){
				con=con+user_input[x];
				if (x<user_input.length-1){
					con=con+" ";
				}
			}
			String payload="request:"+req+"\tcontent:"+con;
			out.println(payload);
		}
	}
	
	class MessageReceiver implements Runnable{   //Multithreading the sending and responses
		Thread t;
		ServerClient client;
		MessageReceiver(ServerClient client){
			this.client=client;
		}
		public void run(){
			while (true){
				try{
					String payload = client.in.readLine();
					
					client.parse(payload);
				}
				catch(IOException ee){
					System.out.println("Server disconnected");
					break;
					
					
				}
					
			}
		}
		public void start(){
			if (this.t==null){
				this.t= new Thread(this);
				t.setDaemon(true);
				t.start();
			}
		}
	}
	
	
	public static void main(String[] args) {
		ServerClient sender = new ServerClient();
		
		
	}
}
