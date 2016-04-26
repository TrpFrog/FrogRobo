package net.trpfrog.frogrobo.streaming;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public abstract class StreamingSetter{

	protected boolean isDebug;
	protected String consumerKey;
	protected String consumerSecret;
	protected String accessToken;
	protected String accessTokenSecret;

	protected boolean isUserStream = false;
	protected boolean registeredShutdownHook = false;

	protected TwitterStream stream = new TwitterStreamFactory(getBuilder().build()).getInstance();

	protected final void setKey
	(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret, boolean isDebug){
		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
		this.accessToken = accessToken;
		this.accessTokenSecret = accessTokenSecret;
		this.isDebug = isDebug;
	}

	protected final void setKey(Stream<String> keyStream, boolean isDebug){
		List<String> apiKeys = keyStream.collect(Collectors.toList());
		this.setKey(apiKeys.get(0), apiKeys.get(1), apiKeys.get(2), apiKeys.get(3), isDebug);
	}

	protected final void setKey
	(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret){
		this.setKey(consumerKey, consumerSecret, accessToken, accessTokenSecret, true);
	}

	public final Twitter getTwitter(){
		 Twitter twitter = new TwitterFactory(getBuilder().build()).getInstance();
		 return twitter;
	}

	protected final ConfigurationBuilder getBuilder(){
		ConfigurationBuilder builder = new ConfigurationBuilder()
		.setDebugEnabled(this.isDebug)
		.setOAuthConsumerKey(this.consumerKey)
		.setOAuthConsumerSecret(this.consumerSecret)
		.setOAuthAccessToken(this.accessToken)
		.setOAuthAccessTokenSecret(this.accessTokenSecret);
		return builder;
	}

	@Deprecated
	public final TwitterStream getStream(){
		if(this.stream==null){
			this.stream = new TwitterStreamFactory(getBuilder().build()).getInstance();
		}
		return this.stream;
	}

	public void end(){
		this.stream.shutdown();
	}

	protected abstract void doing();

	public final void start(){
		doing();
		if(this.registeredShutdownHook==false){
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					try{
						getTwitter().updateStatus("ロボオフ\n["+new Date()+"]");
					}catch(TwitterException e){
						e.printStackTrace();
					}
					stream.shutdown();
				}
			});
			this.registeredShutdownHook = true;
		}
	}



}
