package javaServerCode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class ClientHandler implements Runnable{
	private Thread t;
	private ServerHost server;
	private Socket clientSocket;
	private PrintWriter out;
    private BufferedReader in;
    
    private String username;
    private String chatroom;
    private String current_subject;
    private String userType;
    
	ClientHandler(ServerHost server, Socket clientSocket)  {
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
	
	String getRequest(String payload){						//helper functions to save space and time, get the request and content part of a message easy
		try{
			return payload.split("\t")[0].split(":")[1];
			}
			catch(ArrayIndexOutOfBoundsException e){
				return "";
			}
	}
	String getContent(String payload){
		try{
		return payload.split("\t")[1].split(":")[1];
		}
		catch(ArrayIndexOutOfBoundsException e){
			return "";
		}
	}
	String getChat(){
		return chatroom;
	}
	void setChat(String chatname){
		chatroom=chatname;
	}
	PrintWriter getOut(){
		return out;
	}
	
	String getUsername(){
		return username;
	}
	void setUsername(String name){
		this.username = name;
	}
	String getCurrentSubject(){
		return current_subject;
	}
	void setCurrentSubject(String subject){
		this.current_subject=subject;
	}
	boolean saveProperties(){
		try{
			server.prop.saveProperties(server.getProperties());
			return true;
		}
		catch(Exception e){
			//e.printStackTrace();
			//System.out.println("");
			return false;
		}
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
				String returnToClients = "timestamp:"+LocalTime.now().toString()
						+"\tsender:server\t"
						+ "response:info\t"
						+ "content:"+this.username+" logged out";
				((HashMap)server.getProperties().get("connected_clients")).remove(this); //remove oneself from connected clients 
				if (getChat()!=null){
				ArrayList chat= (ArrayList)((HashMap)server.getProperties().get("chatrooms").get(getChat())).get("log");  //add message to chat
				chat.add(returnToClients);
				HashMap users= (HashMap)((HashMap)server.getProperties().get("chatrooms").get(getChat())).get("users"); //remove oneself from chatroom
				users.remove(this);	 								
				Iterator clients = ((HashMap)((HashMap)server.getProperties().get("chatrooms").get(getChat())).get("users")).keySet().iterator();
				while(clients.hasNext()){
					((ClientHandler)clients.next()).getOut().println(returnToClients);
				}
				setUsername(null);
				}
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
	
	String get_sender(String payload){
		return payload.split("\t")[1].split(":")[1];
	}
	String getUserType(){
		return this.userType;
	}
	void setUserType(String type){
		this.userType = type;
	}
	String get_content(String payload){
		try{
			return payload.split("\t")[3].split(":")[1];
		}
		catch(ArrayIndexOutOfBoundsException e){
			return "";
		}
	}
	String get_timestamp(String payload){
		return payload.split("\t")[0].split("timestamp:")[1];
		//timestamp:
		
	}
	///////////////////////////////////////////////////////////////////////////////////////////// PARSE METHODS BELOW
	
	////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	void parse(String payload){
		String request = getRequest(payload);
		
		if (request.equals("login")){
			parse_login(payload);
			
		}
		else if(request.equals("logout")){
			parse_logout(payload);
		}
		else if(request.equals("msg")){
			parse_msg(payload);
			
		}
		else if(request.equals("help")){
			parse_help(payload);
		}
		//else if(get_request(payload).equals("get<property>")){ //getters for all user data
		//}
		else if(request.equals("get_subjects")){ //getsubjects <None>
			parse_get_subjects(payload);
		}
		else if(request.equals("add_subject")){ //addsjubject <subject>, adds a subject from one of the existing ones
			parse_add_subject(payload);
		}
		else if(request.equals("remove_subject")){ //removesubjects <subject>
			parse_remove_subject(payload);
		}
		else if(request.equals("history")){ //get chatlog in current chatroom, history <null>
			parse_history(payload);
		}
		else if(request.equals("get_questions")){//getquestions <subject>
			parse_get_questions(payload);
		}
		else if(request.equals("set_subject")){
			parse_set_subject(payload);
		}
		else if(request.equals("create_subject")){ //getsubjects <None>, creates a new subject
			parse_create_subject(payload);
		}
		else if(request.equals("add_question")){//addquestion <question in defined string format> into current suject
			parse_add_question(payload);
		}
		else if(request.equals("set_chatroom")){
			parse_set_chatroom(payload);
		}
		else if(request.equals("create_chatroom")){
			parse_create_chatroom(payload);
		} 
		else if(request.equals("get_chatrooms")){
			parse_get_chatrooms(payload);
		}
		else if(request.equals("save_server")){
			parse_save_server(payload);
		}
		else if(request.equals("get_username")){
			parse_get_username(payload);
		}
		else if(request.equals("get_chatroom")){
			parse_get_chatroom(payload);
		}
		else if(request.equals("get_subject")){
			parse_get_subject(payload);
		}

		else if (request.equals("get_questions")){

			parse_get_questions(payload);
		}
		else if (request.equals("get_best_questions")){
			parse_get_best_questions(payload);
		}
		else if (request.equals("get_stats")){
			parse_get_stats(payload);
		}
		else if (request.equals("add_results")){
			parse_add_results(payload);
		}
		else if (request.equals("reset_score")){
			parse_reset_score(payload);
		}
		else if (request.equals("get_names")){
			parse_get_names(payload);
		}
		else if (request.equals("get_type")){
			parse_get_type(payload);
		}
		else{
			this.out.println("timestamp:"+LocalTime.now().toString()+"\tsender:server\tresponse:error\tcontent:Not a valid command");
		}
		
	}
	
	
	
	
	void parse_login(String payload){
		if (getContent(payload).split("@").length<2){
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:you need to provide correct arguments for login, format is <username>@<PW>@<type> if new user, <username>@<PW> if already created user";
			out.println(returnToClient);
			return;
		}
		String username= getContent(payload).split("@")[0];
		String password= getContent(payload).split("@")[1];
		
		if (username.equals("")){
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:you need to provide username for login, format is <username>@<PW>@<type>";
			out.println(returnToClient);
			return;
		}
		if (getUsername() != null){											//check if already given username
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:Already logged in";
			out.println(returnToClient);
			return;
			
		}

		
		
		Iterator userIterator= (Iterator)server.getProperties().get("connected_clients").keySet().iterator();// check if username already logged in
		try{
			while (userIterator.hasNext()){
				if (((ClientHandler)userIterator.next()).getUsername().equals(username)){
					String returnToClient= 	"timestamp:"+LocalTime.now().toString()
							+"\tsender:server\t"
							+ "response:error\t"
							+ "content:Username already logged in";
					out.println(returnToClient);
					return;
				}
			}
		}
		catch(NullPointerException e){
			//System.out.println("Null pointer in connected_clients, no one added yet");
		}
		

		
		HashMap users = (HashMap)server.getProperties().get("users");//set up the user properties, add the user to user etc
		
		
		if(users.containsKey(username)){					//Check if already create user, check password
			if (!password.equals(((HashMap)users.get(username)).get("password"))){
				String returnToClient = "timestamp:"+LocalTime.now().toString()
						+"\tsender:server\t"
						+ "response:info\t"
						+ "content:Invalid password or username";
				out.println(returnToClient);
				return;
			}
			
			setUserType((String)((HashMap)server.getProperties().get("users").get(username)).get("type"));
		}
		else {
			if (getContent(payload).split("@").length<3){
				String returnToClient= 	"timestamp:"+LocalTime.now().toString()
						+"\tsender:server\t"
						+ "response:error\t"
						+ "content:you need to provide correct arguments for login, format is <username>@<PW>@<type> if new user, <username>@<PW> if already created user";
				out.println(returnToClient);
				return;
			}
			String userType= getContent(payload).split("@")[2]; // "admin" for admin, "student" for student, "lecturer" for lecturer
			if (userType.equals("") || !(userType.equals("admin")||userType.equals("student")||userType.equals("lecturer"))){
				String returnToClient= 	"timestamp:"+LocalTime.now().toString()
						+"\tsender:server\t"
						+ "response:error\t"
						+ "content:You need to supply a usertype, <username>@<PW>@<type>, either admin, student, lecturer";
				out.println(returnToClient);
				return;
			}
			users.put(username,new HashMap());
			((HashMap)users.get(username)).put("password",password);
			((HashMap)users.get(username)).put("type",userType);
			setUserType(userType);
		}
		
		if (getChat() == null){
			setChat("start");
		}
		
		HashMap userdata = (HashMap)server.getProperties().get("users").get(username);
		if (!userdata.containsKey("subjects")){
			userdata.put("subjects", new HashMap<String,HashMap>());
		}
		setUsername(username);
		
		String returnToClient= 	"timestamp:"+LocalTime.now().toString()
				+"\tsender:server\t"
				+ "response:info\t"
				+ "content:OK - Login successful as "+this.username+" as "+getUserType()+" into chatroom - "+getChat();
		String returnToClients = "timestamp:"+LocalTime.now().toString()
				+"\tsender:server\t"
				+ "response:message\t"
				+ "content:"+this.username+" logged in";
		
		HashMap chatUsers= (HashMap)((HashMap)server.getProperties().get("chatrooms").get(getChat())).get("users");
		chatUsers.put(this, new HashMap());																	//add user to chat
		
		Iterator clients = ((HashMap)((HashMap)server.getProperties().get("chatrooms").get(getChat())).get("users")).keySet().iterator();  				//send message to every client in chat
		while(clients.hasNext()){
			ClientHandler next = (ClientHandler)clients.next();
			if (!next.equals(this)){
				next.getOut().println(returnToClients);
			}
			
		}
		ArrayList chat= (ArrayList) ((HashMap) server.getProperties().get("chatrooms").get(getChat())).get("log");			//add login notification to chat
		chat.add(returnToClients);
		out.println(returnToClient);																//send receipt
		server.getProperties().get("connected_clients").put(this, new HashMap());
		
	}
	void parse_logout(String payload){
		if (getChat()==null){
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:Need to log in to log out";
			out.println(returnToClient);
			return;
		}
		String returnToClient= 	"timestamp:"+LocalTime.now().toString()
				+"\tsender:server\t"
				+ "response:info\t"
				+ "content:Logout successful";
		String returnToClients = "timestamp:"+LocalTime.now().toString()
				+"\tsender:server\t"
				+ "response:message\t"
				+ "content:"+this.username+" logged out";
		
		ArrayList chat= (ArrayList)((HashMap)server.getProperties().get("chatrooms").get(getChat())).get("log");  //add message to chat
		chat.add(returnToClients);
		HashMap users= (HashMap)((HashMap)server.getProperties().get("chatrooms").get(getChat())).get("users"); //remove oneself from chatroom
		users.remove(this);
		HashMap connectedClients = server.getProperties().get("connected_clients");
		connectedClients.remove(this);
		((HashMap)server.getProperties().get("connected_clients")).remove(this); 								 //remove oneself from connected clients 
		Iterator clients = ((HashMap)((HashMap)server.getProperties().get("chatrooms").get(getChat())).get("users")).keySet().iterator();
		while(clients.hasNext()){
			((ClientHandler)clients.next()).getOut().println(returnToClients);
		}
		out.println(returnToClient);
		
		setUsername(null);
		setChat(null);
		setUserType(null);
		setCurrentSubject(null);
	}
	void parse_msg(String payload){
		if (getUsername()==null || getChat()==null){
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:You need to log in to send messages";
			out.println(returnToClient);
			return;
		}
		String returnToClient= 	"timestamp:"+LocalTime.now().toString()
								+"\tsender:server\t"
								+ "response:info\t"
								+ "content:Message sent";
		
								
		String returnToClients = "timestamp:"+LocalTime.now().toString()
								+"\tsender:"+getUsername()+"\t"
								+ "response:message\t"
								+ "content:"+getContent(payload);
		
		ArrayList chat= (ArrayList) ((HashMap)server.properties.get("chatrooms").get(getChat())).get("log");  //Holy shit casting is annoying
		chat.add(returnToClients);
		Iterator clients = ((HashMap)((HashMap)server.getProperties().get("chatrooms").get(getChat())).get("users")).keySet().iterator();
		while(clients.hasNext()){
			ClientHandler next = (ClientHandler)clients.next();
			if (!next.equals(this)){
				next.getOut().println(returnToClients);
			}
			
		}
		out.println(returnToClient);
		}
	void parse_history(String payload){
		if (this.username==null || this.chatroom==null){
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:You need to log in to see chat logs";
			out.println(returnToClient);
			return;
		}
		ArrayList chatlog = (ArrayList)((HashMap)server.getProperties().get("chatrooms").get(this.chatroom)).get("log"); // print every message received so far
		for(int x=0;x<chatlog.size();x++){
			String returnToClient= 	"timestamp:"+get_timestamp((String)chatlog.get(x))+"\t"
					+"sender:"+get_sender((String)chatlog.get(x))+"\t"
					+ "response:history\t"
					+ "content:"+get_content((String)chatlog.get(x));
			out.println(returnToClient);
		}
		
	}
	void parse_create_subject(String payload){
		if(getUsername()==null){
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:You need to log in to use this function";
			out.println(returnToClient);
			return;
		}
		if(getUserType().equals("student")){
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:Students are not allowed to utilize this function";
			out.println(returnToClient);
			return;
		}
		if (server.getProperties().get("subjects").containsKey(getContent(payload))){
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:Subject already exists";
			out.println(returnToClient);
		}
		else{
			server.getProperties().get("subjects").put(getContent(payload), new HashMap());
			((HashMap)server.getProperties().get("subjects").get(getContent(payload))).put("members", new ArrayList<String>());
			((HashMap)server.getProperties().get("subjects").get(getContent(payload))).put("questions", new ArrayList<String>());
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:info\t"
					+ "content:Subject created successfully";
			out.println(returnToClient);
		}
		
	}
	void parse_set_subject(String payload){
		if(getUsername()==null){
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:You need to log in to use this function";
			out.println(returnToClient);
			return;
		}
		if (! server.getProperties().get("subjects").containsKey(getContent(payload))){
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:Subject does not exist";
			out.println(returnToClient);
			return;
		}
		if (! ((HashMap)((HashMap)server.getProperties().get("users").get(getUsername())).get("subjects")).containsKey(getContent(payload))){
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:Subject does not exist in your registered subjects";
			out.println(returnToClient);
			return;
		}
		else{
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:info\t"
					+ "content:Current subject set successfully";
			out.println(returnToClient);
			setCurrentSubject(getContent(payload));
		}
	}
	void parse_add_subject(String payload){
		String subject = getContent(payload);
		if(getUsername()==null){
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:You need to log in to use this function";
			out.println(returnToClient);
			return;
		}
		
		boolean hasSubject = ((HashMap)((HashMap)server.getProperties().get("users").get(getUsername())).get("subjects")).containsKey(getContent(payload));
		boolean subjectExists = server.getProperties().get("subjects").containsKey(getContent(payload));
		if (hasSubject || !subjectExists ){
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:Subject does not exist, or you already have registered in the subject";
			out.println(returnToClient);
		}
		else{	//create the properties needed for a subject registered to a user
			
			HashMap subjects = (HashMap)((HashMap)server.getProperties().get("users").get(getUsername())).get("subjects");
			subjects.put(subject, new HashMap());
			((HashMap) subjects.get(subject)).put("score",(double)0);
			((HashMap) subjects.get(subject)).put("categories",new HashMap());  
			((HashMap) subjects.get(subject)).put("#questions",(double)-1);
			((ArrayList)((HashMap)server.getProperties().get("subjects").get(subject)).get("members")).add(getUsername());
			
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:info\t"
					+ "content:Subject added successfully";
			out.println(returnToClient);
		}
	}
	void parse_add_question(String payload){
		if(getUsername()==null){
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:You need to log in to use this function";
			out.println(returnToClient);
			return;
		}
		if(getCurrentSubject()==null ){
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:You need to set the working subject";
			out.println(returnToClient);
			return;
		}
		if(getUserType().equals("student")){
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:Students are not allowed to utilize this function";
			out.println(returnToClient);
			return;
		}
		
		((ArrayList)((HashMap)server.getProperties().get("subjects").get(getCurrentSubject())).get("questions")).add(getContent(payload));
		String returnToClient= 	"timestamp:"+LocalTime.now().toString()
				+"\tsender:server\t"
				+ "response:info\t"
				+ "content:Question added successfully in "+getCurrentSubject();
		out.println(returnToClient);
	}
	void parse_get_subjects(String payload){
		if(getUsername()==null){
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:You need to log in to use this function";
			out.println(returnToClient);
			return;
		}
		
		if (getContent(payload).equals("global")){
			String subjects="";
			Iterator subject_iterator = ((HashMap)server.getProperties().get("subjects")).keySet().iterator();
			while(subject_iterator.hasNext()){
				subjects+=subject_iterator.next();
				if (subject_iterator.hasNext()){
					subjects+="@";
				}
			}
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:subjects\t"
					+ "content:"+subjects;
			out.println(returnToClient);
		}
			
		else if (getContent(payload).equals("local")){
			String subjects="";
			Iterator subject_iterator = ((HashMap)((HashMap)((HashMap)server.getProperties().get("users")).get(getUsername())).get("subjects")).keySet().iterator();
			while(subject_iterator.hasNext()){
				subjects+=subject_iterator.next();
				if (subject_iterator.hasNext()){
					subjects+="@";
				}
			}
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:stats\t"
					+ "content:"+subjects;
			out.println(returnToClient);
		}
		else{
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:Must supply argument, global or local";
			out.println(returnToClient);
		}

	}
	

	
	void parse_remove_subject(String payload){

		if(getUsername()==null){
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:You need to log in to use this function";
			out.println(returnToClient);
			return;
		}
		boolean hasSubject = ((HashMap)((HashMap)server.getProperties().get("users").get(getUsername())).get("subjects")).containsKey(getContent(payload));
		if (hasSubject){
			
			if(getCurrentSubject()!=null){
				if (getCurrentSubject().equals(getContent(payload))){
					setCurrentSubject(null);
					}
				}
			
			
			//setCurrentSubject(null);
			((HashMap)((HashMap)server.getProperties().get("users").get(getUsername())).get("subjects")).remove(getContent(payload));
			((ArrayList)((HashMap)server.getProperties().get("subjects").get(getContent(payload))).get("members")).remove(getUsername());
			
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:info\t"
					+ "content:Subject removed from your list";
			out.println(returnToClient);
		}
		else{
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:You are not a participant in "+getContent(payload);
			out.println(returnToClient);
		}
	}
	void parse_get_questions(String payload){//idea, could supply argument, category, to only ask questions in this category
		
		if(getUsername()==null || getCurrentSubject()==null){
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:You need to log in to use this function, and set a working subject";
			out.println(returnToClient);
			return;
		}
		String questions="";
		Iterator question_iterator = ((ArrayList)((HashMap)server.getProperties().get("subjects").get(getCurrentSubject())).get("questions")).iterator();
		while(question_iterator.hasNext()){
			questions+=question_iterator.next();
			if (question_iterator.hasNext()){
				questions+="@";
			}
		}
		String returnToClient= 	"timestamp:"+LocalTime.now().toString()
				+"\tsender:server\t"
				+ "response:question\t"
				+ "content:"+questions;
		out.println(returnToClient);
		
	}
		
	void parse_help(String payload){
		String help = (String)server.getProperties().get("help").get("text");
		String returnToClient= 	"timestamp:"+LocalTime.now().toString()
				+"\tsender:server\t"
				+ "response:info\t"
				+ "content:"+help;
		out.println(returnToClient);
	}
	void parse_get_chatrooms(String payload){
		Iterator chatroom_iterator = ((HashMap)server.getProperties().get("chatrooms")).keySet().iterator();
		String chatrooms = "";
		while(chatroom_iterator.hasNext()){
			chatrooms+=chatroom_iterator.next();
			if (chatroom_iterator.hasNext()){
				chatrooms+=", ";
			}
		}
		String returnToClient= 	"timestamp:"+LocalTime.now().toString()
				+"\tsender:server\t"
				+ "response:info\t"
				+ "content:Chatrooms - "+chatrooms;
		out.println(returnToClient);
		
	}
	void parse_create_chatroom(String payload){
		if(getUsername()==null){
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:You need to log in to use this function";
			out.println(returnToClient);
			return;
		}
		
		boolean hasRoom = server.getProperties().get("chatrooms").containsKey(getContent(payload));
		if (hasRoom){
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:Chatroom already exists";
			out.println(returnToClient);
			return;
		}
		else{
			server.getProperties().get("chatrooms").put(getContent(payload), new HashMap());
			((HashMap)server.getProperties().get("chatrooms").get(getContent(payload))).put("log",new ArrayList<String>());
			((HashMap)server.getProperties().get("chatrooms").get(getContent(payload))).put("users",new HashMap<ClientHandler, HashMap>());
			
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:info\t"
					+ "content:Chatroom "+getContent(payload)+" successfully created";
			out.println(returnToClient);
		}
		
	}
	void parse_set_chatroom(String payload){
		boolean hasRoom = server.getProperties().get("chatrooms").containsKey(getContent(payload));
		if (getUsername() != null || !hasRoom){
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:You need to log out to switch chatrooms and provide a legal chatroom name";
			out.println(returnToClient);
			return;
		}
		else{
			setChat(getContent(payload));
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:info\t"
					+ "content:Successfully joined chatroom, please log in";
			out.println(returnToClient);
		}
		
	}
	void parse_get_chatroom(String payload){
		try{
		String returnToClient= 	"timestamp:"+LocalTime.now().toString()
				+"\tsender:server\t"
				+ "response:info\t"
				+ "content:"+getChat().toString();
		out.println(returnToClient);
		}
		catch(NullPointerException e){
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:info\t"
					+ "content:Null";
			out.println(returnToClient);
		}
	}
	void parse_get_subject(String payload){
		try{
		String returnToClient= 	"timestamp:"+LocalTime.now().toString()
				+"\tsender:server\t"
				+ "response:info\t"
				+ "content:"+getCurrentSubject().toString();
		out.println(returnToClient);
		}
		catch(NullPointerException e){
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:info\t"
					+ "content:No current subject";
			out.println(returnToClient);
		}
	}
	/*void parse_get_subjects(String payload){
		
		try {
			String returnToClient = "timestamp:"+LocalTime.now().toString()
									+"\tsender:server\t"
									+"response:info\t"
									+"content:";
			//server.getProperties().get("subjects").put("TDT4145", new HashMap<String, ArrayList<String>>());
			
			Iterator set = ((HashMap) server.getProperties().get("subjects")).keySet().iterator();
			while (set.hasNext()){
				returnToClient+=set.next();
				if(set.hasNext()){
					returnToClient+="@";
				}
			}
			
			out.println(returnToClient);
		} catch (NullPointerException e){
			String returnToClient = "timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+"response:info\t"
					+"content:Null";
			out.println(returnToClient);
		}
	}*/
	void parse_get_username(String payload){
		try{
		String returnToClient= 	"timestamp:"+LocalTime.now().toString()
				+"\tsender:server\t"
				+ "response:stats\t"
				+ "content:"+getUsername().toString();
		out.println(returnToClient);
		}
		catch(NullPointerException e){
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:stats\t"
					+ "content:No username";
			out.println(returnToClient);
		}
	}
	void parse_save_server(String payload){ 
		
		if(getUserType()==null || !getUserType().equals("admin")){
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:Students and lecturers are not allowed to utilize this function";
			out.println(returnToClient);
			return;
		}
		
		if (saveProperties()){
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:info\t"
					+ "content:Server status saved";
			out.println(returnToClient);
		}

		else{
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:Save unsuccessfull";
			out.println(returnToClient);
			
		}
	}
	void parse_get_names(String payload){// get names in current chatroom, argument, local or global
		String content = getContent(payload);
		if (content.equals("global")){
			String contentToClient="";
			Iterator connectedClients_it = ((HashMap)server.getProperties().get("connected_clients")).keySet().iterator();
			
			while (connectedClients_it.hasNext()){
				contentToClient+=((ClientHandler)connectedClients_it.next()).getUsername();
				
			}
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:info\t"
					+ "content:"+contentToClient;
			out.println(returnToClient);
		}
		else if (content.equals("local")){
			if (getChat()==null){
				String returnToClient= 	"timestamp:"+LocalTime.now().toString()
						+"\tsender:server\t"
						+ "response:error\t"
						+ "content:You need to log in to get local users";
				out.println(returnToClient);
			}
			else{
				String contentToClient="";
				Iterator connectedClients_it = ((HashMap)((HashMap)server.getProperties().get("chatrooms").get(getChat())).get("users")).keySet().iterator();
				while (connectedClients_it.hasNext()){
					contentToClient+=((ClientHandler)connectedClients_it.next()).getUsername();
					
				}
				String returnToClient= 	"timestamp:"+LocalTime.now().toString()
						+"\tsender:server\t"
						+ "response:info\t"
						+ "content:"+contentToClient;
				out.println(returnToClient);
				
			}
		}
		else{
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:You need to suply an argument, global or local";
			out.println(returnToClient);
		}
	}
	void parse_get_stats(String payload){ //get overall score for each subject, and score for each category, number of correct answers/total questions asked TODO: If subject arg provided, give stats in only that subject
		//send this      timestamp:<time>\tsender:server\tresponse:stats\tcontent:<subject>|<overall score>|<category>|<score>|<category>|<score>...@<subject>|<overall score>|<overall_score>|<category>|<score>...
			if (getUsername()==null){
				String returnToClient= 	"timestamp:"+LocalTime.now().toString()
						+"\tsender:server\t"
						+ "response:error\t"
						+ "content:You need to log in to use this function";
				out.println(returnToClient);
			}
			
			String content = "";
			Iterator subject_it = ((HashMap)((HashMap)server.getProperties().get("users").get(getUsername())).get("subjects")).keySet().iterator();
			while(subject_it.hasNext()){
				String nextSubject = (String)subject_it.next();
				HashMap subject = (HashMap)((HashMap) ((HashMap) server.getProperties().get("users").get(getUsername())).get("subjects")).get(nextSubject);
			
				Iterator category_it = ((HashMap)subject.get("categories")).keySet().iterator();
				String categoryAndScore = "";
				double totalScore = 0;
				while (category_it.hasNext()){
					String nextCategory = (String)category_it.next();
				
					HashMap category = (HashMap)((HashMap)subject.get("categories")).get(nextCategory);
					totalScore += (double) category.get("score");
					double categoryScore = ((double)category.get("score") / (double)category.get("#questions"));
					categoryAndScore+=nextCategory+"|"+categoryScore;
					if (category_it.hasNext()){
						categoryAndScore +="|";
						}
				}
				totalScore=totalScore/(double)subject.get("#questions");
				content+=nextSubject+"|"+totalScore+"|"+categoryAndScore;
				if (subject_it.hasNext()){
					content+="@";
				}
			
			}
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:stats\t"
					+ "content:"+content;
			out.println(returnToClient);
		
		
	}
	void parse_add_results(String payload){ //send results of a quiz to the properties of the server, update score
		//adds to current subject, payload is request:add_results\tcontent:<#questions>@<category>|<score>|<#questionsInScore>@<category>|<score>|<#questionsInScore>.... TODO: currently no element in GUI actually sends results, also needs to be implemented
		if (getUsername()==null || getCurrentSubject()==null){
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:You need to log in and set a working subject";
			out.println(returnToClient);
			return;
		}
		String[] rawContent = getContent(payload).split("[@]");
		double numberOfQuestions = Double.parseDouble(rawContent[0]);
		ArrayList categoryScore = new ArrayList();
		
		double numberOfQuestionsOld = (double)  ((HashMap) ((HashMap) ((HashMap) server.getProperties().get("users").get(getUsername())).get("subjects")).get(getCurrentSubject())).get("#questions");
		if (numberOfQuestionsOld == -1){
			numberOfQuestionsOld = 0;
		}
		((HashMap) ((HashMap) ((HashMap) server.getProperties().get("users").get(getUsername())).get("subjects")).get(getCurrentSubject())).put("#questions", numberOfQuestionsOld+numberOfQuestions ); //Update the amount of questions asked in total
		
		for (int x=1;x<rawContent.length;x++){
			String[] rawContentItem = rawContent[x].split("[|]");
			String category = rawContentItem[0];
			double score = Double.parseDouble(rawContentItem[1]);
			double questionsAskedInCategory = Double.parseDouble(rawContentItem[2]);
			try{
				HashMap userCategory = (HashMap)((HashMap)((HashMap)((HashMap)((HashMap)server.getProperties().get("users").get(getUsername())).get("subjects")).get(getCurrentSubject())).get("categories")).get(category);
				double userScore = (double)userCategory.get("score");
				userCategory.put("score", userScore+score);
				double numberOfCategoryQuestions = (double)userCategory.get("#questions");
				if (numberOfCategoryQuestions == -1){
					numberOfCategoryQuestions=0;
				}
				userCategory.put("#questions", numberOfCategoryQuestions+questionsAskedInCategory);
				 
			}
			catch(NullPointerException e){ // if category has not yet been added, do this
				//create categories and add them, set number of questions, score etc
				HashMap subjectCategories = (HashMap)((HashMap) ((HashMap)((HashMap)server.getProperties().get("users").get(getUsername())).get("subjects")).get(getCurrentSubject())).get("categories");
				subjectCategories.put(category,new HashMap());
				((HashMap) subjectCategories.get(category)).put("#questions", questionsAskedInCategory);
				((HashMap) subjectCategories.get(category)).put("score", score);
				
				
			}
		}
		String returnToClient= 	"timestamp:"+LocalTime.now().toString()
				+"\tsender:server\t"
				+ "response:info\t"
				+ "content:Results added successfully";
		out.println(returnToClient);
	}
	void parse_get_best_questions(String payload){ // server chooses which questions to send, based on score and the current subject, args maybe subject or category
		if (getUsername()==null || getCurrentSubject()==null){
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:You need to log in and/or set the working subject";
			out.println(returnToClient);
			return;
		}
		if (!((HashMap)((HashMap)server.getProperties().get("users").get(getUsername())).get("subjects")).containsKey(getCurrentSubject())){
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:You have not registered in the working subject";
			out.println(returnToClient);
			return;
		}
		double percent_min=100;
		String minCategory="error, something went wrong";
		HashMap subject = (HashMap)((HashMap)((HashMap) server.getProperties().get("users").get(getUsername())).get("subjects")).get(getCurrentSubject());
		HashMap categories = (HashMap)subject.get("categories");
		
		Iterator category_it = categories.keySet().iterator();
		while (category_it.hasNext()){
			String nextCategory = (String)category_it.next();
			double score = (double)((HashMap)categories.get(nextCategory)).get("score");
			double questions = (double)((HashMap)categories.get(nextCategory)).get("#questions");
			double percent = (score/questions)*100;
			if (percent<percent_min){
				percent_min = percent;
				minCategory = nextCategory;
				
			}
		}
		ArrayList<String> questionsToClient = new ArrayList();
		ArrayList<String> questions = (ArrayList<String>) ((HashMap)server.getProperties().get("subjects").get(getCurrentSubject())).get("questions");
		String content="";
		if (questions.size()>0){
			for (String question : questions){ // find the correct category of questions, gather those in a list
				String[] questionArray = question.split("[|]");
				for(int x=0;x<questionArray.length;x++){
				
					if (questionArray[x].split("[;]")[0].equals("c")){
						String category = questionArray[x].split("[;]")[1];
						if (category.equals(minCategory)){
							questionsToClient.add(question);
						}
					
					}
				}
			
			}
		
			content = "";
			for(String question : questionsToClient){ 
			content+=question+"@";
			}
			content = content.substring(0, content.length());
		}
		else{
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:No questions added in subject";
			out.println(returnToClient);
			return;
		}
		String returnToClient= 	"timestamp:"+LocalTime.now().toString()
				+"\tsender:server\t"
				+ "response:question\t"
				+ "content:"+content;
		out.println(returnToClient);
		
		
	}
	void parse_reset_score(String payload){ // set score and questions asked in active subject, and in every category under active subject to 0
		if (getUsername()==null || getCurrentSubject()==null){
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:You need to log in and/or set the working subject";
			out.println(returnToClient);
			return;
		}
		String content = getContent(payload);
		if (content.equals("local")){
			HashMap currentSubject = (HashMap)((HashMap)((HashMap)server.getProperties().get("users").get(getUsername())).get("subjects")).get(getCurrentSubject());
			currentSubject.put("#questions", (double)-1);
			currentSubject.put("score",(double) 0);
			Iterator category_it = ((HashMap)currentSubject.get("categories")).keySet().iterator();
			while (category_it.hasNext()){
				String category = (String)category_it.next();
				((HashMap)((HashMap)currentSubject.get("categories")).get(category)).put("#questions", 0);
				((HashMap)((HashMap)currentSubject.get("categories")).get(category)).put("score", 0);
			}
			
			
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:info\t"
					+ "content:Score reset successfully in "+getCurrentSubject();
			out.println(returnToClient);
		
		}
		else if (content.equals("global")){
			Iterator subject_it = ((HashMap)((HashMap)server.getProperties().get("users").get(getUsername())).get("subjects")).keySet().iterator();
			while(subject_it.hasNext()){
				String subject = (String)subject_it.next();
				
				HashMap currentSubject = (HashMap)((HashMap)((HashMap)server.getProperties().get("users").get(getUsername())).get("subjects")).get(subject);
				currentSubject.put("#questions", (double) -1);
				currentSubject.put("score", (double) 0);
				Iterator category_it = ((HashMap)currentSubject.get("categories")).keySet().iterator();
				while (category_it.hasNext()){
					String category = (String)category_it.next();
					((HashMap)((HashMap)currentSubject.get("categories")).get(category)).put("#questions", (double)-1);
					((HashMap)((HashMap)currentSubject.get("categories")).get(category)).put("score", (double) 0);
				}
			}
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:info\t"
					+ "content:Score reset successfully in all subjects";
			out.println(returnToClient);

		}
		else{
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:You need to supply an argument for this function, local or global";
			out.println(returnToClient);
		}
	}

	void parse_get_type(String payload){
		if (getUsername()==null){
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:You need to log in to get the usertype";
			out.println(returnToClient);
			return;
		}
		String returnToClient= 	"timestamp:"+LocalTime.now().toString()
				+"\tsender:server\t"
				+ "response:info\t"
				+ "content:"+getUserType();
		out.println(returnToClient);
		
	}
	
	void parse_get_subject_scores(){
		//TODO: make something that gives the results of every user in the current subject, only for lecturers and admins
	}

	
}