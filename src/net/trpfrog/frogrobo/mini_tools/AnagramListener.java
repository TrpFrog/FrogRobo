package net.trpfrog.frogrobo.mini_tools;

import java.text.BreakIterator;
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
		sb.append("【文字をシャッフルしました】");
		sb.append(System.lineSeparator());

		for(int i=2; i < commands.length ;i++){

			Random rand = new Random(System.currentTimeMillis());
			ArrayList<String> charList = new ArrayList<>();

			BreakIterator bi = BreakIterator.getCharacterInstance();
			bi.setText(commands[i]);

			int start = bi.first(), end = bi.next();
			while(end != BreakIterator.DONE){
				charList.add(commands[i].substring(start,end));
				start = end;
				end = bi.next();
			}

			Collections.shuffle(charList,rand);

			charList.stream().forEach(e -> {
				sb.append(e);
			});
			sb.append(System.lineSeparator());
		}

		MentionListenerPlus.reply(sb.toString(), status, false);
	}

	@Override
	public void whenMentioned(Status status) {
	}

}
