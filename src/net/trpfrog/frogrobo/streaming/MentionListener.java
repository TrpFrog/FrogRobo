package net.trpfrog.frogrobo.streaming;

import twitter4j.Status;

public interface MentionListener {

	public abstract String getCommandName();

	@Deprecated
	public default String getCommandLowerCase(){
		return getCommandName().toLowerCase();
	}

	public abstract String getCommandUsage();
	public abstract String getCommandDescription();
	public abstract String[] getCommandExceptionDescription();

	public abstract void whenReplied(Status status, String[] commands);
	public abstract void whenMentioned(Status status);

	public abstract void setDisable(boolean b);
	public abstract boolean isDisable();

}