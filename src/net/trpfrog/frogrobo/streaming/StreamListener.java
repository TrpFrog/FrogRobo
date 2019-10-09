package net.trpfrog.frogrobo.streaming;

import twitter4j.Status;

public interface StreamListener {
	/**
	 * ストリームを流れる全てのツイートに反応する箇所
	 * @param status
	 */
	public abstract void allTweet(Status status);

	/**
	 * ストリームを流れるうちメンションでない全てのツイートに反応する箇所
	 * @param status
	 */
	public abstract void normalTweet(Status status);
}
