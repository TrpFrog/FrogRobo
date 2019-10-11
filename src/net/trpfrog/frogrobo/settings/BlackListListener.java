package net.trpfrog.frogrobo.settings;

import net.trpfrog.frogrobo.FrogRobo;
import net.trpfrog.frogrobo.Tools;
import net.trpfrog.frogrobo.streaming.MentionListener;
import net.trpfrog.frogrobo.streaming.MentionListenerPlus;
import net.trpfrog.frogrobo.streaming.TweetStream;
import twitter4j.Status;
import twitter4j.TwitterException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BlackListListener extends MentionListenerPlus {

	private static List<Long> bannedList = new ArrayList<>();

	static {
		try {
			Tools.getTextLineStream(FrogRobo.FILE_PATH,"Files","BannedList.txt")
					.forEach( e -> bannedList.add(Long.parseLong(e)) );
		} catch (IOException e) {
			System.out.println("BANリストのロードに失敗しました。");
			e.printStackTrace();
		}
		Runtime.getRuntime().addShutdownHook(new Thread(BlackListListener::writeBannedList));
	}

	public static List<Long> getBannedList() {
		return bannedList;
	}

	public static void removeBan(long userId){
		if (isBanned(userId)) {
			bannedList.remove(userId);
		}
	}

	public static void ban(long userId){
		if (!isBanned(userId)) {
			bannedList.add(userId);
		}
	}

	public static boolean isBanned(long userId){
		return bannedList.contains(userId);
	}

	public static boolean isBanned(Status status){
		return isBanned(status.getUser().getId());
	}

	public static void writeBannedList(){
		String path = Tools.generatePath (FrogRobo.FILE_PATH,"Files","BannedList.txt");
		String[] text = new String[bannedList.size()];
		for(int i=0;i<bannedList.size();i++){
			text[i] = bannedList.get(i).toString();
		}
		try {
			Tools.writeAllNewText(path,text);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getCommandName() {
		return "ban";
	}

	@Override
	public String getCommandUsage() {
		return "ban [UserID]";
	}

	@Override
	public String getCommandDescription() {
		return "[管理者用] 指定したユーザをBANします。";
	}

	@Override
	public String[] getCommandExceptionDescription() {
		return null;
	}


	@Override
	public void doWhenReceiveCommand(Status status, String[] commands) {
		if(status.getUser().getId() != FrogRobo.TRPFROG_USER_ID){
			MentionListenerPlus.reply("権限がありません！このコマンドは管理者のみが使用できます。",status,true);
			return;
		}
		if(commands.length<=2){
			MentionListenerPlus.reply("Usage:"+getCommandUsage(),status,true);
			return;
		}
		long userId;
		try {
			userId = TweetStream.getInstance().getTwitter().showUser(commands[2]).getId();
		} catch (TwitterException e) {
			e.printStackTrace();
			MentionListenerPlus.reply("ユーザが見つかりませんでした。",status,true);
			return;
		}
		if (isBanned(userId)) {
			MentionListenerPlus.reply("既にBANされているユーザです。",status,true);
			return;
		}
		ban(userId);
		MentionListenerPlus.reply(commands[2]+"をBANしました。",status,true);
	}

	@Override
	public void whenMentioned(Status status) {
		// TODO 自動生成されたメソッド・スタブ

	}

}
