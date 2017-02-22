package TECKS;

import java.io.IOException;
import java.io.PrintWriter;


public class SaveToFile {

	
	
	
	public void saveFile(String filename, Question q) throws IOException{
				
		PrintWriter p = new PrintWriter(filename);
		
		p.println("Header: " + q.getHeader());
		p.println("q: " + q.getQuestion());
		for (int i = 0; i < q.getOptions().size(); i++){
			p.println(q.getOptions().get(i));
		}
		p.println("a: " + q.getAnswer());
		p.close();
	}
	
	
	
}
