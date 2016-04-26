package net.trpfrog.frogrobo.text_analysis;

import java.util.ArrayList;
import java.util.Iterator;

import net.trpfrog.frogrobo.mini_tools.ToolsLoader;
import net.trpfrog.frogrobo.streaming.MentionListenerPlus;
import twitter4j.Status;

public class ToHiraganaListener extends MentionListenerPlus{

	@Override
	public String getCommandName() {
		return "toHiragana";
	}

	@Override
	public String getShortCommand() { return "hrgn"; }

	@Override
	public String getCommandUsage() {
		return this.getCommandLowerCase()+" [ひらがなにしたい文]";
	}

	@Override
	public String getCommandDescription() {
		return "文をひらがなにします";
	}

	@Override
	public String[] getCommandExceptionDescription() {
		String[] str = {"文字数が多すぎるとき"};
		return str;
	}

	@Override
	public void doWhenReceiveCommand(Status status, String[] commands) {
		String text = status.getText().replaceFirst(commands[0]+"( |\n)"+commands[1]+"( |\n)", "");

		ArrayList<String[]> words;
		try {
			words = JapaneseAnalysis.morphological(text);
		} catch (APINetworkErrorException e) {
			ToolsLoader.reply(e.getMessage(), status, true);
			return;
		}

		Iterator<String[]> ite = words.iterator();

		StringBuilder sb = new StringBuilder();
		sb.append("【変換結果】\n");
		for(String[] value : words){
			ite.next();
			sb.append(value[JapaneseAnalysis.READING]);
			if(ite.hasNext()){
				sb.append(" ");
			}
		}

		try{
			ToolsLoader.reply(sb.toString(), status, false);
		}catch(IllegalArgumentException ex){
			ToolsLoader.reply("文が長過ぎます", status, true);
		}
	}

	@Override
	public void mention(Status status) {
	}
}

