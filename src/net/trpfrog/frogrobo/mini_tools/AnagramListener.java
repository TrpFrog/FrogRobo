package net.trpfrog.frogrobo.mini_tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import org.apache.commons.lang3.text.StrBuilder;

import net.trpfrog.frogrobo.streaming.MentionListenerPlus;
import twitter4j.Status;

public class AnagramListener extends MentionListenerPlus {

	@Override
	public String getCommandName() { return "Anagram"; }

	@Override
	public String getCommandUsage() { return this.getCommandLowerCase(); }
	@Override
	public String getCommandDescription() {return "アナグラムします(適当)";}
	@Override
	public String[] getCommandExceptionDescription() { return null; }

	@Override
	public void doWhenReceiveCommand(Status status, String[] commands) {
		if(commands.length == 2)
			return;

		StringBuilder sb = new StringBuilder();
		sb.append(System.lineSeparator());
		sb.append("【アナグラムしました(適当)】");
		sb.append(System.lineSeparator());
		for(int i=2; i < commands.length ;i++){
			Random rand = new Random(System.currentTimeMillis());
			ArrayList<Character> charList = new ArrayList<>();
			for (char c : commands[i].toCharArray()) {
				charList.add(c);
			}
			Collections.shuffle(charList,rand);
			charList.stream().forEach(e -> {
				sb.append(e);
			});
			sb.append(System.lineSeparator());
		}

		ToolsLoader.reply(sb.toString(), status, false);
	}

	@Override
	public void whenMentioned(Status status) {
	}

}
