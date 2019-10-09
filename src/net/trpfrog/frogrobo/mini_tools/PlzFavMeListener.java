package net.trpfrog.frogrobo.mini_tools;

import net.trpfrog.frogrobo.streaming.MentionListenerPlus;
import net.trpfrog.frogrobo.streaming.TweetStream;
import twitter4j.Status;
import twitter4j.TwitterException;

public class PlzFavMeListener extends MentionListenerPlus{

	@Override
	public String getCommandName() { return "plzFav"; }

	@Override
	public String getCommandUsage() { return this.getCommandLowerCase()+" [Opt:TweetId]"; }
	@Override
	public String getCommandDescription() {return "ふぁぼります";}
	@Override
	public String[] getCommandExceptionDescription() { return null; }

	@Override
	public void doWhenReceiveCommand(Status status, String[] commands) {
		if(commands.length == 3){
			if(commands[2].matches("^[0-9]+$")){ //整数
				long tweetId = Long.parseLong(commands[2]);
				try {
					TweetStream.getInstance().getTwitter().createFavorite(tweetId);
				} catch (TwitterException e) {
					ToolsLoader.reply("ふぁぼれませんでした", status, true);
					return;
				}
				ToolsLoader.reply("ID:"+tweetId+"のツイートをふぁぼりました", status, true);
			}
		}
		try {
			TweetStream.getInstance().getTwitter().createFavorite(status.getId());
		} catch (TwitterException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void whenMentioned(Status status) {
	}

}
