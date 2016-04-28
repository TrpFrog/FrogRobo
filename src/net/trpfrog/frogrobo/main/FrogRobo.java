package net.trpfrog.frogrobo.main;

import java.util.ArrayList;

import org.apache.commons.lang3.SystemUtils;

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


public class FrogRobo /*extends Application*/ {

	public static final String FS = SystemUtils.FILE_SEPARATOR;
	public static String FILE_PASS =
			//"home"+FS+"pi"+FS+"Desktop"+FS+"TwitterBot"+FS
			"."+FS
			;

	public static void main(String[] args){
		//Copycat.main();
		//UpdateNamer.main();
		//Spoofing.main(); //なりすまし


		ListenerLoader.load();

		StreamingLoader.start();

		ArrayList<MentionListener> listenerList = new ArrayList<>();
		listenerList.addAll(TweetStream.getInstance().getMentionListenerList());
		listenerList.addAll(TrpFrogUserStream.getInstance().getListenerList());
		for (MentionListener listener : listenerList) {
			System.out.println("☆"+listener.getCommandName()+" ("+listener.getShortCommand()+")");
		}
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