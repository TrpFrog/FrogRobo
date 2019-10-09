package net.trpfrog.frogrobo.streaming;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.trpfrog.frogrobo.FrogRobo;
import twitter4j.*;
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

	StreamingSetter (String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret){
		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
		this.accessToken = accessToken;
		this.accessTokenSecret = accessTokenSecret;
		this.isDebug = false;
	}

	StreamingSetter (Stream<String> keyStream){
		setKey(keyStream, false);
	}

	StreamingSetter(){
		//nothing to do.
	}

	/**
	 * ストリームにAPIキーを設定します。
	 * @param consumerKey
	 * @param consumerSecret
	 * @param accessToken
	 * @param accessTokenSecret
	 * @param isDebug
	 */
	protected final void setKey(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret, boolean isDebug){
		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
		this.accessToken = accessToken;
		this.accessTokenSecret = accessTokenSecret;
		this.isDebug = isDebug;
	}

	/**
	 * ストリームにコンシューマキー、コンシューマシークレット、アクセストークン、アクセストークンシークレットの順に並んだStringのストリームを用いて設定します。
	 * @param keyStream コンシューマキー、コンシューマシークレット、アクセストークン、アクセストークンシークレットの順に並んだStringのストリーム
	 * @param isDebug
	 */
	protected final void setKey(Stream<String> keyStream, boolean isDebug){
		List<String> apiKeys = keyStream.collect(Collectors.toList());
		this.setKey(apiKeys.get(0), apiKeys.get(1), apiKeys.get(2), apiKeys.get(3), isDebug);
	}

	/**
	 * デバックをtrueの状態でAPIキーを設定します
	 * @param consumerKey
	 * @param consumerSecret
	 * @param accessToken
	 * @param accessTokenSecret
	 */
	protected final void setKey(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret){
		this.setKey(consumerKey, consumerSecret, accessToken, accessTokenSecret, true);
	}

	/**
	 * Twitterインスタンスを返します。
	 * @return Twitterインスタンス
	 */
	public final Twitter getTwitter(){
		 Twitter twitter = new TwitterFactory(getBuilder().build()).getInstance();
		 return twitter;
	}


	/**
	 * 設定したAPIキーからConfigurationBuilderを生成します。
	 * @return ConfigurationBuilder
	 */
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

	public final void applyStream(){
		this.stream = new TwitterStreamFactory(getBuilder().build()).getInstance();
	}



	static Stream<String> keyFileReader(String fileName) throws IOException{
		final String FS = FrogRobo.FS;
		StringBuilder path = new StringBuilder();
		path.append(FrogRobo.FILE_PATH);
		path.append("SecretFiles");
		path.append(FS);
		path.append(fileName);
		Stream<String> keyStream = Files.lines(Paths.get(path.toString()));
		return keyStream;
	}

	/**
	 * ストリームを閉じます。
	 */
	public void close(){
		this.stream.shutdown();
	}

	/**
	 * ストリームの初期設定をします。このクラスのstreamインスタンスにaddListener()で処理を実装してください。
	 */
	protected abstract void doing();


	/**
	 * ストリームを起動します。
	 */
	public final void start(){
		doing();
		if(this.registeredShutdownHook==false){
			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				try{
					getTwitter().updateStatus("ロボオフ\n["+new Date()+"]");
				}catch(TwitterException e){
					e.printStackTrace();
				}
				stream.shutdown();
			}));
			this.registeredShutdownHook = true;
		}
	}

}
