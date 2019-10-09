package net.trpfrog.frogrobo.reboot;

import net.trpfrog.frogrobo.streaming.MentionListenerPlus;
import twitter4j.Status;

public class RebootListener extends MentionListenerPlus {

	@Override
	public String getCommandName() {
		return null;
	}

	@Override
	public String getCommandUsage() {
		return null;
	}

	@Override
	public String getCommandDescription() {
		return null;
	}

	@Override
	public String[] getCommandExceptionDescription() {
		return null;
	}

	@Override
	public void doWhenReceiveCommand(Status status, String[] commands) {
	}

	@Override
	public void whenMentioned(Status status) {
	}

}
