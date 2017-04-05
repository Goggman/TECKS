package javaClientCode;
import javafx.scene.control.Label;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Queue;
public class FeedUpdater implements Runnable{
		Thread t;
		ServerClient client;
		Label feed;
		Queue<String> queue;
		FeedUpdater(ServerClient client, Label feed, Queue<String> queue){
			this.client=client;
			this.feed=feed;
			this.queue=queue;
		}


		public void run(){
			while (true){
				try {
					Thread.sleep(800);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				String payload = queue.poll();
				if (payload!=null){
					Platform.runLater(()->{

						feed.setText(feed.getText()+"\n"+client.getSender(payload).toUpperCase()+"\n"+client.getContent(payload));

					});
					
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