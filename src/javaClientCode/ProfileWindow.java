package javaClientCode;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.Axis;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
public class ProfileWindow implements Window { 
	Stage stage;
	GUIController ctrl;
	ServerClient client;
	FeedUpdater updater1;
	FeedUpdater updater2;
	FeedUpdater updater3;
	FeedUpdater updater4;
	FeedUpdater updater5;
	FeedUpdater updater6;
	TextArea userScore;
	TextArea stats;
	TextArea serverFeed;
	TextArea subjectsGlobal;
	TextArea subjectsLocal;
	TextArea subjectScore;
	MenuButton removeSubject;
	MenuButton setSubject;
	MenuButton addSubject;
	BarChart scoreGraph;
	Stage chat;
	boolean setSubjectNeedsUpdate=true;
	boolean addSubjectNeedsUpdate=true;
	int xBase, yBase;
	Button showSubjectGraph;
	Button resetScore;
	Button requestScore;
	ProfileWindow(Stage stageIn, GUIController ctrlIn, ServerClient clientIn, Stage chatIn){
		stage=stageIn;
		ctrl=ctrlIn;
		client=clientIn;
		chat = chatIn;
	}
	
	public ProfileWindow() {
		
	}

	public Scene createScene(){
		xBase = 0; yBase = 0;
		Pane root = new Pane();

		Label title = new Label("Your Profile");title.setLayoutX(200);title.setLayoutY(50);title.setStyle("-fx-font-size: 30px");
		subjectsLocal = new TextArea();

		subjectsGlobal = new TextArea();
		subjectScore = new TextArea();
		userScore = new TextArea();

		stats = new TextArea(); stats.setLayoutX(xBase+100); stats.setLayoutY(yBase+100);stats.setEditable(false);

		stats.setPrefHeight(150); stats.setPrefWidth(400);
		stats.setEditable(false);
		
		serverFeed = new TextArea(); serverFeed.setLayoutX(xBase+100); serverFeed.setLayoutY(yBase+500); serverFeed.setStyle("-fx-border-color: black"); serverFeed.setPrefSize(400, 100);
		serverFeed.setEditable(false);
		
		addSubject = new MenuButton("addSubject");  addSubject.setLayoutX(xBase+100); addSubject.setLayoutY(yBase+250); addSubject.setPrefWidth(120);
		removeSubject = new MenuButton("RemoveSubject");  removeSubject.setLayoutX(xBase+223); removeSubject.setLayoutY(yBase+250); removeSubject.setPrefWidth(120);
		setSubject = new MenuButton("setSubject"); setSubject.setLayoutX(xBase+346); setSubject.setLayoutY(yBase+250); setSubject.setPrefWidth(120);
		showSubjectGraph = new Button("showGraph"); showSubjectGraph.setLayoutX(xBase+200); showSubjectGraph.setLayoutY(yBase+310);
		resetScore = new Button("resetScore"); resetScore.setLayoutX(xBase+100); resetScore.setLayoutY(yBase+280); setSubject.setPrefWidth(100);
		requestScore = new Button("reqScore"); requestScore.setLayoutX(xBase+206); requestScore.setLayoutY(yBase+280); requestScore.setPrefWidth(100);
		TextField createSubject = new TextField(); createSubject.setLayoutX(xBase+100); createSubject.setLayoutY(yBase+310); createSubject.setPrefWidth(100);
		scoreGraph = new BarChart(new CategoryAxis(), new NumberAxis()); scoreGraph.setLayoutX(xBase+100); scoreGraph.setLayoutY(yBase+350);
		scoreGraph.setVisible(false);	scoreGraph.setPrefSize(300, 100);
		
		MenuButton typeScore = new MenuButton("pickScoreType"); typeScore.setLayoutX(xBase+403);typeScore.setLayoutY(yBase+300);
		MenuItem showUserScore = new MenuItem("userScore");
		showUserScore.setOnAction(e->{
			root.getChildren().remove(scoreGraph);
			scoreGraph=prepareScore(userScore);
			scoreGraph.setVisible(true);
			scoreGraph.setPrefSize(400, 100); scoreGraph.setLayoutX(xBase+100); scoreGraph.setLayoutY(yBase+350);
			root.getChildren().add(scoreGraph);
			
		});
		MenuItem showSubjectScore = new MenuItem("subjectScore"); showSubjectScore.setDisable(true);
		showSubjectScore.setOnAction(e->{
			root.getChildren().remove(scoreGraph);
			scoreGraph=prepareScore(subjectScore);
			scoreGraph.setVisible(true);
			scoreGraph.setPrefSize(400, 100); scoreGraph.setLayoutX(xBase+100); scoreGraph.setLayoutY(yBase+350);
			root.getChildren().add(scoreGraph);
			
		});
		typeScore.getItems().addAll(showUserScore, showSubjectScore);


		setSubject.setOnMouseEntered(e->{
			if (subjectsLocal.getText().trim().split("[;]").length == 1){
				return;
			}
			if (subjectsLocal.getText().trim().split("[;]")[1].isEmpty()){
				return;
			}
			if (setSubjectNeedsUpdate){
				setSubject.getItems().clear();
				removeSubject.getItems().clear();
				String[] subjectArray = subjectsLocal.getText().trim().split("[;]")[1].trim().split("[@]");
				for(String subject : subjectArray){
					MenuItem item = new MenuItem(subject);
					item.setOnAction(x->{
						client.sendMessage("request:set_subject\tcontent:"+new String(subject));
						userScore.clear();
						client.sendMessage("request:get_stats\tcontent:");
					});
					setSubject.getItems().add(item);
					
					MenuItem removeSubjectItem = new MenuItem(subject);
					removeSubjectItem.setOnAction(c->{
						client.sendMessage("request:remove_subject\tcontent:"+new String(subject));
						setSubjectNeedsUpdate=true;
						wakeUp();
						});
					removeSubject.getItems().add(removeSubjectItem);
					
				}
				setSubjectNeedsUpdate=false;
			}
			
		});
		removeSubject.setOnMouseEntered(setSubject.getOnMouseEntered());
		
		
		addSubject.setOnMouseEntered(x->{
			if (subjectsGlobal.getText().trim().isEmpty()){
				return;
			}
			if (addSubjectNeedsUpdate){
				addSubject.getItems().clear();
				String[] subjectArray = subjectsGlobal.getText().trim().split("[@]");
				for(String subject : subjectArray){
					String itemsubject = subject;
					MenuItem item = new MenuItem(subject);
					item.setOnAction(z->{
						client.sendMessage("request:add_subject\tcontent:"+itemsubject);
						wakeUp();
					});
					addSubject.getItems().add(item);
				}
				addSubjectNeedsUpdate=false;
			}
			
		});
		
		
		Button tab1 = new Button("Quiz"); tab1.setLayoutX(92); tab1.setLayoutY(2); 
		tab1.setStyle("-fx-background-color: -fx-outer-border, -fx-inner-border, -fx-body-color; -fx-background-insets: 0, 1, 2;-fx-background-radius: 5, 4, 3;");
		tab1.setPrefWidth(100);

		tab1.setOnAction(e->{
			stage.setScene(ctrl.getScene(1)); //QuestionScene at index 1 in GUIctrl
			
		});
		


		Button tab2 = new Button("Qcreator"); tab2.setLayoutX(196); tab2.setLayoutY(2); 
		tab2.setStyle("-fx-background-color: -fx-outer-border, -fx-inner-border, -fx-body-color; -fx-background-insets: 0, 1, 2;-fx-background-radius: 5, 4, 3;");

		tab2.setPrefWidth(100);
		tab2.setOnAction(e->{
			stage.setScene(ctrl.getScene(2));
		});
		

		

		Button tab3 = new Button("Login"); tab3.setLayoutX(300); tab3.setLayoutY(2); 
		tab3.setStyle("-fx-background-color: -fx-outer-border, -fx-inner-border, -fx-body-color; -fx-background-insets: 0, 1, 2;-fx-background-radius: 5, 4, 3;");

		tab3.setPrefWidth(100);
		tab3.setOnAction(e->{
			stage.setScene(ctrl.getScene(4));
			
		});

		Button tab4 = new Button("Profile"); tab4.setLayoutX(404); tab4.setLayoutY(2); 
		tab4.setStyle("-fx-background-color: -fx-outer-border, -fx-inner-border, -fx-body-color; -fx-background-insets: 0, 1, 2;-fx-background-radius: 5, 4, 3;");

		tab4.setPrefWidth(100);
		tab4.setOnAction(e->{
			stage.setScene(ctrl.getScene(5));
		});

		Button showChat = new Button("ShowChat"); showChat.setLayoutX(502);showChat.setLayoutY(543);
		showChat.setStyle("-fx-pref-width: 95");
		showChat.setOnAction(e->{
			ctrl.chat.wakeUp();
			chat.show();
			
		});
		Button hideChat = new Button("HideChat"); hideChat.setLayoutX(502);hideChat.setLayoutY(573);
		hideChat.setStyle("-fx-pref-width: 95");
		hideChat.setOnAction(e->{
			ctrl.chat.sleep();
			chat.hide();
		});
		


		
		createSubject.setPromptText("createSubject");
		createSubject.setStyle("-fx-pref-width: 150");
		createSubject.setOnAction(e->{
			client.sendMessage("request:create_subject\tcontent:"+createSubject.getText());
			wakeUp();
		});

		resetScore.setOnAction(e->{
			client.sendMessage("request:reset_score\tcontent:local");
			
		});
		
		



		root.getChildren().addAll(stats, serverFeed, addSubject, showChat, hideChat, createSubject, resetScore, removeSubject, setSubject, tab1, tab2, tab3, tab4, requestScore, scoreGraph, typeScore);

		
		
		updater1 = new FeedUpdater(client, stats, client.ProfileWindowStats);
		updater1.start();
		updater2 = new FeedUpdater(client, serverFeed, client.ProfileWindow);
		updater2.start();
		updater3 = new FeedUpdater(client, subjectsGlobal, client.ProfileWindowSubjectsGlobal);
		updater3.start();
		updater4 = new FeedUpdater(client, userScore, client.ProfileWindowUserScore);
		updater4.start();
		updater5 = new FeedUpdater(client, subjectsLocal, client.ProfileWindowSubjectsLocal);
		updater5.start();
		updater6 = new FeedUpdater(client, subjectScore, client.ProfileWindowSubjectScore);
		updater6.start();
		Scene scene = new Scene(root, 600, 600);
		scene.getStylesheets().add(getClass().getResource("GUI.css").toExternalForm());
		

	return scene;
	}
	
