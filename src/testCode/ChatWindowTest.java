package testCode;

import javaClientCode.ChatWindow;
import javaClientCode.ServerClient;
import javaServerCode.ServerHost;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import junit.framework.TestCase;

public class ChatWindowTest extends TestCase {
	Stage chatStage;

	public void test_chat(){
		chatStage = new Stage();
		ServerHost server = new ServerHost();
		ServerClient client = new ServerClient();
		ChatWindow chat = new ChatWindow(chatStage, client);
		Scene scene = chat.createScene();
		chat.text.setText("test");
		chat.text.fireEvent(new ActionEvent());
		System.out.println(chat.messageFeed.getText());
		assertTrue(chat.text.getText().isEmpty());
		//assertEquals(" ", chat.messageFeed.getText());
		
	}
}
