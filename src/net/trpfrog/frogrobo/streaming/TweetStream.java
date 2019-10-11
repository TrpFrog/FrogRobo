package net.trpfrog.frogrobo.streaming;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

import net.trpfrog.frogrobo.FrogRobo;
import net.trpfrog.frogrobo.autoreply.AutoReply;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterStreamFactory;

public class TweetStream extends CommandStreaming {

	public static final long FROGROBO_USER_ID = 2744579940L;

	private static TweetStream frogrobo;

	public static TweetStream getInstance(){
		if(frogrobo==null){
			try {
				frogrobo = new TweetStream();
			} catch (TwitterException e) {
				System.err.println("TweetStreamインスタンスの取得に失敗しました");
				e.printStackTrace();
				System.exit(1);
			} catch (IOException e) {
				System.err.println("TweetStreamインスタンスの取得に失敗しました(IO)");
				e.printStackTrace();
				System.exit(1);
			}
		}
		return frogrobo;
	}

	@Deprecated
	private List<MentionListener> mentionListenerList = new ArrayList<>(); //追加はFrogRobo.javaでやっている...？


	private TweetStream() throws TwitterException, IOException {
		super(keyFileReader("FrogRoboAPIKeys.txt"));
		super.applyStream();
	}

	/**
	 * Stream起動時の動作を設定する。ここでリスナーに流れを書く。
	 */
	protected void doing(){
		this.stream.addListener(new StatusListener(){

			@Override
			public void onException(Exception arg0) {}
			@Override
			public void onDeletionNotice(StatusDeletionNotice arg0) {}
			@Override
			public void onScrubGeo(long arg0, long arg1) {}
			@Override
			public void onStallWarning(StallWarning arg0) {}
			@Override
			public void onTrackLimitationNotice(int arg0) {}

			@Override
			public void onStatus(Status status) {

				streamListenerList.forEach(listener->listener.allTweet(status));

				System.out.println("["+status.getUser().getName()+"("+status.getUser().getScreenName()+")]");
				System.out.println(status.getText());

				tweetAction(status);

				System.out.println("[exit from onStatus]");
			}
		});

		super.stream.filter("@frogrobo");

		try{
			super.getTwitter().updateStatus("ロボを起動しました on "+System.getProperty("os.name")+"\n["+new Date()+"]");
		}catch(TwitterException e){
			e.printStackTrace();
		}
		System.out.println("[FrogRobo - Streaming started]");
		System.out.println("[ListenerCount:"+mentionListenerMap.size()+"]");

		mentionListenerMap.forEach((k,v)->{
			System.out.println(v.getClass().getSimpleName());
		});
	}

	@Deprecated
	public List<MentionListener> getMentionListenerList(){
		return this.mentionListenerList;
	}

}
