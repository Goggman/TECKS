package javaServerCode;
import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.time.LocalTime;
public class ServerHost {
	
	//Message format from client/server
	//request:
	//content:
	//request:<>\tcontent:<>
	//
	//timestamp:
	//sender:
	//response:
	//content:
	//timestamp:<>\tsender:<>\tresponse:<>\tcontent:<>
	
	
	
	//userdata
	//-> fag -> delemner i fag, -> total score for faget
	//U> delemner i fag -> score for hvert delemne
	//-> type bruker->student/lærer/admin
	//->
	HashMap<String, HashMap> properties;
	int portNumber;
	ServerSocket serverSocket;
    
    
	ServerHost(){
		portNumber=12000;
		setProperties();
		try{
		serverSocket = new ServerSocket(portNumber);
		}
		catch (IOException e){
			System.out.println("Error in instancing serverSocket");
		}
		
		System.out.println("Ready to serve");
			
	}
	void setProperties(){
		properties=new HashMap<String,HashMap>();
		properties.put("connected_clients", new HashMap<ClientHandler, String>());
		properties.put("users", new HashMap<String, HashMap<String, String>>());
		properties.put("chatrooms", new HashMap<String, ArrayList<String>>());
		properties.put("subjects", new HashMap<String, HashMap<String, ArrayList<String>>>());
		properties.get("subjects").put("tdt4100",new HashMap<String, ArrayList<String>>());
	}

	void serve_forever(){
		while(true){
			
			try {
				ClientHandler handler = new ClientHandler(this, serverSocket.accept());
				handler.start();
				properties.get("connected_clients").put(handler, "");
				
			} catch (IOException e) {
				System.out.println("Could not connect in serve forever");
				break;
			}
			
		}
		
	}
	
	public static void main(String[] args) {
		ServerHost server = new ServerHost();
		server.serve_forever();
	}
	
	public class ClientHandler implements Runnable{
		private Thread t;
		private ServerHost server;
		private Socket clientSocket;
		private PrintWriter out;
	    private BufferedReader in;
	    private String username;
	    
	    
		ClientHandler(ServerHost server, Socket clientSocket){
			this.server=server;
			this.clientSocket=clientSocket;
			try{
				this.out = new PrintWriter(this.clientSocket.getOutputStream(), true);
				this.in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
			}
			catch (IOException e) {
		        System.out.println("Couldn't get I/O for the connection");
		    } 
			
		}
		void parse(String payload){
			if (get_request(payload).equals("login")){
				parse_login(payload);
				
			}
			else if(get_request(payload).equals("logout")){
				parse_logout(payload);
			}
			else if(get_request(payload).equals("msg")){
				parse_msg(payload);
				
			}
			else if(get_request(payload).equals("help")){
				parse_help(payload);
			}
			else if(get_request(payload).equals("get<property>")){ //getters for all user data
				//create all parse_get<> to
			}
			else if(get_request(payload).equals("getquestions")){//getquestions <subject>
				
			}
			else if(get_request(payload).equals("createquestions")){//createquestion <question in defined string format>
				
			}
			else if(get_request(payload).equals("set<property>")){ // setters for all different types of things one want to change with a user, type, subjects,
				//create all parse_set<> to
			}
			else{
				this.out.println("timestamp:"+LocalTime.now().toString()+"\nsender:server\nresponse:Invalid request\ncontent:null");
			}
			
		}
		
		String get_request(String payload){
			return payload.split("\t")[0].split(":")[1];
		}
		String get_content(String payload){
			return payload.split("\t")[1].split(":")[1];
		}
		
		
		
		void parse_login(String payload){
			String data= get_content(payload);
			if (this.username == null){
				
			}
			this.username = data;
			HashMap prop = (HashMap)server.properties.get("users");
			prop.put(data,new HashMap());
			
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:info\t"
					+ "content:Login successful as "+this.username;
			String returnToClients = "timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:info\t"
					+ "content:"+this.username+" logged in";
				
			
			ArrayList chat= (ArrayList)server.properties.get("chat");
			chat.add(returnToClients);
			out.println(returnToClient);
		}
		void parse_logout(String payload){
			
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:info\t"
					+ "content:Logout successful as "+this.username;
			String returnToClients = "timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:info\t"
					+ "content:"+this.username+" logged out";
			
			ArrayList chat= (ArrayList)server.properties.get("chat");
			chat.add(returnToClients);
			out.println(returnToClient);
			this.username=null;
		}
		
		void parse_msg(String payload){
			
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
									+"\tsender:server\t"
									+ "response:info\t"
									+ "content:Message sent";
			
									
			String returnToClients = "timestamp:"+LocalTime.now().toString()
									+"\tsender:"+this.username+"server\t"
									+ "response:info\t"
									+ "content:"+get_content(payload);
			
			ArrayList chat= (ArrayList)server.properties.get("chat");
			chat.add(returnToClients);
			out.println(returnToClient);
			}
			
			
		void parse_help(String payload){
			
		}
		public void run(){
			while (true){
				try{
					String payload = in.readLine();
					System.out.println(payload);
					parse(payload);
				}
				catch(IOException ee){
					System.out.println("Client disconnected");
					break;
				}
					
			}
		}
		void start(){
			if (this.t==null){
				this.t= new Thread(this);
				t.setDaemon(true);
				t.start();
			}
		}
		
	}

}
