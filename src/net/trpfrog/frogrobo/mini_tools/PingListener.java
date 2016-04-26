package net.trpfrog.frogrobo.mini_tools;

import net.trpfrog.frogrobo.streaming.MentionListenerPlus;
import twitter4j.Status;

public class PingListener extends MentionListenerPlus{

	@Override
	public String getCommandName() { return "bootCheck"; }
	@Override
	public String getShortCommand() { return "ping"; }
	@Override
	public String getCommandUsage() { return this.getCommandLowerCase(); }
	@Override
	public String getCommandDescription() {return "起動しているかの確認に使います";}
	@Override
	public String[] getCommandExceptionDescription() { return null; }

	@Override
	public void doWhenReceiveCommand(Status status, String[] commands) {
		ToolsLoader.reply("やっほー起きてるよ", status, true);
	}

	@Override
	public void mention(Status status) {
	}

}