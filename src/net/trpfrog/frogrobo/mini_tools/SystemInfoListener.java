package net.trpfrog.frogrobo.mini_tools;

import net.trpfrog.frogrobo.streaming.MentionListener;
import net.trpfrog.frogrobo.streaming.MentionListenerPlus;
import twitter4j.Status;

public class SystemInfoListener extends MentionListenerPlus{

	public static void main(String[] args) {
		System.out.println(buildMsg());
	}

	@Override
	public String getCommandName() { return "system_info"; }

	@Override
	public String getCommandUsage() { return this.getCommandName(); }
	@Override
	public String getCommandDescription() {return "実行環境を返します。";}
	@Override
	public String[] getCommandExceptionDescription() { return null; }

	@Override
	public void doWhenReceiveCommand(Status status, String[] commands) {
		MentionListenerPlus.reply(buildMsg(), status, true);
	}

	public static String buildMsg(){
		StringBuilder sb = new StringBuilder();
		sb.append(System.getProperty("os.name"));
		sb.append(" ");
		sb.append(System.getProperty("os.version"));
		sb.append(" ");
		sb.append(System.getProperty("os.arch"));
		sb.append(System.lineSeparator());
		sb.append("Java ");
		sb.append(System.getProperty("java.specification.version"));
		return sb.toString();
	}

	@Override
	public void whenMentioned(Status status) {
	}

}