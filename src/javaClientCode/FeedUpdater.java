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
			//	try {
			//		this.wait(1000000);
			//	} catch (InterruptedException e) {
					// TODO Auto-generated catch block
			//		e.printStackTrace();
			//	}
				for(long x=0;x<10000000;x++){
					//Wait
				}
				String payload = queue.poll();
				if (payload!=null){
					Platform.runLater(()->{
						feed.setText(feed.getText()+"\n"+client.get_sender(payload)+"\n"+client.get_content(payload));
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