package javaClientCode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
public class GUIData {
	private String textField;
	private String textLabel;
	private GUI gui;
	private ArrayList<Listener> listeners;
	
	
	
	GUIData(GUI GUI, Listener...objects){
		textField="";
		textLabel="";
		gui=GUI;
		listeners = new ArrayList<Listener>();
		for (int x=0;x<objects.length;x++){
			listeners.add(objects[x]);
			
		}
		
	}
	
	void setTextField(String string){
		textField=string;
		gui.update();
		notifyListeners();
	}
	String getTextField(){
		return textField;
	}
	void setTextLabel(String string){
		int count=0;
		for (int x=0; x<textLabel.length(); x++){
			if (textLabel.charAt(x) == '\n'){
				count++;
			}
		}
		if (count>15){
			textLabel="";
		}
		textLabel=textLabel+string+"\n";
		gui.update();
		//notifyListeners();
	}
	String getTextLabel(){
		return textLabel;
	}
	
	void notifyListeners(){
		Iterator<Listener> it=listeners.iterator();
		int i = 0;
		while (it.hasNext()){
			it.next().trigger();
			i++;
			System.out.println("NOTIFIED"+i);
			
			
		}
	}
	void addListener(Listener listener){
		if (!listeners.contains(listener))
		{listeners.add(listener);}
		
	}
}
