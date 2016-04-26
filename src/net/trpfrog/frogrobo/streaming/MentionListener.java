package net.trpfrog.frogrobo.streaming;

import twitter4j.Status;

public interface MentionListener {
	public abstract String getCommandName();
	public abstract String getShortCommand();
	public default String getCommandLowerCase(){

		return getCommandName().toLowerCase();
	}

	public abstract String getCommandUsage();
	public abstract String getCommandDescription();
	public abstract String[] getCommandExceptionDescription();

	public abstract void reply(Status status, String[] commands);
	public abstract void mention(Status status);

	public abstract void setDisable(boolean b);
	public abstract boolean isDisable();

	public default boolean isThisCommand(String[] commands){
		boolean isCmdTweet =
				commands[1].equalsIgnoreCase(getCommandLowerCase()) ||
				commands[1].equalsIgnoreCase(getShortCommand().toLowerCase());
		return isCmdTweet;
	}
}