package javaServerCode;
import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.time.LocalTime;
import java.util.Iterator;
public class ServerHost {
	
	//Message format from client
	//request:
	//content:
	//request:<>\tcontent:<>
	//
	//from server
	//timestamp:
	//sender:
	//response:
	//content:
	//timestamp:<>\tsender:<>\tresponse:<>\tcontent:<>
	
	
	
	
	HashMap<String,HashMap> properties;
	int portNumber;
	ServerSocket serverSocket;
    PropertiesHandler prop=new PropertiesHandler();
    
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
	
		properties=prop.loadProperties();
		if (properties == null){
			System.out.println("Creating new properties");
			properties=new HashMap<String,HashMap>();
			properties.put("connected_clients", new HashMap<ClientHandler, String>());
			properties.put("users", new HashMap<String, HashMap<String, String>>());
			properties.put("chatrooms", new HashMap<String, ArrayList>());
			properties.get("chatrooms").put("start", new HashMap<String, ArrayList<String>>());
			HashMap start= (HashMap)properties.get("chatrooms").get("start");
			start.put("log",new ArrayList<String>());
			start.put("users", new HashMap<ClientHandler,HashMap>());
		
			properties.put("subjects", new HashMap<String, HashMap<String, ArrayList<String>>>());
		
		
		
			properties.put("help",new HashMap());
			properties.get("help").put("text", "TODO - add helptext, useful methods, commands, etc");
		}
		else{
			System.out.println("Found Properties in file");
		}
		
	}
	
	void serve_forever(){
		while(true){
			
			try {
				ClientHandler handler = new ClientHandler(this, serverSocket.accept());
				handler.start();
				properties.get("connected_clients").put(handler, "");
				System.out.println("Client connected");
			} catch (IOException e) {
				System.out.println("Could not connect in serve forever");
				break;
			}
			
		}
		
	}
	HashMap<String,HashMap> getProperties(){
		return properties;
	}
	public static void main(String[] args) {
		ServerHost server = new ServerHost();
		server.serve_forever();
	}	
	


}
