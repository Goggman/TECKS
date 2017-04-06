package javaClientCode;


public class TerminalToServer {
	
public static void main(String[] args){
	//launch();
	ServerClient client = new ServerClient();
	client.listen();
	}
}