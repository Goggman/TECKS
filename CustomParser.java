package TECKS;


import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class CustomParser {

	private String path;
	
	public void ReadFile(){
		path = "C:/users/martin/tdt4100-2017-master/ws/PU_GUI/src/TECKS/thing.txt";
		
	}
	
	public ArrayList<ArrayList<String>> OpenFile() throws IOException{
		FileReader fr = new FileReader(path);
		BufferedReader textReader = new BufferedReader(fr);
		
		
		ArrayList<ArrayList<String>> textData = new ArrayList<>();
		int i = -1;
		 
		while (textReader.ready()){
			String next = textReader.readLine();
			if (next.contains("Header:")){
				textData.add(new ArrayList<String>());
				i++;
			}
			//System.out.println(next);
			textData.get(i).add(next);
			
		}
		
		textReader.close();
		return textData;
		
	}
		
	
	
	public static void main(String[] args) {
		CustomParser fcuk = new CustomParser();
		fcuk.ReadFile();
		try {
			ArrayList<ArrayList<String>> out = fcuk.OpenFile();
			for (int i = 0; i < out.size(); i++){
				
				//System.out.println(out.get(i));
				
				Question q = fcuk.createQuestion(out.get(i));
				System.out.println(q.getHeader());
				System.out.println(q.getQuestion());
				System.out.println(q.getAnswer());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public CustomParser() {
		// TODO Auto-generated constructor stub
	}
	
	public Question createQuestion(ArrayList<String> qe){
		
		
		Question q = new Question();
		
		for (int i = 0; i < qe.size(); i++){
			String current = qe.get(i);
			if (current.contains("Header:")){
				String[] temp = current.split(":");
				q.setHeader(temp[1]);
			}
			else if (current.contains("q:")){
				String[] temp = current.split(":");
				q.setQuestion(temp[1]);
			}
			else if (current.contains("a:")){
				String[] temp = current.split(":");
				q.setAnswer(temp[1]);
			}
		}
		
		
		
		
		
		
		return q;
	}
	
	
	
}
