package net.trpfrog.frogrobo.update_name;

import net.trpfrog.frogrobo.FrogRobo;
import net.trpfrog.frogrobo.streaming.MentionListenerPlus;
import net.trpfrog.frogrobo.streaming.TweetStream;
import twitter4j.Status;
import twitter4j.TwitterException;

import java.util.ArrayList;
import java.util.List;

import static net.trpfrog.frogrobo.settings.BlackListListener.isBanned;
import static net.trpfrog.frogrobo.settings.BlackListListener.removeBan;

public class RemoveNGListener extends MentionListenerPlus {

	private static List<Long> bannedList = new ArrayList<>();

	public static List<Long> getBannedList() {
		return bannedList;
	}

	@Override
	public String getCommandName() {
		return "remove_ng";
	}

	@Override
	public String getCommandUsage() {
		return "remove_ng [UserID]";
	}

	@Override
	public String getCommandDescription() {
		return "[管理者用] 指定した文字列を含む改名を許可します。";
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
		String word = commands[2];
		if (NGWordListener.contains(word)) {
			NGWordListener.removeNG(word);
			MentionListenerPlus.reply(word+"を含む名前への改名を許可しました。",status,true);
		}else{
			MentionListenerPlus.reply("既に許可されている語句です。",status,true);
		}
	}

	@Override
	public void whenMentioned(Status status) {
		// TODO 自動生成されたメソッド・スタブ

	}

}
