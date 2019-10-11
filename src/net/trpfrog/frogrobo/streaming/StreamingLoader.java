package net.trpfrog.frogrobo.streaming;

import twitter4j.TwitterException;

import java.io.IOException;

public class StreamingLoader {
	public static void start(){
		TweetStream.getInstance().start();
		TrpFrogUserStream tus = TrpFrogUserStream.getInstance();
		tus.postCloseMsg = false;
		tus.start();
	}
}
