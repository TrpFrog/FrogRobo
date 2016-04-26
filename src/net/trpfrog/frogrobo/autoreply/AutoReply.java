package net.trpfrog.frogrobo.autoreply;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.commons.lang3.SystemUtils;

import net.trpfrog.frogrobo.FrogRobo;
import net.trpfrog.frogrobo.hit_and_blow.HitAndBlowListener;
import net.trpfrog.frogrobo.mini_tools.ToolsLoader;
import net.trpfrog.frogrobo.streaming.MentionListenerPlus;
import twitter4j.Status;


public class AutoReply extends MentionListenerPlus{

	private static List<String> replyList = new ArrayList<>();

	@Override
	public void mention(Status status) {
	}


	@Override
	public String getCommandName() { return ""; }
	@Override
	public String getShortCommand() { return ""; }
	@Override
	public String getCommandUsage() {return "";}
	@Override
	public String getCommandDescription() {return "";}
	@Override
	public String[] getCommandExceptionDescription() {return new String[0];};


	@Override
	public void doWhenReceiveCommand(Status status, String[] commands) {

		//------[返信文ファイルのロード]------//
		if(replyList.size() == 0){
			final String FS = SystemUtils.FILE_SEPARATOR;
			StringBuilder path = new StringBuilder();
			path.append(FrogRobo.FILE_PASS);
			path.append("Files");
			path.append(FS);
			path.append("AutoReply.txt");
			try {
				replyList = Files.lines(Paths.get(path.toString())).collect(Collectors.toList());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if(commands[1].matches("^[0-9]+$")){
			commands[2] = commands[1];
			commands[1] = "hab";
			new HitAndBlowListener().reply(status, commands);
			return;
		}

		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}
		//------[返信内容の選定(乱数)------//
		Random random = new Random(System.currentTimeMillis());
		boolean success = true;
		do{
			success = true;
			int num = random.nextInt(replyList.size());
			try{
				ToolsLoader.reply(replyList.get(num), status, false);
			}catch (IllegalArgumentException e){
				num = random.nextInt(replyList.size());
				success = false;
			}
		}while(success==false);
	}
}
