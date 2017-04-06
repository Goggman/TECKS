package javaClientCode;
import javafx.scene.control.Label;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import java.io.IOException;
import java.util.Queue;
public class FeedUpdater implements Runnable{
		Thread t;
		ServerClient client;
		TextArea feed;
		Queue<String> queue;
		FeedUpdater(ServerClient client, TextArea feed, Queue<String> queue){
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
<<<<<<< HEAD

						feed.setText(feed.getText()+"\n"+client.getSender(payload).toUpperCase()+"\n"+client.getContent(payload));

=======
						feed.setText(feed.getText()+"\n"+payload);
>>>>>>> 2b06fef36e07d65168544f82c9b7112bdde414a8
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
		
		void sleep(){
			try {
				this.wait();
			}
			catch (InterruptedException e){
				
			}
		}
		
	}