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
			System.out.println("feedupdater started");
			while (true){
				for(int x=0;x<1000000000;x++){
					//Wait for message
				}
				String newMessage = client.serverIn.poll();
				if (newMessage!=null){
					//System.out.println("feedupdater got this message: "+newMessage);
					feed.setText(newMessage);
					break;
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