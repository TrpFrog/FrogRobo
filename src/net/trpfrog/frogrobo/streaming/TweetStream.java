package net.trpfrog.frogrobo.streaming;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.SystemUtils;

import net.trpfrog.frogrobo.FrogRobo;
import net.trpfrog.frogrobo.autoreply.AutoReply;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterStreamFactory;

public class TweetStream extends StreamingSetter {

	public static final long FROGROBO_USER_ID = 2744579940L;

	private static TweetStream frogrobo = new TweetStream();

	private List<MentionListener> mentionListenerList = new ArrayList<>();

	private List<StreamListener> streamListenerList = new ArrayList<>();

	private TweetStream(){

		final String FS = SystemUtils.FILE_SEPARATOR;
		StringBuilder path = new StringBuilder();
		path.append(FrogRobo.FILE_PASS);
		path.append("SecretFiles");
		path.append(FS);
		path.append("FrogRoboAPIKeys.txt");
		try (Stream<String> keyStream = Files.lines(Paths.get(path.toString()));){
			this.setKey(keyStream, false);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.stream = new TwitterStreamFactory(getBuilder().build()).getInstance();
	}

	public static TweetStream getInstance(){
		if(frogrobo==null){
			frogrobo = new TweetStream();
		}
		return frogrobo;
	}

	protected void doing(){
		try {
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

				String lowerScreenName = "@"+frogrobo.getTwitter().getScreenName().toLowerCase();
				@Override
				public void onStatus(Status status) {


					streamListenerList.forEach(listener->listener.allTweet(status));

					System.out.println("["+status.getUser().getName()+"("+status.getUser().getScreenName()+")]");
					System.out.println(status.getText());

					final boolean REPLY = status.getText().toLowerCase().matches("^"+lowerScreenName+"(.|\\\\n)*");
					// @frogrobo (ケース問わず）で始まるツイートであればリプライと判断

					final boolean MENTION = status.getText().toLowerCase().matches("(.|\\\\n)*"+lowerScreenName+"(.|\\\\n)*")||REPLY;
					// リプライであるか、@frogroboが含まれているツイートはメンションと判断

					System.err.println("  isReply:"+REPLY);
					System.err.println("isMention:"+MENTION);

					if (MENTION) {
						boolean isCommandTweet = false;
						String[] commands = status.getText().split("( |\\n)"); //スペースか改行で区切る
						
						for(MentionListener listener : mentionListenerList){
							System.err.println("[in to "+listener.getCommandName()+"]");
							if(REPLY){
								listener.reply(status, commands);
								if(listener.isThisCommand(commands)){
									isCommandTweet = true;
									break;
								}
							}
							listener.mention(status);
						}
						if(isCommandTweet == false && REPLY){
							new AutoReply().doWhenReceiveCommand(status,commands);
						}
					} else {
						streamListenerList.forEach(listener->listener.normalTweet(status));
					}
					System.err.println("[exit from onStatus]");
				}
			});
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		super.stream.user();
		try{
			super.getTwitter().updateStatus("ロボを起動しました\n["+new Date()+"]");
		}catch(TwitterException e){
			e.printStackTrace();
		}
		System.out.println("[FrogRobo - UserStreaming started]");
		System.out.println("[ListenerCount:"+mentionListenerList.size()+"]");
		for(int i=0;i<mentionListenerList.size();i++){
			System.out.println("     ・"+mentionListenerList.get(i).getClass().getSimpleName());
		}
	}

	public synchronized void addMentionListener(MentionListener l){
		this.mentionListenerList.add(l);
	}

	public synchronized void removeMentionListener(MentionListener l){
		this.mentionListenerList.remove(l);
	}

	public List<MentionListener> getMentionListenerList(){
		return this.mentionListenerList;
	}

	public synchronized void addStreamListener(StreamListener l){
		this.streamListenerList.add(l);
	}

	public synchronized void removeStreamListener(StreamListener l){
		this.streamListenerList.remove(l);
	}

	public List<StreamListener> getStreamListenerList(){
		return this.streamListenerList;
	}



}
