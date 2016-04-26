package net.trpfrog.frogrobo.mini_tools;

import java.util.Random;

import net.trpfrog.frogrobo.streaming.MentionListenerPlus;
import twitter4j.Status;

/*
 * ハコ語変換クラス
 */

public class TranslateBoxLangListener extends MentionListenerPlus{
	@Override
	public String getCommandName() { return "toHakoLang"; }
	@Override
	public String getShortCommand() { return "bxlg"; }
	@Override
	public String getCommandUsage() { return getCommandName()+" [翻訳対象文字列]"; }
	@Override
	public String getCommandDescription() { return "文字列をハコ語に変換します"; }
	@Override
	public String[] getCommandExceptionDescription() {
		String[] str =  {"翻訳対象文字列が長すぎて翻訳結果が140文字を超える場合"};
		return str;
	}

	@Override
	public void doWhenReceiveCommand(Status status, String[] commands) {
		System.out.println("HakoLang Tweet");

		String translateString =
				status.getText().replaceFirst(commands[0]+"( |\\n)"+commands[1]+"( |\\n)", "");
		char[] chars = translateString.toCharArray();
		StringBuilder sb = new StringBuilder();

		sb.append("\n【ハコ語への翻訳結果】\n");
		for (int i=0;i<chars.length;i++) {
			Character c = chars[i];
			String ch = c.toString();
			if(ch.matches("\n")){
				sb.append("\n");
				continue;
			}
			if(ch.matches("([!！?？。.,、，。．「」{}\"]|[1-9])")){
				sb.append(ch);
				continue;
			}

			Random random = new Random();
			switch(random.nextInt(2)){
			case 0:
				sb.append('□');
				break;
			case 1:
				sb.append('■');
				break;
			}
		}
		try{
			ToolsLoader.reply(sb.toString(), status, false);
		}catch(IllegalArgumentException e){
			ToolsLoader.reply("文字数を減らしてください", status, true);
		}
	}

	@Override
	public void mention(Status status) {

	}
}
