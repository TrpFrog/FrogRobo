package net.trpfrog.frogrobo.streaming;

import net.trpfrog.frogrobo.FrogRobo;
import twitter4j.TwitterException;

import java.io.IOException;

public class StreamingLoader {
	public static void start(){
		TweetStream robo = TweetStream.getInstance();
		robo.start();

		try {
			if(System.getProperty("os.name").equals("Mac OS X")) {
				robo.getTwitter().updateProfile("つまみロボ [テスト中]", null, null, null);
			}else{
				robo.getTwitter().updateProfile("つまみロボ", null, null, null);
			}
		} catch (TwitterException e) {
			e.printStackTrace();
		}

		TrpFrogUserStream tus = TrpFrogUserStream.getInstance();
		tus.postCloseMsg = false;
		tus.start();
	}
}
