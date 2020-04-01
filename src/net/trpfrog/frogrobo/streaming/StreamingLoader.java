package net.trpfrog.frogrobo.streaming;

import net.trpfrog.frogrobo.FrogRobo;
import twitter4j.TwitterException;
import twitter4j.User;

import java.io.IOException;

public class StreamingLoader {
	public static void start(){
		TweetStream robo = TweetStream.getInstance();
		robo.start();
		User robotwi = null;
		try {
			robotwi = TweetStream.getInstance().getTwitter().verifyCredentials();
		} catch (TwitterException e) {
			e.printStackTrace();
		}
//		try {
//			if(System.getProperty("os.name").equals("Mac OS X")) {
//				robo.getTwitter().updateProfile("つまみロボ(テスト中)", robotwi.getURL(), robotwi.getLocation(), robotwi.getDescription());
//			}else{
//				robo.getTwitter().updateProfile("つまみロボ", robotwi.getURL(), robotwi.getLocation(), robotwi.getDescription());
//			}
//		} catch (TwitterException e) {
//			e.printStackTrace();
//		}

		TrpFrogUserStream tus = TrpFrogUserStream.getInstance();
		tus.postCloseMsg = false;
		tus.start();
	}
}
