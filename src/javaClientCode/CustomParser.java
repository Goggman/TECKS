package javaClientCode;

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
	
	/**
	 * read from file and create a new question object
	 * @param path
	 * @return Arraylist matrix with strings
	 * @throws IOException
	 */
	public ArrayList<ArrayList<String>> OpenFile(String path) throws IOException{
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
		
	
	/**
	 * creates new question object from ArrayList in the ArrayList matrix	
	 * @param qe
	 * @return Question object
	 */
	public Question createQuestion(ArrayList<String> qe){
		
		Question q = new Question();
		
		for (int i = 0; i < qe.size(); i++){
			String current = qe.get(i);
			if (current.contains("Header:")){
				String[] temp = current.split(":");
				q.setHeader(temp[1]);
			}
			else if (current.contains("c: ")){
				String[] temp = current.split(":");
				q.setCategory(temp[1]);
				
			}
			else if (current.contains("q:")){
				String[] temp = current.split(":");
				q.setQuestion(temp[1]);
			}
			else if (current.contains("a:")){
				String[] temp = current.split(":");
				q.setAnswer(temp[1]);
				q.addOptions(temp[1]);
			}
			else if (current.contains("op:")){
				String[] temp = current.split(":");
				q.addOptions(temp[1]);
				
			}
		}	
		return q;
	}
	
	
	
}
