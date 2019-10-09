package net.trpfrog.frogrobo.mini_tools;

import java.util.Date;

import org.apache.commons.lang3.text.StrBuilder;

import net.trpfrog.frogrobo.streaming.TweetStream;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.TwitterException;

public final class ToolsLoader {

	/**
	 * 面倒なリプライ処理を行うメソッド
	 *
	 * @param tweet リプライで飛ばす文面
	 * @param inReplyTo リプライを飛ばす先のツイート
	 * @param dateTweet 日付付きツイートにするかどうか
	 * @throws IllegalArgumentException ツイートが140文字を超える場合
	 * @Deprecated このクラスは廃止予定です。代わりにMentionListenerPlus.reply()を使ってください。
	 */
	@Deprecated
	public static void reply(String tweet, Status inReplyTo, boolean dateTweet) {
		final String SCREEN_NAME = "@" + inReplyTo.getUser().getScreenName() + " ";
		String date = "";

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
