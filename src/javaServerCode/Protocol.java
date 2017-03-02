package javaServerCode;
import java.util.ArrayList;
import java.util.HashMap;
public class Protocol {
	ServerHost server;
	Protocol(ServerHost serverIn){
		server=serverIn;
		
		
	}
	
	String[] processInput(String data){
		String[] rows= data.split("\n");
		ArrayList<String[]> lines = new ArrayList<String[]>();
		for(int x=0;x<rows.length;x++){
			lines.add(rows[x].split(" "));
		}
		
		if (lines.get(0)[0].equals("GET")){
			if (lines.get(0)[1].equals("USER")){
				HashMap userMap = (HashMap) server.data.get("users");
						HashMap userData = (HashMap) userMap.get(lines.get(0)[2]);
						String userString = lines.get(0)[2]+userData.get("pw")+userData.get("subject")+userData.get("type");
				return new String[]{"200 - OK", userString};
			}
			else if (lines.get(0)[1].equals("QUIZ")){
				return new String[]{"200 - OK", "Command: GET QUIZ "+lines.get(0)[2]};
			}
			
			
			
		}
		else if (lines.get(0)[0].equals("SAVE")){
			if (lines.get(0)[1].equals("USER")){
				server.createUser(new String[]{
						lines.get(0)[2], lines.get(0)[3], lines.get(0)[4]
				});
				return new String[]{"200 - OK", "Command: SAVE USER "+lines.get(0)[2]};
			}
			else if (lines.get(0)[1].equals("QUESTION")){
				
				server.createQuestion(""+lines.get(0)[2]);
				return new String[]{"200 - OK", "Command: SAVE QUESTION "+lines.get(0)[2]};
			}
		}
		return new String[]{"400 - Bad request", ""};
	}
	
	

}
