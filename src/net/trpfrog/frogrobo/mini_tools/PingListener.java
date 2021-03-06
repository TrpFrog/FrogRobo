package net.trpfrog.frogrobo.mini_tools;

import net.trpfrog.frogrobo.streaming.MentionListenerPlus;
import twitter4j.Status;

public class PingListener extends MentionListenerPlus{

	@Override
	public String getCommandName() { return "ping"; }

	@Override
	public String getCommandUsage() { return this.getCommandName(); }
	@Override
	public String getCommandDescription() {return "起動しているかの確認に使います";}
	@Override
	public String[] getCommandExceptionDescription() { return null; }

	@Override
	public void doWhenReceiveCommand(Status status, String[] commands) {
		System.out.println("test");
		MentionListenerPlus.reply("やっほー起きてるよ", status, true);
	}

	@Override
	public void whenMentioned(Status status) {
	}

}