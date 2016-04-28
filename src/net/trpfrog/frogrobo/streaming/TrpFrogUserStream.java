package net.trpfrog.frogrobo.streaming;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.SystemUtils;

import net.trpfrog.frogrobo.main.FrogRobo;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterStreamFactory;

public class TrpFrogUserStream extends StreamingSetter{

	private static TrpFrogUserStream trpfrog = new TrpFrogUserStream();

	private List<MentionListener> listenerList = new ArrayList<>();

	private TrpFrogUserStream(){
		final String FS = SystemUtils.FILE_SEPARATOR;
		StringBuilder path = new StringBuilder();
		path.append(FrogRobo.FILE_PASS);
		path.append("SecretFiles");
		path.append(FS);
		path.append("TrpFrogAPIKeys.txt");
		try (Stream<String> keyStream = Files.lines(Paths.get(path.toString()));){
			this.setKey(keyStream, false);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.stream = new TwitterStreamFactory(getBuilder().build()).getInstance();
	}

	public static TrpFrogUserStream getInstance(){
		if(trpfrog==null){
			trpfrog = new TrpFrogUserStream();
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
					final boolean REPLY = status.getText().toLowerCase().matches("^"+lowerScreenName+".*");
					final boolean MENTION = status.getText().toLowerCase().matches(".*"+lowerScreenName+".*")||REPLY;

					if (MENTION) {
						for(MentionListener listener : listenerList){
							System.out.println("[in to "+listener.getCommandName()+"]");
							if(REPLY){
								String[] commands = status.getText().split("( |\n)");
								listener.reply(status, commands);
							}
							listener.mention(status);
						}
					}
				}
			});
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		stream.user();
		System.out.println("[TrpFrog - UserStreaming started]");
	}

	public void addListener(MentionListener l){
		listenerList.add(l);
	}

	public List<MentionListener> getListenerList(){
		return listenerList;
	}
}