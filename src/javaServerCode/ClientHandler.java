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
import java.util.Set;

public class ClientHandler implements Runnable{
	private Thread t;
	private ServerHost server;
	private Socket clientSocket;
	private PrintWriter out;
    private BufferedReader in;
    
    private String returnToClient;
    private String username;
    private String chatroom;
    private String current_subject;
    private String userType;
    
    public ClientHandler(){
    	
    }
    
	public ClientHandler(ServerHost server, Socket clientSocket)  {
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
	public String getReturnToClient(){
		return returnToClient;
	}
	public String getRequest(String payload){						//helper functions to save space and time, get the request and content part of a message easy
		try{
			return payload.split("\t")[0].split(":")[1];
			}
			catch(ArrayIndexOutOfBoundsException e){
				return "";
			}
	}
	public String getContent(String payload){
		try{
		return payload.split("\t")[1].split(":")[1];
		}
		catch(ArrayIndexOutOfBoundsException e){
			return "";
		}
	}
	public String getChat(){
		return chatroom;
	}
	public void setChat(String chatname){
		chatroom=chatname;
	}
	public PrintWriter getOut(){
		return out;
	}
	
	public String getUsername(){
		return username;
	}
	public void setUsername(String name){
		this.username = name;
	}
	public String getCurrentSubject(){
		return current_subject;
	}
	public void setCurrentSubject(String subject){
		this.current_subject=subject;
	}
	boolean saveProperties(){
		try{
			server.prop.saveProperties(server.getProperties());
			return true;
		}
		catch(Exception e){
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
	
	public String get_sender(String payload){
		return payload.split("\t")[1].split(":")[1];
	}
	public String getUserType(){
		return this.userType;
	}
	public void setUserType(String type){
		this.userType = type;
	}
	public String get_content(String payload){
		try{
			return payload.split("\t")[3].split(":")[1];
		}
		catch(ArrayIndexOutOfBoundsException e){
			return "";
		}
	}
	String get_timestamp(String payload){
		return payload.split("\t")[0].split("timestamp:")[1];
		
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
		else if (request.equals("get_subject_scores")){ 
			parse_get_subject_scores(payload);
		}
		else{
			this.out.println("timestamp:"+LocalTime.now().toString()+"\tsender:server\tresponse:error\tcontent:Not a valid command");
		}
		
	}
	
	
	
	
	void parse_login(String payload){
		if (getContent(payload).split("@").length<2){
			returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:you need to provide correct arguments for login, format is <username>@<PW>@<type> if new user, <username>@<PW> if already created user";
			out.println(returnToClient);
			return;
		}
		String username= getContent(payload).split("@")[0];
		String password= getContent(payload).split("@")[1];
		
		if (username.equals("")){
			returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:you need to provide username for login, format is <username>@<PW>@<type>";
			out.println(returnToClient);
			return;
		}
		if (getUsername() != null){											//check if already given username
			returnToClient= 	"timestamp:"+LocalTime.now().toString()
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
					returnToClient= 	"timestamp:"+LocalTime.now().toString()
							+"\tsender:server\t"
							+ "response:error\t"
							+ "content:Username already logged in";
					out.println(returnToClient);
					return;
				}
			}
		}
		catch(NullPointerException e){
			//Do nothing
		}
		

		
		HashMap users = (HashMap)server.getProperties().get("users");//set up the user properties, add the user to user etc
		
		
		if(users.containsKey(username)){					//Check if already create user, check password
			if (!password.equals(((HashMap)users.get(username)).get("password"))){
				returnToClient = "timestamp:"+LocalTime.now().toString()
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
				returnToClient= 	"timestamp:"+LocalTime.now().toString()
						+"\tsender:server\t"
						+ "response:error\t"
						+ "content:you need to provide correct arguments for login, format is <username>@<PW>@<type> if new user, <username>@<PW> if already created user";
				out.println(returnToClient);
				return;
			}
			String userType= getContent(payload).split("@")[2]; // "admin" for admin, "student" for student, "lecturer" for lecturer
			if (userType.equals("") || !(userType.equals("admin")||userType.equals("student")||userType.equals("lecturer"))){
				returnToClient= 	"timestamp:"+LocalTime.now().toString()
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
		
		returnToClient= 	"timestamp:"+LocalTime.now().toString()
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
			returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:Need to log in to log out";
			out.println(returnToClient);
			return;
		}
		returnToClient= 	"timestamp:"+LocalTime.now().toString()
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
			returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:You need to log in to send messages";
			out.println(returnToClient);
			return;
		}
		returnToClient= 	"timestamp:"+LocalTime.now().toString()
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
			returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:You need to log in to see chat logs";
			out.println(returnToClient);
			return;
		}
		ArrayList chatlog = (ArrayList)((HashMap)server.getProperties().get("chatrooms").get(this.chatroom)).get("log"); // print every message received so far
		for(int x=0;x<chatlog.size();x++){
			returnToClient= 	"timestamp:"+get_timestamp((String)chatlog.get(x))+"\t"
					+"sender:"+get_sender((String)chatlog.get(x))+"\t"
					+ "response:history\t"
					+ "content:"+get_content((String)chatlog.get(x));
			out.println(returnToClient);
		}
		
	}
	void parse_create_subject(String payload){
		if(getUsername()==null){
			returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:You need to log in to use this function";
			out.println(returnToClient);
			System.out.println("retrun: " + returnToClient);
			return;
		}
		if(getUserType().equals("student")){
			returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:Students are not allowed to utilize this function";
			out.println(returnToClient);
			return;
		}
		if (server.getProperties().get("subjects").containsKey(getContent(payload))){
			returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:Subject already exists";
			out.println(returnToClient);
		}
		else{
			server.getProperties().get("subjects").put(getContent(payload), new HashMap());
			((HashMap)server.getProperties().get("subjects").get(getContent(payload))).put("members", new ArrayList<String>());
			((HashMap)server.getProperties().get("subjects").get(getContent(payload))).put("questions", new ArrayList<String>());
			returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:info\t"
					+ "content:Subject created successfully";
			out.println(returnToClient);
		}
		
	}
	void parse_set_subject(String payload){
		if(getUsername()==null){
			returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:You need to log in to use this function";
			out.println(returnToClient);
			return;
		}
		if (! server.getProperties().get("subjects").containsKey(getContent(payload))){
			returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:Subject does not exist";
			out.println(returnToClient);
			return;
		}
		if (! ((HashMap)((HashMap)server.getProperties().get("users").get(getUsername())).get("subjects")).containsKey(getContent(payload))){
			returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:Subject does not exist in your registered subjects";
			out.println(returnToClient);
			return;
		}
		else{
			returnToClient= 	"timestamp:"+LocalTime.now().toString()
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
			returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:You need to log in to use this function";
			out.println(returnToClient);
			return;
		}
		
		boolean hasSubject = ((HashMap)((HashMap)server.getProperties().get("users").get(getUsername())).get("subjects")).containsKey(getContent(payload));
		boolean subjectExists = server.getProperties().get("subjects").containsKey(getContent(payload));
		if (hasSubject || !subjectExists ){
			returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:Subject does not exist, or you already have registered in the subject";
			out.println(returnToClient);
		}
		else{	//create the properties needed for a subject registered to a user
			
			HashMap subjects = (HashMap)((HashMap)server.getProperties().get("users").get(getUsername())).get("subjects");
			subjects.put(subject, new HashMap());
			((HashMap) subjects.get(subject)).put("score",Double.parseDouble("0"));
			((HashMap) subjects.get(subject)).put("categories",new HashMap());  
			((HashMap) subjects.get(subject)).put("#questions",Double.parseDouble("-1"));
			((ArrayList)((HashMap)server.getProperties().get("subjects").get(subject)).get("members")).add(getUsername());
			
			returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:info\t"
					+ "content:Subject added successfully";
			out.println(returnToClient);
		}
	}
	public void parse_add_question(String payload){
		if(getUsername()==null){
			returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:You need to log in to use this function";
			out.println(returnToClient);
			return;
		}
		if(getCurrentSubject()==null ){
			returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:You need to set the working subject";
			out.println(returnToClient);
			return;
		}
		if(getUserType().equals("student")){
			returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:Students are not allowed to utilize this function";
			out.println(returnToClient);
			return;
		}
		
		((ArrayList)((HashMap)server.getProperties().get("subjects").get(getCurrentSubject())).get("questions")).add(getContent(payload));
		returnToClient= 	"timestamp:"+LocalTime.now().toString()
				+"\tsender:server\t"
				+ "response:info\t"
				+ "content:Question added successfully in "+getCurrentSubject();
		out.println(returnToClient);
	}
	public void parse_get_subjects(String payload){
		if(getUsername()==null){
			returnToClient= 	"timestamp:"+LocalTime.now().toString()
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
			returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:subjectsGlobal\t"
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
			returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:subjectsLocal\t"
					+ "content:Your registered subjects; "+subjects;
			out.println(returnToClient);
		}
		else{
			returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:Must supply argument, global or local";
			out.println(returnToClient);
		}

	}
	

	
	void parse_remove_subject(String payload){

		if(getUsername()==null){
			returnToClient= 	"timestamp:"+LocalTime.now().toString()
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
			
			returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:info\t"
					+ "content:Subject removed from your list";
			out.println(returnToClient);
		}
		else{
			returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:You are not a participant in "+getContent(payload);
			out.println(returnToClient);
		}
	}
	void parse_get_questions(String payload){
		
		if(getUsername()==null || getCurrentSubject()==null){
			returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:You need to log in to use this function, and set a working subject";
			out.println(returnToClient);
			return;
		}
		String questions="";
		Iterator question_iterator = ((ArrayList)((HashMap)server.getProperties().get("subjects").get(getCurrentSubject())).get("questions")).iterator();
		if (!getContent(payload).equals("")){ //If an argument is supplied, try to find every question in the category
			String category = getContent(payload);
			while(question_iterator.hasNext()){
				
				String nextQuestion = (String)question_iterator.next();
				for(String element : nextQuestion.split("[|]")){
					if (element.split("[;]")[0].equals("c")){
						if (element.split("[;]")[1].equals(category)){
							questions+=nextQuestion+"@";
						}
					}
				}
			}
		}
		else{
			while(question_iterator.hasNext()){
				String nextQuestion = (String)question_iterator.next();
				questions+=nextQuestion+"@";
			}
		}
		if (!(questions.length()==0)){
			questions = questions.substring(0, questions.length()-1);
		}
		
		returnToClient= 	"timestamp:"+LocalTime.now().toString()
				+"\tsender:server\t"
				+ "response:question\t"
				+ "content:"+questions;
		out.println(returnToClient);
		
	}
		
