package javaClientCode;
import javafx.scene.control.Label;

public class FeedUpdater implements Runnable{
		Thread t;
		ServerClient client;
		Label feed;
		FeedUpdater(ServerClient client, Label feed){
			this.client=client;
			this.feed=feed;
		}
		public void run(){
			while (true){
				String newMessage = client.messageIn.poll();
				if (newMessage!=null){
					feed.setText(feed.getText()+"\n"+newMessage);
					
				}
					
			}
		}
		public void start(){
			if (this.t==null){
				this.t= new Thread(this);
				t.setDaemon(true);
				t.start();
			}
		}
		
	}