	public BarChart<String, Number> prepareScore(TextArea score){
		if (score.getText().trim().isEmpty()){
			return new BarChart(new CategoryAxis(), new NumberAxis());
		}
			CategoryAxis xAxis = new CategoryAxis();
			NumberAxis yAxis = new NumberAxis(0, 1, 0.1); 
			BarChart<String, Number> scoreGraphLocal = new BarChart<>(xAxis, yAxis);
			
			
			HashMap<String, HashMap<String, Double>> subjectMap = new HashMap<>();
		
			String[] scoreBySubject = score.getText().split("[@]");
			System.out.println("contents: "+score.getText());
			for (String subjectScore : scoreBySubject){
				HashMap<String, Double> categoryScoreMap = new HashMap<>();
				String[] rawScoreArray = subjectScore.split("[|]");
				String subject=rawScoreArray[0];
				double scoreSubject = Double.parseDouble(rawScoreArray[1]);
			
				categoryScoreMap.put("Overall score", scoreSubject);
				if (rawScoreArray.length>2){
					for (int x=2;x<rawScoreArray.length;x+=2){
						try{
							categoryScoreMap.put(rawScoreArray[x], Double.parseDouble(rawScoreArray[x+1]));
						}
						catch(ArrayIndexOutOfBoundsException e){
							categoryScoreMap.put(rawScoreArray[x], Double.parseDouble("0"));
						}
						catch(NumberFormatException xz){
							categoryScoreMap.put(rawScoreArray[x], Double.parseDouble("0"));
						}
					}
					subjectMap.put(subject, categoryScoreMap);
				}
				else{
					HashMap<String, Double> categoryScoreMapAlt = new HashMap<>();
					categoryScoreMapAlt.put("Overall Score", scoreSubject);
					subjectMap.put(subject, categoryScoreMapAlt);
				}

		
			}
				for(String subjectKey : subjectMap.keySet() ){
					XYChart.Series<String, Number> series = new XYChart.Series<>();
					for(String category : subjectMap.get(subjectKey).keySet()){
						series.getData().add(new XYChart.Data<String, Number>(category, subjectMap.get(subjectKey).get(category)));
					}
					series.setName(subjectKey);
					scoreGraphLocal.getData().add(series);  scoreGraphLocal.setAnimated(false);  scoreGraphLocal.setLegendVisible(true);
				}
		
		return scoreGraphLocal;
	}
	
	public boolean getSubjectNeedsUpdate(){
		return setSubjectNeedsUpdate;
	}
	
	public boolean getAddSubjectNeedsUpdate(){
		return addSubjectNeedsUpdate;
	}
	
	public void wakeUp(){
		stats.setText("Your stats:");	
		serverFeed.setText("Messages from server:");
		subjectsGlobal.clear();
		subjectsLocal.clear();
		subjectScore.clear();
		userScore.clear();
		setSubject.getItems().clear();
		removeSubject.getItems().clear();
		addSubject.getItems().clear();
		setSubjectNeedsUpdate=true;
		addSubjectNeedsUpdate=true;
		client.sendMessage("request:get_username\tcontent:");
		client.sendMessage("request:get_type\tcontent:");
		client.sendMessage("request:get_subjects\tcontent:local");
		client.sendMessage("request:get_subjects\tcontent:global");
		client.sendMessage("request:get_stats\tcontent:");
		//client.sendMessage("request:get_subject_scores\tcontent:");
		
		
	}
	public void sleep(){
		
	}
}
