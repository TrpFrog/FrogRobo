package net.trpfrog.frogrobo.streaming;

import twitter4j.Status;

public interface StreamListener {
	public abstract void allTweet(Status status);
	public abstract void normalTweet(Status status);
}
