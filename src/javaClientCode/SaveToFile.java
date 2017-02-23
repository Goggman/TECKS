package javaClientCode;

import java.io.IOException;
import java.io.PrintWriter;


public class SaveToFile {

	
	
	
	public void saveFile(String filename, Question q) throws IOException{
				
		PrintWriter p = new PrintWriter(filename);
		
		p.println("Header: " + q.getHeader());
		p.println("c: "+q.getCategory());
		p.println("q: " + q.getQuestionText());
		for (int i = 0; i < q.getOptions().size(); i++){
			p.println(q.getOptions().get(i));
		}
		p.println("a: " + q.getCorrectAnswer());
		p.close();
	}
	
	
	
}
