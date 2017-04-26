package javaServerCode;
import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.Set;
public class PropertiesHandler {
	//String path= System.getProperties().getProperty("user.dir") +"/TECKS/src/javaServerCode/serverSave.ser";
	String path= "serverSave.ser";
	
	public PropertiesHandler(){
		
	}
	
	public HashMap purgeMap(HashMap map){								//helper function, clears the map from non .ser -able objects (objects that cannot be written to file easily), particularly clienthandler objects, which are to be found in the properties map
		HashMap newmap = new HashMap<String, HashMap>();
		newmap.put("users", (HashMap)map.get("users"));
		newmap.put("chatrooms", new HashMap());
		
		Iterator keyit = ((HashMap)map.get("chatrooms")).keySet().iterator();
		while(keyit.hasNext()){
			String next = (String)keyit.next();
			((HashMap) newmap.get("chatrooms")).put(next, new HashMap());
			HashMap chatroom = (HashMap)((HashMap)newmap.get("chatrooms")).get(next);
			chatroom.put("log", ((HashMap)((HashMap)map.get("chatrooms")).get(next)).get("log"));
			chatroom.put("users", new HashMap());
		}
		
		newmap.put("subjects", (HashMap)map.get("subjects"));
		newmap.put("connected_clients", new HashMap<ClientHandler, String>());
		newmap.put("help", map.get("help"));
		
		
		return newmap;
	}
	public void saveProperties(HashMap map) throws Exception{
		ObjectOutputStream oos=null;
		FileOutputStream fout=null;
		try{
		    fout = new FileOutputStream(path);
		    oos = new ObjectOutputStream(fout);
		    oos.writeObject(purgeMap(map));
		} catch (Exception ex) {
		    ex.printStackTrace();
			throw (new Exception("savePropertiesError, did not save to file"));
			
		} finally {
		    if(oos  != null){
		    	try{
		    		oos.close();
		    	}
		        catch (IOException e){
		        	e.printStackTrace();
		        }
		    	
		    } 
		    
		}
		
	}
	public HashMap<String, HashMap> loadProperties(){
		ObjectInputStream objectinputstream = null;
		FileInputStream streamIn=null;
		try {
		    streamIn = new FileInputStream(path);
		    objectinputstream = new ObjectInputStream(streamIn);
		    HashMap readCase = (HashMap) objectinputstream.readObject();
		    return readCase;
		} catch (Exception e) {
		    //e.printStackTrace();
			System.out.println("Properties not loaded");
		} finally {
		    if(objectinputstream != null){
		        try {
					objectinputstream .close();
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("Not able to close objectinputstream");
				}
		    } 
		}
		return null;
	}
	
	public String getPath(){
		return path;
	}
}
