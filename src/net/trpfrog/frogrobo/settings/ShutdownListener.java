package net.trpfrog.frogrobo.settings;

import net.trpfrog.frogrobo.FrogRobo;
import net.trpfrog.frogrobo.streaming.MentionListener;
import net.trpfrog.frogrobo.streaming.MentionListenerPlus;
import net.trpfrog.frogrobo.streaming.TrpFrogUserStream;
import twitter4j.Status;
import twitter4j.TwitterException;

public class ShutdownListener extends MentionListenerPlus {

	@Override
	public String getCommandName() {
		return "shutdown";
	}

	@Override
	public String getCommandUsage() {
		return "shutdown [自動ツイ消しの有無]";
	}

	@Override
	public String getCommandDescription() {
		return "ロボをシャットダウンします。";
	}

	@Override
	public String[] getCommandExceptionDescription() {
		return new String[1];
	}

	@Override
	public void doWhenReceiveCommand(Status status, String[] commands) {
		if(status.getUser().getId() != FrogRobo.TRPFROG_USER_ID){
			MentionListenerPlus.reply("アクセス権がありません。このコマンドは管理者のみが使用できます。",status,true);
			return;
		}
		if(commands[2].equals("true")){
			try {
				TrpFrogUserStream.getInstance().getTwitter().destroyStatus(status.getId());
			} catch (TwitterException e) {
				MentionListenerPlus.reply("ツイ消しできませんでした。",status,true);
			}
		}else{
			MentionListenerPlus.reply("シャットダウンします。",status,true);
		}
		System.exit(0);
	}

	@Override
	public void whenMentioned(Status status) {
		// TODO 自動生成されたメソッド・スタブ

	}

}
