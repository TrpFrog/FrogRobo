package net.trpfrog.frogrobo.settings;

import net.trpfrog.frogrobo.FrogRobo;
import net.trpfrog.frogrobo.streaming.MentionListener;
import net.trpfrog.frogrobo.streaming.MentionListenerPlus;
import net.trpfrog.frogrobo.streaming.TweetStream;

import twitter4j.Status;
import twitter4j.TwitterException;

import java.util.ArrayList;
import java.util.List;

import static net.trpfrog.frogrobo.settings.BlackListListener.*;
import static net.trpfrog.frogrobo.streaming.MentionListenerPlus.*;

public class UnbanListener extends MentionListenerPlus {

	private static List<Long> bannedList = new ArrayList<>();

	public static List<Long> getBannedList() {
		return bannedList;
	}

	@Override
	public String getCommandName() {
		return "unban";
	}

	@Override
	public String getCommandUsage() {
		return "unban [UserID]";
	}

	@Override
	public String getCommandDescription() {
		return "[管理者用] 指定したユーザのBANを解除します。";
	}

	@Override
	public String[] getCommandExceptionDescription() {
		return null;
	}


	@Override
	public void doWhenReceiveCommand(Status status, String[] commands) {
		if(status.getUser().getId() != FrogRobo.TRPFROG_USER_ID){
			reply("権限がありません！このコマンドは管理者のみが使用できます。",status,true);
			return;
		}
		if(commands.length<=2){
			reply("Usage:"+getCommandUsage(),status,true);
			return;
		}
		long userId;
		try {
			userId = TweetStream.getInstance().getTwitter().showUser(commands[2]).getId();
		} catch (TwitterException e) {
			reply("ユーザが見つかりませんでした。",status,true);
			return;
		}
		if (isBanned(userId)) {
			removeBan(userId);
			reply(commands[2] +"のBANを解除しました。",status,true);
		}else{
			reply("BANされていないユーザです。",status,true);
		}
	}

	@Override
	public void whenMentioned(Status status) {
		// TODO 自動生成されたメソッド・スタブ

	}

}
