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

public class ClientHandler implements Runnable{
	private Thread t;
	private ServerHost server;
	private Socket clientSocket;
	private PrintWriter out;
    private BufferedReader in;
    
    private String username;
    private String chatroom;
    private String current_subject;
    
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
		if (getRequest(payload).equals("login")){
			parse_login(payload);
			
		}
		else if(getRequest(payload).equals("logout")){
			parse_logout(payload);
		}
		else if(getRequest(payload).equals("msg")){
			parse_msg(payload);
			
		}
		else if(getRequest(payload).equals("help")){
			parse_help(payload);
		}
		//else if(get_request(payload).equals("get<property>")){ //getters for all user data
		//}
		else if(getRequest(payload).equals("get_subjects")){ //getsubjects <None>
			parse_get_subjects(payload);
		}
		else if(getRequest(payload).equals("add_subject")){ //addsjubject <subject>, adds a subject from one of the existing ones
			parse_add_subject(payload);
		}
		else if(getRequest(payload).equals("remove_subject")){ //removesubjects <subject>
			parse_remove_subject(payload);
		}
		else if(getRequest(payload).equals("history")){ //get chatlog in current chatroom, history <null>
			parse_history(payload);
		}
		else if(getRequest(payload).equals("get_questions")){//getquestions <subject>
			parse_get_questions(payload);
		}
		else if(getRequest(payload).equals("set_subject")){ //TODO: possible to set subject without having it added, fix this---set_current_subject <subject>, should check if you have membership in the subject, or if it is a subject at all
			parse_set_subject(payload);
		}
		else if(getRequest(payload).equals("create_subject")){ //getsubjects <None>, creates a new subject
			parse_create_subject(payload);
		}
		else if(getRequest(payload).equals("add_question")){//addquestion <question in defined string format> into current suject
			parse_add_question(payload);
		}
		else if(getRequest(payload).equals("set_chatroom")){
			parse_set_chatroom(payload);
		}
		else if(getRequest(payload).equals("add_chatroom")){
			parse_add_chatroom(payload);
		} 
		else if(getRequest(payload).equals("get_chatrooms")){
			parse_get_chatrooms(payload);
		}
		else if(getRequest(payload).equals("save_server")){
			parse_save_server(payload);
		}
		else if(getRequest(payload).equals("get_username")){
			parse_get_username(payload);
		}
		else if(getRequest(payload).equals("get_chatroom")){
			parse_get_chatroom(payload);
		}
		else if(getRequest(payload).equals("get_subject")){
			parse_get_subject(payload);
		}
		else if (getRequest(payload).equals("get_questions")){
			parse_get_questions(payload);
		}
		else{
			this.out.println("timestamp:"+LocalTime.now().toString()+"\tsender:server\tresponse:error\tcontent:Not a valid command");
		}
		
	}
	
	
	
	
	void parse_login(String payload){
		String username= getContent(payload);
		if (username.equals("")){
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:you need to provide username for login";
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
		
		Iterator userIterator= (Iterator)server.getProperties().get("connected_clients").keySet().iterator();		// check if username already logged in
		try{
		while (userIterator.hasNext()){
			if (((ClientHandler)userIterator.next()).getUsername().equals(username)){
				String returnToClient= 	"timestamp:"+LocalTime.now().toString()
						+"\tsender:server\t"
						+ "response:error\t"
						+ "content:Username already in use";
				out.println(returnToClient);
				return;
			}
		}
		}
		catch(NullPointerException e){
			//System.out.println("Null pointer in connected_clients, no one added yet");
		}
		
		if (getChat() == null){
			setChat("start");
		}
		
		
		HashMap prop = (HashMap)server.getProperties().get("users");				//set up the user properties, add the user to user etc
		prop.put(username,new HashMap());
		HashMap userdata = (HashMap)server.getProperties().get("users").get(username);
		userdata.put("subjects", new HashMap<String,HashMap>());
		//userdata.put("overall_score",0);
		setUsername(username);
		String returnToClient= 	"timestamp:"+LocalTime.now().toString()
				+"\tsender:server\t"
				+ "response:info\t"
				+ "content:OK - Login successful as "+this.username+" into chatroom - "+getChat();
		String returnToClients = "timestamp:"+LocalTime.now().toString()
				+"\tsender:server\t"
				+ "response:info\t"
				+ "content:"+this.username+" logged in";
		
		HashMap users= (HashMap)((HashMap)server.getProperties().get("chatrooms").get(getChat())).get("users");
		users.put(this, new ArrayList());																	//add user to chat
		
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
		//ArrayList chatlog = (ArrayList)((HashMap)server.getProperties().get("chatrooms").get(getChat())).get("log"); // print every message received so far
		//for(int x=chatlog.size()-1;x>chatlog.size()-4;x--){ // 4 last messages from chat
		//	out.println(chatlog.get(x));
		//}
		
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
				+ "response:info\t"
				+ "content:"+this.username+" logged out";
		
		ArrayList chat= (ArrayList)((HashMap)server.getProperties().get("chatrooms").get(getChat())).get("log");  //add message to chat
		chat.add(returnToClients);
		HashMap users= (HashMap)((HashMap)server.getProperties().get("chatrooms").get(getChat())).get("users"); //remove oneself from chatroom
		users.remove(this);
		((HashMap)server.getProperties().get("connected_clients")).remove(this); 								 //remove oneself from connected clients 
		Iterator clients = ((HashMap)((HashMap)server.getProperties().get("chatrooms").get(getChat())).get("users")).keySet().iterator();
		while(clients.hasNext()){
			((ClientHandler)clients.next()).getOut().println(returnToClients);
		}
		out.println(returnToClient);
		setUsername(null);
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
		else{
			
			
			((HashMap)((HashMap)server.getProperties().get("users").get(getUsername())).get("subjects")).put(getContent(payload), new HashMap());   //casing out of control, setup the standard keys for a user-subject
			((HashMap)((HashMap)((HashMap)server.getProperties().get("users").get(getUsername())).get("subjects")).get(getContent(payload))).put("score",null);
			((HashMap)((HashMap)((HashMap)server.getProperties().get("users").get(getUsername())).get("subjects")).get(getContent(payload))).put("categories",new HashMap());  
			((HashMap)((HashMap)((HashMap)((HashMap)server.getProperties().get("users").get(getUsername())).get("subjects")).get(getContent(payload))).get("categories")).put("score",null);
			
			((ArrayList)((HashMap)server.getProperties().get("subjects").get(getContent(payload))).get("members")).add(getUsername());
			
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
					subjects+=", ";
				}
			}
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:info\t"
					+ "content:Subjects registered - "+subjects;
			out.println(returnToClient);
		}
			
		else if (getContent(payload).equals("local")){
			String subjects="";
			Iterator subject_iterator = ((HashMap)((HashMap)((HashMap)server.getProperties().get("users")).get(getUsername())).get("subjects")).keySet().iterator();
			while(subject_iterator.hasNext()){
				subjects+=subject_iterator.next();
				if (subject_iterator.hasNext()){
					subjects+=", ";
				}
			}
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:info\t"
					+ "content:Your subjects - "+subjects;
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
	void parse_remove_subject(String payload){//TODO: Not done with remove subject, do this
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
			if(getCurrentSubject().equals(getContent(payload))){setCurrentSubject(null);}
			
			
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
	void parse_get_questions(String payload){
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
	void parse_add_chatroom(String payload){
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
					+ "content:Null";
			out.println(returnToClient);
		}
	}
	void parse_get_username(String payload){
		try{
		String returnToClient= 	"timestamp:"+LocalTime.now().toString()
				+"\tsender:server\t"
				+ "response:info\t"
				+ "content:"+getUsername().toString();
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
	void parse_save_server(String payload){
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
	//add methods to save status on score and grade for each user in each subject
	void parse_get_stats(String payload){ //get overall score for each subject, and score for each category
		//get subject - <overall score> category - score - category - score
		// do this for every subject registered for the user
		
	}
	void parse_add_results(String payload){ //send results of a quiz to the properties of the server, update score
		//need to define subprotocol, maybe content: <category> <points added>
		
		//first regular error checking, user needs to be logged in, current subject not null etc
		//them spimply update the relevevant properties, checking if null, and adding to a counter which keeps track of the number of questions asked
	}
	
	
	void parse_get_questions(){
		if (getCurrentSubject()==null){
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:you need to set the current subject";
			out.println(returnToClient);
			return;
		}
		if (getUsername()==null){
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:you need to log in to use this function";
			out.println(returnToClient);
			return;
		}
		try{
		ArrayList<String> questionList = (ArrayList)((HashMap)server.getProperties().get("subjects").get(getCurrentSubject())).get("questions");
		String content="";
		for(String question : questionList){
			content+=question+"@";
			
		}
		content=content.substring(0, content.length()-2);
		String returnToClient= 	"timestamp:"+LocalTime.now().toString()
				+"\tsender:server\t"
				+ "response:question\t"
				+ "content:"+content;
		out.println(returnToClient);
		}
		catch(NullPointerException e){
			String returnToClient= 	"timestamp:"+LocalTime.now().toString()
					+"\tsender:server\t"
					+ "response:error\t"
					+ "content:No questions to send";
			out.println(returnToClient);
			return;
		}
	}
	
	
	

	
}