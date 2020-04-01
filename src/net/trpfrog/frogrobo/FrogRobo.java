package net.trpfrog.frogrobo;

import java.io.File;
import java.io.IOException;
import java.util.*;

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
import net.trpfrog.frogrobo.autoreply.AutoReply;
import net.trpfrog.frogrobo.settings.BlackListListener;
import net.trpfrog.frogrobo.streaming.*;
import net.trpfrog.frogrobo.update_name.UpdateNamerSetter;
import twitter4j.Status;
import twitter4j.TwitterException;

import static net.trpfrog.frogrobo.streaming.CommandStreaming.splitCommands;


public class FrogRobo /*extends Application*/ {

	public static final String FS = File.separator;
	private static String jarPath = System.getProperty("java.class.path");
	public static final String FILE_PATH = getFilePath();

	private static boolean macOS = false;

	public static boolean isMacOS() {
		return macOS;
	}

	private static String getFilePath() {
		if (System.getProperty("os.name").equals("Mac OS X")) {
			macOS = true;
			return "." + FS;
		} else {
			return jarPath.substring(0, jarPath.lastIndexOf(File.separator) + 1);
		}
	}

	public static final int TRPFROG_USER_ID = 92482871;
	public static final long FROGROBO_USER_ID = 2744579940L;

	public static final boolean TEST_MODE = true;
	public static final boolean GUI_MODE = false;

	public static void main(String[] args) {
		//load listeners
		ListenerLoader.load();

		//load update_name listener
		UpdateNamerSetter.main();

		//start streaming
		if(!TEST_MODE) StreamingLoader.start();

		//print available commands on console
		TweetStream.getInstance().getMentionListenerMap()
				.forEach((k, v) -> System.out.println("☆" + k));
		TrpFrogUserStream.getInstance().getMentionListenerMap()
				.forEach((k, v) -> System.out.println("☆" + k));

		if(GUI_MODE)  MainFrame.createFrame();
		if(TEST_MODE) getTweetFromCommandLine();
	}

	private static void getTweetFromCommandLine(){
		Scanner sc = new Scanner(System.in);
		var mentionListenerMap = TweetStream.getInstance().getMentionListenerMap();
		while (true) {
			String str = "@FrogRobo " + sc.nextLine();
			Status status = TweetGeneratorForTest.generate(str);

			String[] commands = splitCommands(str);
			String firstCmd = commands[1].trim().toLowerCase();

			if (mentionListenerMap.containsKey(firstCmd)) {
				MentionListener listener = mentionListenerMap.get(firstCmd);
				listener.whenReplied(status, commands);
			}
		}
	}
}