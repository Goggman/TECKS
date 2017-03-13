package javaServerCode;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
public class Testing {
	String path= "C:/Users/Fritz Olav/Desktop/PU_GROUP88/workspace_TECHS_88/TECKS/src/javaServerCode/testSave.ser";
	
	
	void load(){
		ObjectInputStream objectinputstream = null;
		FileInputStream streamIn=null;
		try {
		    streamIn = new FileInputStream(path);
		    objectinputstream = new ObjectInputStream(streamIn);
		    HashMap readCase = (HashMap) objectinputstream.readObject();
		    System.out.println(""+((HashMap)readCase.get("test")).get("test"));
		    //map= readCase;
		} catch (Exception e) {
		    e.printStackTrace();
			//System.out.println("Properties not loaded from file");
		} finally {
		    if(objectinputstream != null){
		        try {
					objectinputstream .close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		    } 
		}
	}
	void save(){
		ObjectOutputStream oos=null;
		FileOutputStream fout=null;
		HashMap map = new HashMap();
		map.put("test", new HashMap());
		((HashMap)map.get("test")).put("test", "another level deeper");
		try{
		    fout = new FileOutputStream(path);
		    oos = new ObjectOutputStream(fout);
		    oos.writeObject(map);
		    
		} catch (Exception ex) {
		    ex.printStackTrace();
		} finally {
		    if(oos  != null){
		    	try{
		    		oos.close();
		    	}
		        catch (IOException e){
		        	e.printStackTrace();
		}}}}

	
	
public static void main(String[] args){
	Testing test = new Testing();
	test.save();
	test.load();
	
	
}
}