	void parse_help(String payload){
		String help = (String)server.getProperties().get("help").get("text");
		returnToClient= 	"timestamp:"+LocalTime.now().toString()
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
		returnToClient= 	"timestamp:"+LocalTime.now().toString()
				+"\tsender:server\t"
				+ "response:info\t"
				+ "content:Chatrooms - "+chatrooms;
		out.println(returnToClient);
		
	}
	void parse_create_chatroom(String payload){
		if(getUsername()==null){
			returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:You need to log in to use this function";
			out.println(returnToClient);
			return;
		}
		
		boolean hasRoom = server.getProperties().get("chatrooms").containsKey(getContent(payload));
		if (hasRoom){
			returnToClient= 	"timestamp:"+LocalTime.now().toString()
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
			
			returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:info\t"
					+ "content:Chatroom "+getContent(payload)+" successfully created";
			out.println(returnToClient);
		}
		
	}
	void parse_set_chatroom(String payload){
		boolean hasRoom = server.getProperties().get("chatrooms").containsKey(getContent(payload));
		if (getUsername() == null || !hasRoom){
			returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:You need to log in to enter chatrooms and provide a legal chatroom name";
			out.println(returnToClient);
			return;
		}
		else{
			String dummy="";
			String username = getUsername();
			String pw = (String)((HashMap)server.getProperties().get("users").get(username)).get("password");
			parse_logout(dummy);
			setChat(getContent(payload));
			String credentials = "request:login\tcontent:"+username+"@"+pw;
			parse_login(credentials);
			
			
			
			returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:info\t"
					+ "content:Successfully joined chatroom "+getChat();
			out.println(returnToClient);
		}
		
	}
	void parse_get_chatroom(String payload){
		try{
		returnToClient= 	"timestamp:"+LocalTime.now().toString()
				+"\tsender:server\t"
				+ "response:info\t"
				+ "content:"+getChat().toString();
		out.println(returnToClient);
		}
		catch(NullPointerException e){
			returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:info\t"
					+ "content:Null";
			out.println(returnToClient);
		}
	}
	void parse_get_subject(String payload){
		try{
		returnToClient= 	"timestamp:"+LocalTime.now().toString()
				+"\tsender:server\t"
				+ "response:info\t"
				+ "content:"+getCurrentSubject().toString();
		out.println(returnToClient);
		}
		catch(NullPointerException e){
			returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:info\t"
					+ "content:No current subject";
			out.println(returnToClient);
		}
	}

