package TECKS;


import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class CustomParser {

	private String path = "C:/users/martin/tdt4100-2017-master/ws/PU_GUI/src/TECKS/";
	
	
	public void ReadFile(String filename){
		path += filename;
		
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
		fcuk.ReadFile("thing.txt");
		Question q;
		try {
			ArrayList<ArrayList<String>> out = fcuk.OpenFile();
			for (int i = 0; i < out.size(); i++){
				
				//System.out.println(out.get(i));
				
				q = fcuk.createQuestion(out.get(i));
				System.out.println(q.getHeader());
				System.out.println(q.getQuestion());
				System.out.println(q.getAnswer());
				for (int j = 0; j < q.getOptions().size(); j++){
					System.out.println(q.getOptions().get(j));
				}
				
				System.out.println("save to file: ");
				Scanner in = new Scanner(System.in);
				String fileName = "C:/users/martin/tdt4100-2017-master/ws/PU_GUI/src/TECKS/" + in.next();
				
				SaveToFile stf = new SaveToFile();
				stf.saveFile(fileName, q);
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
				q.setOptions(temp[1]);
			}
			else if (current.contains("op:")){
				String[] temp = current.split(":");
				q.setOptions(temp[1]);
				
			}
		}	
		return q;
	}
	
	
	
}
