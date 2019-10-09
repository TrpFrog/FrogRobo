package net.trpfrog.frogrobo.text_analysis;

import java.util.ArrayList;

import net.trpfrog.frogrobo.mini_tools.ToolsLoader;
import net.trpfrog.frogrobo.streaming.MentionListenerPlus;
import twitter4j.Status;

public class AnalysisListener extends MentionListenerPlus{
	@Override
	public String getCommandName() {
		return "analyze_text";
	}

	@Override
	public String getCommandUsage() {
		return getCommandName()+" [品詞分解したい文]";
	}

	@Override
	public String getCommandDescription() {
		return "品詞分解します";
	}

	@Override
	public String[] getCommandExceptionDescription() {
		String[] str = {"文字数が多すぎるとき"};
		return str;
	}

	@Override
	public void doWhenReceiveCommand(Status status, String[] commands) {

		String text = status.getText().replaceFirst(commands[0]+"( |\n)"+commands[1]+"( |\n)", "");
		ArrayList<String[]> words = null;

		try {
			words = JapaneseAnalysis.morphological(text);
		} catch (APINetworkErrorException e) {
			ToolsLoader.reply(e.getMessage(), status, true);
			return;
		}

		StringBuilder sb = new StringBuilder();
		sb.append("\n【解析結果】\n");
		for(String[] value : words){
			sb.append(value[JapaneseAnalysis.POS]);
			sb.append(": ");
			sb.append(value[JapaneseAnalysis.SURFACE]);
			sb.append("\n");
		}
		try{
			ToolsLoader.reply(sb.toString(), status, false);
		}catch(IllegalArgumentException ex){
			ToolsLoader.reply("文が長過ぎます", status, true);
		}
	}

	@Override
	public void whenMentioned(Status status) {

	}

}
