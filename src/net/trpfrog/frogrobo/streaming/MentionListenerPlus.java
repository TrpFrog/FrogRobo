package net.trpfrog.frogrobo.streaming;

import twitter4j.Status;

public abstract class MentionListenerPlus implements MentionListener {

	private boolean disable = false;

	private int lengthOfCmds = 0;

	protected int getCmdsLength(){
		return lengthOfCmds-1;
	}

	@Override
	public String toString() {
		return this.getCommandName();
	};

	@Override
	public abstract String getCommandName();

	@Override
	public abstract String getCommandUsage();

	@Override
	public abstract String getCommandDescription();

	@Override
	public abstract String[] getCommandExceptionDescription();

	@Override
	public abstract String getShortCommand();

	@Override
	public final void reply(Status status, String[] commands) {
		if(this.disable){
			return;
		}
		String cmdName = commands[1].toLowerCase();
		if((cmdName.equals(this.getCommandLowerCase())||
				(cmdName.equals( this.getShortCommand() ) ))==false){
			return;
		}
		doWhenReceiveCommand(status, commands);
		this.lengthOfCmds = commands.length;
	}

	public abstract void doWhenReceiveCommand(Status status, String[] commands);


	public abstract void mention(Status status);

	@Override
	public void setDisable(boolean b) {
		this.disable = b;
	}

	@Override
	public boolean isDisable() {
		return this.disable;
	}

}