	void parse_get_username(String payload){
		try{
		returnToClient= 	"timestamp:"+LocalTime.now().toString()
				+"\tsender:server\t"
				+ "response:stats\t"
				+ "content:Your username; "+getUsername().toString();
		out.println(returnToClient);
		}
		catch(NullPointerException e){
			returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:stats\t"
					+ "content:No username";
			out.println(returnToClient);
		}
	}
	void parse_save_server(String payload){ 
		
		if(getUserType()==null || !getUserType().equals("admin")){

			if (getUserType()==null){
				returnToClient= 	"timestamp:"+LocalTime.now().toString()
						+"\tsender:server\t"
						+ "response:error\t"
						+ "content:You need to log in to verify that you are and admin to use this function";
				out.println(returnToClient);
				return;
			}
			returnToClient= 	"timestamp:"+LocalTime.now().toString()

					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:Students and lecturers are not allowed to utilize this function";
			out.println(returnToClient);
			return;
		}
		
		if (saveProperties()){
			returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:info\t"
					+ "content:Server status saved";
			out.println(returnToClient);
		}

		else{
			returnToClient= 	"timestamp:"+LocalTime.now().toString()
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
			returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:info\t"
					+ "content:"+contentToClient;
			out.println(returnToClient);
		}
		else if (content.equals("local")){
			if (getChat()==null){
				returnToClient= 	"timestamp:"+LocalTime.now().toString()
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
				returnToClient= 	"timestamp:"+LocalTime.now().toString()
						+"\tsender:server\t"
						+ "response:info\t"
						+ "content:"+contentToClient;
				out.println(returnToClient);
				
			}
		}
		else{
			returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:You need to suply an argument, global or local";
			out.println(returnToClient);
		}
	}
	void parse_get_stats(String payload){ //get overall score for each subject, and score for each category, number of correct answers/total questions asked
		//send this      timestamp:<time>\tsender:server\tresponse:stats\tcontent:<subject>|<overall score>|<category>|<score>|<category>|<score>...@<subject>|<overall score>|<overall_score>|<category>|<score>...
			if (getUsername()==null){
				returnToClient= 	"timestamp:"+LocalTime.now().toString()
						+"\tsender:server\t"
						+ "response:error\t"
						+ "content:You need to log in to use this function";
				out.println(returnToClient);
				return;
			}
			if (getCurrentSubject()==null){
				returnToClient= 	"timestamp:"+LocalTime.now().toString()
						+"\tsender:server\t"
						+ "response:error\t"
						+ "content:You need to set a working subject";
				out.println(returnToClient);
				return;
			}
			
			String content = "";
			
				String nextSubject = getCurrentSubject();
				HashMap subject = (HashMap)((HashMap) ((HashMap) server.getProperties().get("users").get(getUsername())).get("subjects")).get(nextSubject);
			
				Iterator category_it = ((HashMap)subject.get("categories")).keySet().iterator();
				String categoryAndScore = "";
				double totalScore = 0;
				while (category_it.hasNext()){
					String nextCategory = (String)category_it.next();
				
					HashMap category = (HashMap)((HashMap)subject.get("categories")).get(nextCategory);
					totalScore += Double.parseDouble(""+category.get("score"));
					double categoryScore = (Double.parseDouble(""+category.get("score")) / Double.parseDouble(""+category.get("#questions")));
					categoryAndScore+=nextCategory+"|"+categoryScore;
					if (category_it.hasNext()){
						categoryAndScore +="|";
						} 
				}
				totalScore=totalScore/Double.parseDouble(""+subject.get("#questions"));
				content+=nextSubject+"|"+totalScore+"|"+categoryAndScore;

			returnToClient= 	"timestamp:"+LocalTime.now().toString()

					+"\tsender:server\t"
					+ "response:userScore\t"
					+ "content:"+content;
			out.println(returnToClient);
		
		
	}
	void parse_add_results(String payload){ //send results of a quiz to the properties of the server, update score
		//adds to current subject, payload is request:add_results\tcontent:<#questions>@<category>|<score>|<#questionsInScore>@<category>|<score>|<#questionsInScore>.... 
		if (getUsername()==null || getCurrentSubject()==null){
			returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:You need to log in and set a working subject";
			out.println(returnToClient);
			return;
		}
		String[] rawContent = getContent(payload).split("[@]");
		double numberOfQuestions = Double.parseDouble(rawContent[0]);
		ArrayList categoryScore = new ArrayList();
		double scoreOld = Double.parseDouble(""+((HashMap) ((HashMap) ((HashMap) server.getProperties().get("users").get(getUsername())).get("subjects")).get(getCurrentSubject())).get("score"));
		double numberOfQuestionsOld = Double.parseDouble(""+((HashMap) ((HashMap) ((HashMap) server.getProperties().get("users").get(getUsername())).get("subjects")).get(getCurrentSubject())).get("#questions"));
		double totalScore =0;
		if (Double.compare(numberOfQuestionsOld, Double.parseDouble("-1"))==0){
			numberOfQuestionsOld = 0;
		}
		((HashMap) ((HashMap) ((HashMap) server.getProperties().get("users").get(getUsername())).get("subjects")).get(getCurrentSubject())).put("#questions", numberOfQuestionsOld+numberOfQuestions ); //Update the amount of questions asked in total
		
		for (int x=1;x<rawContent.length;x++){
			String[] rawContentItem = rawContent[x].split("[|]");
			String category = rawContentItem[0];
			double score = Double.parseDouble(rawContentItem[1]);
			totalScore+=score;
			double questionsAskedInCategory = Double.parseDouble(rawContentItem[2]);
			try{
				HashMap userCategory = (HashMap)((HashMap)((HashMap)((HashMap)((HashMap)server.getProperties().get("users").get(getUsername())).get("subjects")).get(getCurrentSubject())).get("categories")).get(category);
				double userScore = Double.parseDouble(""+userCategory.get("score"));
				userCategory.put("score", userScore+score);
				double numberOfCategoryQuestions = Double.parseDouble(""+userCategory.get("#questions"));
				if (Double.compare(numberOfCategoryQuestions, Double.parseDouble("-1"))==0){
					numberOfCategoryQuestions=0;
				}
				userCategory.put("#questions", numberOfCategoryQuestions+questionsAskedInCategory);
			}
			catch(NullPointerException e){
				HashMap subjectCategories = (HashMap)((HashMap) ((HashMap)((HashMap)server.getProperties().get("users").get(getUsername())).get("subjects")).get(getCurrentSubject())).get("categories");
				subjectCategories.put(category,new HashMap());
				((HashMap) subjectCategories.get(category)).put("#questions", questionsAskedInCategory);
				((HashMap) subjectCategories.get(category)).put("score", score);
				
			}
		}
		
		((HashMap) ((HashMap) ((HashMap) server.getProperties().get("users").get(getUsername())).get("subjects")).get(getCurrentSubject())).put("score", scoreOld+totalScore );
		returnToClient= 	"timestamp:"+LocalTime.now().toString()

				+"\tsender:server\t"
				+ "response:info\t"
				+ "content:Results added successfully";
		out.println(returnToClient);
	}
	void parse_get_best_questions(String payload){ 
		if (getUsername()==null || getCurrentSubject()==null){
			returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:You need to log in and/or set the working subject";
			out.println(returnToClient);
			return;
		}
		if (!((HashMap)((HashMap)server.getProperties().get("users").get(getUsername())).get("subjects")).containsKey(getCurrentSubject())){
			returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:You have not registered in the working subject";
			out.println(returnToClient);
			return;
		}
		double percent_min=101;
		String minCategory="something went wrong";
		HashMap subject = (HashMap)((HashMap)((HashMap) server.getProperties().get("users").get(getUsername())).get("subjects")).get(getCurrentSubject());
		HashMap categories = (HashMap)subject.get("categories");
		
		Iterator category_it = categories.keySet().iterator();
		while (category_it.hasNext()){
			String nextCategory = (String)category_it.next();
			double score = Double.parseDouble(""+((HashMap)categories.get(nextCategory)).get("score"));
			double questions = Double.parseDouble(""+((HashMap)categories.get(nextCategory)).get("#questions"));
			double percent = (score/questions)*100;
			if (percent<percent_min){
				percent_min = percent;
				minCategory = new String(nextCategory);
				
			}
		}
		returnToClient= 	"timestamp:"+LocalTime.now().toString()

				+"\tsender:server\t"
				+ "response:bestQuestions\t"
				+ "content:"+minCategory;
		out.println(returnToClient);
		
		
	}
	
	
	void parse_reset_score(String payload){ // set score and questions asked in active subject, and in every category under active subject to 0
		if (getUsername()==null || getCurrentSubject()==null){
			returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:You need to log in and/or set the working subject";
			out.println(returnToClient);
			return;
		}
		
		String content = getContent(payload);
		if (content.equals("local")){
			HashMap currentSubject = (HashMap)((HashMap)((HashMap)server.getProperties().get("users").get(getUsername())).get("subjects")).get(getCurrentSubject());
			currentSubject.put("#questions", Double.parseDouble("-1"));
			currentSubject.put("score",Double.parseDouble("0"));
			Iterator category_it = ((HashMap)currentSubject.get("categories")).keySet().iterator();
			while (category_it.hasNext()){
				String category = (String)category_it.next();
				((HashMap)((HashMap)currentSubject.get("categories")).get(category)).put("#questions", 0);
				((HashMap)((HashMap)currentSubject.get("categories")).get(category)).put("score", 0);
			}
			
			
			returnToClient= 	"timestamp:"+LocalTime.now().toString()
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
				currentSubject.put("#questions", Double.parseDouble("-1"));
				currentSubject.put("score", Double.parseDouble("0"));
				Iterator category_it = ((HashMap)currentSubject.get("categories")).keySet().iterator();
				while (category_it.hasNext()){
					String category = (String)category_it.next();
					((HashMap)((HashMap)currentSubject.get("categories")).get(category)).put("#questions", Double.parseDouble("-1"));
					((HashMap)((HashMap)currentSubject.get("categories")).get(category)).put("score", Double.parseDouble("0"));
				}
			}
			returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:info\t"
					+ "content:Score reset successfully in all subjects";
			out.println(returnToClient);

		}
		else{
			returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:You need to supply an argument for this function, local or global";
			out.println(returnToClient);
		}
	}

	void parse_get_type(String payload){
		if (getUsername()==null){

			if (getContent(payload).equals("noreply")){
				return;
			}
			returnToClient= 	"timestamp:"+LocalTime.now().toString()

					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:You need to log in to get the usertype";
			out.println(returnToClient);
			return;
		}
		returnToClient= 	"timestamp:"+LocalTime.now().toString()

				+"\tsender:server\t"
				+ "response:stats\t"
				+ "content:User type; "+getUserType();
		out.println(returnToClient);
		
	}
	
	void parse_get_subject_scores(String payload){
		if (getUsername()==null || getCurrentSubject()==null){
			if (getContent(payload).equals("noreply")){
				return;
			}
			returnToClient= 	"timestamp:"+LocalTime.now().toString()

					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:You need to log in to use this function, or you need to set the current subject";
			out.println(returnToClient);
			return;
		}
		if (getUserType().equals("student")){
			if (getContent(payload).equals("noreply")){
				return;
			}
			returnToClient= 	"timestamp:"+LocalTime.now().toString()

					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:Your username does not have the required privelige";
			out.println(returnToClient);
			return;
		}
		
			String content = ""+getCurrentSubject();
			HashMap usernamesInSubject_map = new HashMap();
			Iterator usernamesInSubject_it = ((ArrayList)((HashMap)server.getProperties().get("subjects").get(getCurrentSubject())).get("members")).iterator();
			while (usernamesInSubject_it.hasNext()){
				usernamesInSubject_map.put((String)usernamesInSubject_it.next(), "");
			}
			Set userSet = usernamesInSubject_map.keySet();
			
			double totalScore = 0;
			double totalQuestions = -1;
			HashMap subjectScoreMap = new HashMap();
			Iterator globalUsers_it = server.getProperties().get("users").keySet().iterator();
			while (globalUsers_it.hasNext()){
				String member = (String)globalUsers_it.next();
				if (userSet.contains(member)){
					Iterator categories_it = ((HashMap)((HashMap)((HashMap)((HashMap)server.getProperties().get("users").get(member)).get("subjects")).get(getCurrentSubject())).get("categories")).keySet().iterator();
					
					
					totalScore += Double.parseDouble(""+((HashMap)((HashMap)((HashMap)server.getProperties().get("users").get(member)).get("subjects")).get(getCurrentSubject())).get("score"));
					double serverQuestions = Double.parseDouble(""+((HashMap)((HashMap)((HashMap)server.getProperties().get("users").get(member)).get("subjects")).get(getCurrentSubject())).get("#questions"));
					if (Double.compare(serverQuestions, Double.parseDouble("-1"))!=0){
						if (totalQuestions==-1){
							totalQuestions=0;
						}
						totalQuestions += Double.parseDouble(""+((HashMap)((HashMap)((HashMap)server.getProperties().get("users").get(member)).get("subjects")).get(getCurrentSubject())).get("#questions"));
					}
					while (categories_it.hasNext()){
						String category =(String) categories_it.next();
						if (subjectScoreMap.containsKey(category)){
							double newQuestions = Double.parseDouble(""+((HashMap)((HashMap)((HashMap)((HashMap)((HashMap)server.getProperties().get("users").get(member)).get("subjects")).get(getCurrentSubject())).get("categories")).get(category)).get("#questions"));
							if (Double.compare(newQuestions,-1)==0){
								newQuestions=0;
							}
							double newScore = Double.parseDouble(""+((HashMap)((HashMap)((HashMap)((HashMap)((HashMap)server.getProperties().get("users").get(member)).get("subjects")).get(getCurrentSubject())).get("categories")).get(category)).get("score"));
							double oldQuestions = Double.parseDouble(""+((HashMap)subjectScoreMap.get(category)).get("#questions"));
							double oldScore = Double.parseDouble(""+((HashMap)subjectScoreMap.get(category)).get("#questions"));
							((HashMap)subjectScoreMap.get(category)).put("score", oldScore+newScore);
							((HashMap)subjectScoreMap.get(category)).put("#questions", oldQuestions+newQuestions);
						}

						else{
							double newQuestions = Double.parseDouble(""+((HashMap)((HashMap)((HashMap)((HashMap)((HashMap)server.getProperties().get("users").get(member)).get("subjects")).get(getCurrentSubject())).get("categories")).get(category)).get("#questions"));
							if (Double.compare(newQuestions,-1)==0){
								newQuestions=0;
							}
							double newScore = Double.parseDouble(""+((HashMap)((HashMap)((HashMap)((HashMap)((HashMap)server.getProperties().get("users").get(member)).get("subjects")).get(getCurrentSubject())).get("categories")).get(category)).get("score"));
							subjectScoreMap.put(category, new HashMap());
							((HashMap)subjectScoreMap.get(category)).put("score", newScore);
							((HashMap)subjectScoreMap.get(category)).put("#questions",newQuestions);

					
					}
				
				}
			}
	
	}
			
			Iterator categories_it = subjectScoreMap.keySet().iterator();
			content+="|"+totalScore/totalQuestions+"|";
			
			while (categories_it.hasNext()){
				String category = (String)categories_it.next();
				double tempScore = Double.parseDouble(""+((HashMap)subjectScoreMap.get(category)).get("score"));
				double tempQs =  Double.parseDouble(""+((HashMap)subjectScoreMap.get(category)).get("#questions"));
				double fraction= tempScore/tempQs;
				content+=category+"|"+fraction;
				if (categories_it.hasNext()){
					content+="|";
				}
			}

			returnToClient= 	"timestamp:"+LocalTime.now().toString()

					+"\tsender:server\t"
					+ "response:subjectScore\t"
					+ "content:"+content;
			out.println(returnToClient);
		
	
}
	}