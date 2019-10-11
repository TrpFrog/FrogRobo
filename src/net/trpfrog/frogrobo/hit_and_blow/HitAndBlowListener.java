package net.trpfrog.frogrobo.hit_and_blow;

import java.io.IOException;
import java.util.HashMap;

import net.trpfrog.frogrobo.FrogRobo;
import net.trpfrog.frogrobo.Tools;
import net.trpfrog.frogrobo.mini_tools.ToolsLoader;
import net.trpfrog.frogrobo.streaming.MentionListenerPlus;
import twitter4j.Status;

public class HitAndBlowListener extends MentionListenerPlus {

	@Override
	public String getCommandName() { return "HitAndBlow"; }

	@Override
	public String getCommandUsage() { return this.getCommandLowerCase(); }
	@Override
	public String getCommandDescription() {return "ヒットアンドブローします";}
	@Override
	public String[] getCommandExceptionDescription() { return null; }

	private static HashMap<Long,HitAndBlowPlayer> players = new HashMap<>();
	private static String filePath = Tools.generatePath
			(FrogRobo.FILE_PATH,"Files","HitAndBlowPlayers.txt");

	private static final byte SUB_COMMAND = 2;
	private static final byte LENGTH = 3;

	private static HitAndBlowTools tools = new HitAndBlowTools();

	@Override
	public void doWhenReceiveCommand(Status status, String[] commands) {
		if(commands.length==2)
			return;

		if(players.size()==0){ //初回起動時
			loadPlayers();
			registerShutdownHook();
		}

		long userId = status.getUser().getId();

		//---------------------------------------------------//
		switch(commands[SUB_COMMAND].toLowerCase()){
		case "newgame":
		case "new":
			exitFromTheGame(status.getUser().getId());
			int length = 3;
			if(commands.length>2){
				try {
					length = Integer.parseInt(commands[LENGTH]);
				} catch (NumberFormatException e) {
					//しゃーないね
					length = 3;
				}
			}
			joinTheGame(userId, length);
			MentionListenerPlus.reply("\nゲームを開始します", status, true);
			break;

		//---------------------------------------------------//
		case "giveup":
		case "end":
			exitFromTheGame(status.getUser().getId());
			MentionListenerPlus.reply("\nゲームを終了します", status, true);
			break;

		//---------------------------------------------------//
		default:
			HitAndBlowPlayer player = players.get(userId);
			if(exceptionChecker(player, commands[SUB_COMMAND], status)){
				break;
			}
			recieveNumber(player, commands[SUB_COMMAND], status);
			break;
		}
		//---------------------------------------------------//
	}

	private boolean exceptionChecker(HitAndBlowPlayer player, String numberStr, Status status){
		if(player == null){
			MentionListenerPlus.reply("\nまだゲームが始まっていません", status, true);
			return true;
		}

		if(numberStr.length()!=(player.getSolution()+"").length()){
			MentionListenerPlus.reply("\n桁数が間違っています", status, true);
			return true;
		}

		if(tools.isAnswer(numberStr,player.getSolution())==false){
			MentionListenerPlus.reply("\n数字で答えてください", status, true);
			return true;
		}

		try {
			if(tools.isRepeated(Integer.parseInt(numberStr))){
				MentionListenerPlus.reply("\n数字が繰り返されています", status, true);
				return true;
			}
		} catch (NumberFormatException e1) {
			MentionListenerPlus.reply("\n数字が長過ぎます", status, true);
			return true;
		}
		return false;
	}

	private void recieveNumber(HitAndBlowPlayer player, String numberStr, Status status){

		long userId = status.getUser().getId();

		HitAndBlowResult result = new HitAndBlowResult(114, 514);
		try {
			result =
					tools.checkHitAndBlow(player.getSolution(), Integer.parseInt(numberStr));
		} catch (NumberFormatException e) {
			MentionListenerPlus.reply("\n数字で答えてください", status, true);
		}

		player.addTrials();

		if((player.getSolution()+"").length()==result.getHit()){
			String cleared = HitAndBlowMessageFactory.cleared(player);
			exitFromTheGame(userId);
			MentionListenerPlus.reply(cleared, status, false);
			return;
		}

		if(player.getTrials()==10){
			String failed = HitAndBlowMessageFactory.gameOver(player);
			exitFromTheGame(userId);
			MentionListenerPlus.reply(failed, status, false);
			return;
		}

		String progress = HitAndBlowMessageFactory.progress(player, result);
		MentionListenerPlus.reply(progress, status, false);
	}

	private static void loadPlayers(){
		System.out.println("intoLoad");
		try {
			Tools.getTextLineStream(filePath).forEach(e->{
				players.put(Long.parseLong(e.split("/")[0]), new HitAndBlowPlayer(e));
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("exitload");
	}

	private static int loops = 0;
	public static void writePlayers(){
		String[] stringArray = new String[players.size()];
		loops = 0;
		players.keySet().stream().forEach(e->{
			stringArray[loops] = players.get(e).toString();
			loops++;
		});
		try {
			Tools.writeAllNewText(filePath, stringArray);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void joinTheGame(long userId,int length){
		players.put(userId, new HitAndBlowPlayer(userId, tools.generateNumber(length), 0));
	}

	private void exitFromTheGame(long userId){
		players.remove(userId);
	}

	private void registerShutdownHook(){
		System.out.println("intoshut");
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				HitAndBlowListener.writePlayers();
			}
		});
		System.out.println("exitshut");
	}

	@Override
	public void whenMentioned(Status status) {
	}

}
