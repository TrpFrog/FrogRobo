package net.trpfrog.frogrobo.streaming;

public class StreamingLoader {
	public static void start(){
		TweetStream.getInstance().start();
		TrpFrogUserStream.getInstance().start();
	}
}
