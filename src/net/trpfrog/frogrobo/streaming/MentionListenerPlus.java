package net.trpfrog.frogrobo.streaming;

import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.TwitterException;

import java.util.Date;

/**
 * コマンドで反応する機能を持たせたいメンションリスナーに使います。
 */
public abstract class MentionListenerPlus implements MentionListener {

	private boolean disable = false;


	private int lengthOfCmds = 0;

	protected int getCmdsLength(){
		return lengthOfCmds-1;
	}

	@Override
	public String toString() {
		return this.getCommandName();
	};

	@Override
	public abstract String getCommandName();

	@Override
	public abstract String getCommandUsage();

	@Override
	public abstract String getCommandDescription();

	@Override
	public abstract String[] getCommandExceptionDescription();

	@Override
	public final void whenReplied(Status status, String[] commands) {
		if(this.disable){
			return;
		}
		this.lengthOfCmds = commands.length;
		doWhenReceiveCommand(status, commands);
	}

	public abstract void doWhenReceiveCommand(Status status, String[] commands);


	public abstract void whenMentioned(Status status);

	/**
	 * コマンドを無効化するかどうかを設定します
	 * @param b
	 */
	@Override
	public void setDisable(boolean b) {
		this.disable = b;
	}

	/**
	 * コマンドが無効化されいているかを調べます
	 * @return
	 */
	@Override
	public boolean isDisable() {
		return this.disable;
	}

	public static void reply(String tweet, Status inReplyTo, boolean dateTweet) {
		final String SCREEN_NAME = "@" + inReplyTo.getUser().getScreenName() + " ";
		String date = "";

		if(TweetStream.FROGROBO_USER_ID==inReplyTo.getInReplyToUserId()) return;

		if (dateTweet == true) {
			date = "\n[" + new Date() + "]";
		}
		String replyMsg = SCREEN_NAME + tweet + date;

		final int MAX_LENGTH = 140 - (SCREEN_NAME + date).length();

		if (tweet.length() >= MAX_LENGTH) {
			StringBuilder sb = new StringBuilder();
			sb.append("メッセージが長過ぎます。");
			sb.append(System.lineSeparator());
			sb.append(MAX_LENGTH);
			sb.append("文字以内にしてください");
			sb.append(System.lineSeparator());
			sb.append("(「").append(tweet).append("」は");
			sb.append(tweet.length());
			sb.append("文字で");
			sb.append(tweet.length()-MAX_LENGTH);
			sb.append("文字オーバーしています)");
			throw new IllegalArgumentException(sb.toString());
		}

		StatusUpdate reply = new StatusUpdate(replyMsg)
				.inReplyToStatusId(inReplyTo.getId());
		try {
			TweetStream.getInstance().getTwitter().updateStatus(reply);
		} catch (TwitterException e) {
			throw new IllegalArgumentException();
		}
	}

}
