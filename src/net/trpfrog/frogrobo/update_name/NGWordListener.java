package net.trpfrog.frogrobo.update_name;

import net.trpfrog.frogrobo.FrogRobo;
import net.trpfrog.frogrobo.Tools;
import net.trpfrog.frogrobo.streaming.MentionListenerPlus;
import net.trpfrog.frogrobo.streaming.TweetStream;
import twitter4j.Status;
import twitter4j.TwitterException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NGWordListener extends MentionListenerPlus {

	private static List<String> NGnameList = new ArrayList<>();

	static {
		try {
			Tools.getTextLineStream(FrogRobo.FILE_PATH,"Files","NGnameList.txt")
					.forEach( e -> NGnameList.add(e) );
		} catch (IOException e) {
			System.out.println("NGリストのロードに失敗しました。");
			e.printStackTrace();
		}
		Runtime.getRuntime().addShutdownHook(new Thread(NGWordListener::writeNGList));
	}

	public static List<String> getNGnameList() {
		return NGnameList;
	}

	public static void removeNG(String word){
		if (NGnameList.contains(word)) {
			NGnameList.remove(word);
		}
	}

	public static boolean contains(String word){
		return NGnameList.contains(word);
	}

	public static boolean isNG(String name){
		for(String ngword : NGnameList){
			if(name.toLowerCase().matches("(.|\\n)*"+ngword+"(.|\\n)*")){
				return true;
			}
		}
		return false;
	}


	public static void writeNGList(){
		String path = Tools.generatePath (FrogRobo.FILE_PATH,"Files","NGnameList.txt");
		String[] text = new String[NGnameList.size()];
		for(int i = 0; i< NGnameList.size(); i++){
			text[i] = NGnameList.get(i);
		}
		try {
			Tools.writeAllNewText(path,text);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getCommandName() {
		return "ng_word";
	}

	@Override
	public String getCommandUsage() {
		return "ng_word [NGワード]";
	}

	@Override
	public String getCommandDescription() {
		return "[管理者用] 指定した文字列を含む改名を禁止します。";
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
		String word = commands[2];
		if (isNG(word)) {
			MentionListenerPlus.reply("既に禁止されている語句です。",status,true);
			return;
		}
		NGnameList.add(word);
		MentionListenerPlus.reply(word+"を含む名前への改名を禁止しました。",status,true);
	}

	@Override
	public void whenMentioned(Status status) {
		// TODO 自動生成されたメソッド・スタブ

	}

}
