package net.trpfrog.frogrobo.streaming;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import net.trpfrog.frogrobo.FrogRobo;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterStreamFactory;

public class TrpFrogUserStream extends CommandStreaming{

	private static TrpFrogUserStream trpfrog;

	private List<MentionListener> listenerList = new ArrayList<>();

	private TrpFrogUserStream() throws IOException, TwitterException {
		super(keyFileReader("TrpFrogAPIKeys.txt"));
		super.applyStream();
	}

	public static TrpFrogUserStream getInstance(){
		if(trpfrog==null){
			try {
				trpfrog = new TrpFrogUserStream();
			} catch (IOException e) {
				System.err.println("TrpFrogUserStreamインスタンスの取得に失敗しました(IO)");
				e.printStackTrace();
				System.exit(1);
			} catch (TwitterException e) {
				System.err.println("TrpFrogUserStreamインスタンスの取得に失敗しました");
				e.printStackTrace();
				System.exit(1);
			}
		}
		return trpfrog;
	}

	protected void doing(){
		try {
			stream.addListener(new StatusListener(){

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

				String lowerScreenName = "@"+trpfrog.getTwitter().getScreenName().toLowerCase();
				@Override
				public void onStatus(Status status) {

					streamListenerList.forEach(listener->listener.allTweet(status));

					System.out.println("["+status.getUser().getName()+"("+status.getUser().getScreenName()+")]");
					System.out.println(status.getText());

					tweetAction(status);

					System.out.println("[exit from onStatus]");
				}
			});
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		stream.filter("@trpfrog");
		System.out.println("[TrpFrog - UserStreaming started]");
	}

	/**
	 * TrpFrog用の自作リスナーをセットします。
	 * @param l
	 */
	@Deprecated
	public void addListener(MentionListener l){
		listenerList.add(l);
	}

	/**
	 * 登録されているリスナーのリストを返します。
	 * @return リスナーリスト
	 */
	@Deprecated
	public List<MentionListener> getListenerList(){
		return listenerList;
	}
}