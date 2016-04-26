package net.trpfrog.frogrobo.help;

import java.util.List;

import net.trpfrog.frogrobo.mini_tools.ToolsLoader;
import net.trpfrog.frogrobo.streaming.MentionListener;
import net.trpfrog.frogrobo.streaming.MentionListenerPlus;
import net.trpfrog.frogrobo.streaming.TweetStream;
import twitter4j.Status;

public class HelpListener extends MentionListenerPlus {


	@Override
	public String getCommandName() {
		return "Help";
	}
	@Override
	public String getShortCommand() { return ""; }

	@Override
	public String getCommandUsage() {
		return getCommandName()+" [コマンド]";
	}

	@Override
	public String getCommandDescription(){
		return "コマンドの説明を返します";
	}

	@Override
	public String[] getCommandExceptionDescription() {
		return null;
	}

	@Override
	public void doWhenReceiveCommand(Status status, String[] commands) {
		List<MentionListener> listenerList = TweetStream.getInstance().getMentionListenerList();
		for(MentionListener listener : listenerList){
			if(commands[2].toLowerCase().equals(listener.getCommandName())==false){
				continue;
			}
			StringBuilder sb = new StringBuilder();
			sb.append("\n【"+listener.getCommandName()+"コマンド】");
			sb.append(listener.getCommandDescription());
			sb.append("\n\n[使い方]");
			sb.append(listener.getCommandUsage());
			sb.append("\n\n[スローする例外]");
			for(String msg : listener.getCommandExceptionDescription()){
				sb.append(msg);
				sb.append("\n");
			}
			ToolsLoader.reply(sb.toString(), status, true);
			break;
		}
	}

	@Override
	public void mention(Status status) {

	}

}
