package net.trpfrog.frogrobo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import javafx.application.Application;
//import javafx.geometry.Orientation;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.CheckBox;
//import javafx.scene.control.Label;
//import javafx.scene.control.Tooltip;
//import javafx.scene.layout.BorderPane;
//import javafx.scene.layout.FlowPane;
//import javafx.scene.text.Font;
//import javafx.stage.Stage;
import net.trpfrog.frogrobo.streaming.MentionListener;
import net.trpfrog.frogrobo.streaming.StreamingLoader;
import net.trpfrog.frogrobo.streaming.TrpFrogUserStream;
import net.trpfrog.frogrobo.streaming.TweetStream;
import net.trpfrog.frogrobo.update_name.UpdateNamerSetter;
import twitter4j.TwitterException;


public class FrogRobo /*extends Application*/ {

	public static final String FS = File.separator;
	private static String jarPath = System.getProperty("java.class.path");
	public static final String FILE_PATH = getFilePath();

	private static boolean macOS = false;

	public static boolean isMacOS() {
		return macOS;
	}

	private static String getFilePath(){
		if (System.getProperty("os.name").equals("Mac OS X")){
			macOS = true;
			return "."+FS;
		}else{
			return jarPath.substring(0, jarPath.lastIndexOf(File.separator)+1);
		}
	}



	public static final int TRPFROG_USER_ID = 92482871;

	public static void main(String[] args){
		//Copycat.main();
		UpdateNamerSetter.main();
		//Spoofing.main(); //なりすまし

		ListenerLoader.load();

		StreamingLoader.start();


		TweetStream.getInstance().getMentionListenerMap()
				.forEach((k,v)-> System.out.println("☆"+k));
		TrpFrogUserStream.getInstance().getMentionListenerMap()
				.forEach((k,v)-> System.out.println("☆"+k));

		MainFrame.createFrame();
		//launch(args);
	}
//	@Override
//
//	public void start(Stage primaryStage) throws Exception {
//
//		primaryStage.setTitle("つまみロボ alpha");
//		primaryStage.centerOnScreen();
//		primaryStage.setOnCloseRequest(event->{
//			System.exit(0);
//		});
//
//		BorderPane labelPane = new BorderPane();
//
//		Label titleLabel = new Label();
//		titleLabel.setFont(new Font(STYLESHEET_CASPIAN, 25.0));
//		titleLabel.setText("FrogRobo");
//
//		labelPane.setTop(new Label(""));
//		labelPane.setCenter(titleLabel);
//		labelPane.setBottom(new Label(""));
//
//		FlowPane checkboxesPane = new FlowPane();
//		checkboxesPane.setOrientation(Orientation.VERTICAL);
//
//		ArrayList<MentionListener> listenerList = new ArrayList<>();
//		listenerList.addAll(TweetStream.getInstance().getMentionListenerList());
//		listenerList.addAll(TrpFrogUserStream.getInstance().getListenerList());
//
//		Button applyButton = new Button();
//		Button checkboxTogglerButton = new Button();
//		ArrayList<CheckBox> commandsCheckBoxes = new ArrayList<>();
//
//
//		int i = 0;
//		for (MentionListener listener : listenerList) {
//			commandsCheckBoxes.add(new CheckBox());
//			commandsCheckBoxes.get(i)
//				.setText(listener.getCommandName()+" ("+listener.getShortCommand()+") : "+listener.getCommandDescription());
//			commandsCheckBoxes.get(i).setTooltip(new Tooltip
//					("Usage: "+listener.getCommandUsage()));
//			commandsCheckBoxes.get(i).setSelected(true);
//			commandsCheckBoxes.get(i).setOnAction(event->{
//				applyButton.setDisable(false);
//				checkboxTogglerButton.setText("Deselect all");
//			});
//			checkboxesPane.getChildren().add(commandsCheckBoxes.get(i));
//			i++;
//		}
//
//		BorderPane buttonPane = new BorderPane();
//
//		applyButton.setText("Apply");
//		applyButton.setDisable(true);
//
//		ArrayList<Object[]> objects = new ArrayList<>();
//		for(int j=0;i<listenerList.size();i++){
//			Object[] objectArray = {listenerList.get(j),commandsCheckBoxes.get(j)};
//			objects.add(objectArray);
//		}
//
//		applyButton.setOnAction(event->{
//			for (Object[] object : objects) {
//				((MentionListener) object[0]).setDisable(!((CheckBox) object[1]).isSelected());
//			}
//			applyButton.setDisable(true);
//		});
//		buttonPane.setLeft(applyButton);
//
//		checkboxTogglerButton.setText("Deselect all");
//		checkboxTogglerButton.setOnAction(action -> {
//			boolean allChecked = true;
//			for (CheckBox checkbox : commandsCheckBoxes) {
//				if (checkbox.isSelected() == false)
//					continue;
//				allChecked = false;
//			}
//			for (CheckBox checkbox : commandsCheckBoxes) {
//				checkbox.setSelected(allChecked);
//			}
//			if (allChecked) {
//				checkboxTogglerButton.setText("Deselect all");
//			} else {
//				checkboxTogglerButton.setText("Select all");
//			}
//			applyButton.setDisable(false);
//		});
//
//
//
//		Button exitButton = new Button("Exit");
//		exitButton.setOnAction(action->{
//			System.exit(0);
//		});
//		buttonPane.setCenter(exitButton);
//
//		buttonPane.setRight(checkboxTogglerButton);
//
//		BorderPane brankPane = new BorderPane();
//		brankPane.setCenter(new Label("　"));
//
//		BorderPane mainPane = new BorderPane();
//		mainPane.setTop(labelPane);
//		mainPane.setCenter(checkboxesPane);
//		mainPane.setBottom(buttonPane);
//		mainPane.setLeft(brankPane);
//
//
//		Scene scene = new Scene(mainPane,450,330);
//		primaryStage.setScene(scene);
//		primaryStage.show();
//	}
}