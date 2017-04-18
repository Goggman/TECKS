package javaClientCode;
import java.net.*;
import java.time.LocalTime;
import java.util.Scanner;
import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
public class ServerClient {
	
	Socket echoSocket;
    PrintWriter out;
    BufferedReader in;
    MessageReceiver receiver;
    Queue<String> LoginWindow;
    Queue<String> ChatWindow;
    Queue<String> CreateQWindow;
    Queue<String> CreateQWindowSubjects;
    Queue<String> QuestionWindowQuestions;
    Queue<String> QuestionWindowInfo;
    Queue<String> ChatWindowInfo;
    Queue<String> ProfileWindow;
    Queue<String> ProfileWindowStats;
    Queue<String> ProfileWindowSubjects;
    Queue<String> ProfileWindowScores;
    Queue<String> QuestionWindowSubjects;
    ServerClient(){

    	init(new String[]{"localhost", "12000"}); //

    	
    	//listen();
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
	        LoginWindow = new LinkedList<String>();
	        ChatWindow = new LinkedList<String>();
	        CreateQWindow = new LinkedList<String>();
	        CreateQWindowSubjects = new LinkedList<String>();
	        QuestionWindowQuestions = new LinkedList<String>();
	        QuestionWindowInfo = new LinkedList<String>();
	        QuestionWindowSubjects = new LinkedList<String>();
	        ChatWindowInfo = new LinkedList<String>();
	       	ProfileWindow = new LinkedList<String>();
	       	ProfileWindowStats = new LinkedList<String>();
	       	ProfileWindowSubjects = new LinkedList<String>();
	       	ProfileWindowScores = new LinkedList<String>();
	        receiver = new MessageReceiver(this);
	        receiver.start();
	        
	        System.out.println("Reay to send\n");
	    }
	    
	
	void parse(String payload){
		String response = getResponse(payload);
		if (response.equals("info")){
			parse_info(payload);
			
		}
		else if(response.equals("message")){
			parse_message(payload);
		}
		else if(response.equals("history")){
			parse_history(payload);
			
		}
		else if(response.equals("error")){
			parse_error(payload);
		}
		else if(response.equals("question")){
			parse_question(payload);
		}
		else if (response.equals("stats")){
			parse_stats(payload);
		}
		else if (response.equals("subjects")){
			parse_subjects(payload);
		}
		else if (response.equals("scores")){
			parse_scores(payload);
		}
		
		else{
			System.out.println("Bad request");
		}
	}
	String getResponse(String payload){
		return payload.split("\t")[2].split(":")[1];
	}
	String getContent(String payload){
		try{
			return payload.split("\t")[3].split(":")[1];
		}
		catch(ArrayIndexOutOfBoundsException e){
			return "";
		}
	}
	String getSender(String payload){
		return payload.split("\t")[1].split(":")[1];
	}
	String getTimestamp(String payload){
		return payload.split("\t")[0].split("timestamp:")[1];
		//timestamp:
		
	}
	void parse_subjects(String payload){
		
		ProfileWindowSubjects.add(getContent(payload));

		printPrettyMessageGeneral(payload);
	}
	void parse_scores(String payload){
		ProfileWindowScores.add(getContent(payload));
		printPrettyMessageGeneral(payload);
	}
	void parse_error(String payload){
		LoginWindow.add(payload);
		CreateQWindow.add(payload);
		QuestionWindowInfo.add(payload);
		ChatWindowInfo.add(payload);
		ProfileWindow.add(payload);
		printPrettyMessageGeneral(payload);
	}
	void parse_message(String payload){
		ChatWindow.add(getSender(payload).toUpperCase()+"\n"+getContent(payload));
		printPrettyMessageGeneral(payload);
	}
	void parse_info(String payload){
		QuestionWindowInfo.add(getContent(payload));
		LoginWindow.add(getContent(payload));
		CreateQWindow.add(getContent(payload));
		ChatWindowInfo.add(getContent(payload));
		ProfileWindow.add(getContent(payload));
		printPrettyMessageGeneral(payload);
	}
	void parse_history(String payload){
		ChatWindow.add(getSender(payload)+"\n"+getContent(payload));
		printPrettyMessageGeneral(payload);
	}
	void parse_stats(String payload){
		ProfileWindowStats.add(getContent(payload));
		try{QuestionWindowSubjects.add(getContent(payload).split("[;]")[1].trim());
			CreateQWindowSubjects.add(getContent(payload).split("[;]")[1].trim());}
		catch(ArrayIndexOutOfBoundsException e){//do nothing
			
		}
		
		printPrettyMessageGeneral(payload);
	}
	void parse_question(String payload){
		QuestionWindowQuestions.add(getContent(payload));
		printPrettyMessageGeneral(payload);
	}
	void printPrettyMessageGeneral(String payload){
		System.out.println(
				"time:\t\t"+getTimestamp(payload)+"\n"+
				"sender:\t\t"+getSender(payload)+"\n"+
				"response:\t"+getResponse(payload)+"\n"+
				"content:\t"+getContent(payload)+"\n"
				
						);
	}
	void sendMessage(String payload){
		try{
			out.println(payload);
		}
		catch (NullPointerException e){
			System.out.println("Could not send this message, try to connect");
		}
	}
	
	
	void listen(){
		Scanner scanner = new Scanner(System.in); //When scenes are properly set up, all will println to some kind of outputstream
		while(true){
			String line = scanner.nextLine();
			if (line.equals("exit")){
				System.out.println("Closing client");
				scanner.close();
				System.exit(1);
				
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
				catch (NullPointerException e){
					System.out.println("Nothing to receive");
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
	

	




}
