package net.trpfrog.frogrobo.mini_tools;

import java.util.Date;

import net.trpfrog.frogrobo.streaming.MentionListenerPlus;
import net.trpfrog.frogrobo.streaming.TweetStream;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.TwitterException;

public class ToBinaryListener extends MentionListenerPlus{

	@Override
	public String getCommandName() { return "to_Base"; }
	@Override
	public String getCommandUsage() { return this.getCommandName()+" [変換対象数列] [対象はn進数]"; }
	@Override
	public String getCommandDescription() { return "指定した数列を2,8,10,16進数に変換します"; }
	@Override
	public String[] getCommandExceptionDescription() {
		String[] str =  {"変換対象数列が指定した進法のルールにのっとっていない場合",
						"変換対象数列に文字列が入っている場合"};
		return str;
	}

	private static final byte NUMBER_AREA = 2;
	private static final byte BASE = 3;

	@Override
	public void doWhenReceiveCommand(Status status, String[] commands) {
		final boolean CMD2_IS_NUMBER = commands[NUMBER_AREA].matches("[0-9]*");
		final boolean IS_BINARY =
				commands[BASE].toLowerCase().equals("bin")||
				commands[BASE].toLowerCase().equals("binary")||
				commands[BASE].toLowerCase().equals("b")||
				commands[BASE].toLowerCase().equals("2");
		final boolean IS_OCTAL =
				!IS_BINARY &&
				commands[BASE].toLowerCase().equals("oct")||
				commands[BASE].toLowerCase().equals("octal")||
				commands[BASE].toLowerCase().equals("o")||
				commands[BASE].toLowerCase().equals("8");
		final boolean IS_HEX =
				!IS_BINARY && !IS_OCTAL &&
				commands[BASE].toLowerCase().equals("hex")||
				commands[BASE].toLowerCase().equals("h")||
				commands[BASE].toLowerCase().equals("16");
		final boolean IS_DECIMAL =
				!IS_BINARY && !IS_OCTAL && !IS_HEX;

		if(IS_BINARY && commands[NUMBER_AREA].toLowerCase().matches("[0-1]*")==false){
			errorMessage(status, "2進数では0~1以外の文字,数字は使用できません");
			return;
		}

		if(IS_OCTAL && commands[NUMBER_AREA].toLowerCase().matches("[0-7]*")==false){
			errorMessage(status, "8進数では0~7以外の文字,数字は使用できません");
			return;
		}

		if(IS_HEX && commands[NUMBER_AREA].toLowerCase().matches("[0-9,a-f]*")==false){
			errorMessage(status, "16進数では0~9,a~f以外の文字は使用できません");
			return;
		}

		if(IS_DECIMAL && commands[NUMBER_AREA].toLowerCase().matches("[0-9]*")==false){
			errorMessage(status, "10進数では0~9以外の文字は使用できません");
			return;
		}

		int decimal = 0;
		byte base = 0;
		if(IS_BINARY) base = 2;
		else if(IS_OCTAL) base = 8;
		else if(IS_HEX) base = 16;
		else if(IS_DECIMAL) base = 10;

		if(CMD2_IS_NUMBER||IS_HEX){
			decimal = Integer.parseInt(commands[NUMBER_AREA], base);
			String binary = Integer.toBinaryString(decimal);
			String octal  = Integer.toOctalString(decimal);
			String hex    = Integer.toHexString(decimal);

			final String BR = "\n";

			StringBuilder sb = new StringBuilder();
			sb.append("@");
			sb.append(status.getUser().getScreenName());
			sb.append(BR);

			sb.append("【\"");
			sb.append(commands[NUMBER_AREA]);
			sb.append("\"(");
			sb.append(base);
			sb.append("進数)の変換結果】");
			sb.append(BR);

			sb.append("10進数: ");
			sb.append(decimal);
			sb.append(BR);

			sb.append("16進数: ");
			sb.append(hex);
			sb.append(BR);

			sb.append("08進数: ");
			sb.append(octal);
			sb.append(BR);

			sb.append("02進数: ");
			sb.append(binary);

			StatusUpdate reply = new StatusUpdate(sb.toString()).inReplyToStatusId(status.getId());
			try {
				TweetStream.getInstance().getTwitter().updateStatus(reply);
			} catch (TwitterException e) {
				e.printStackTrace();
			}

		}
	}

	private void errorMessage(Status status,String text) {
		StringBuilder sb = new StringBuilder();
		sb.append("@");
		sb.append(status.getUser().getScreenName());
		sb.append("\n");
		sb.append(text);
		sb.append("\n");
		sb.append("["+new Date()+"]");

		StatusUpdate reply = new StatusUpdate(sb.toString()).inReplyToStatusId(status.getId());
		try {
			TweetStream.getInstance().getTwitter().updateStatus(reply);
		} catch (TwitterException e) {
			e.printStackTrace();
		}

		status.getGeoLocation();
	}

	@Override
	public void whenMentioned(Status status) {

	}

}
