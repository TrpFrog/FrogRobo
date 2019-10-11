package net.trpfrog.frogrobo.mini_tools;

import java.util.Random;

import net.trpfrog.frogrobo.streaming.MentionListenerPlus;
import twitter4j.Status;

public class ChanceListener extends MentionListenerPlus {

	@Override
	public String getCommandName() { return "Chance"; }

	@Override
	public String getCommandUsage() { return this.getCommandLowerCase(); }
	@Override
	public String getCommandDescription() {return "確率をつまみロボが計算して割り出すよ～！(大嘘)";}
	@Override
	public String[] getCommandExceptionDescription() { return null; }

	@Override
	public void doWhenReceiveCommand(Status status, String[] commands) {
		if(commands.length == 2)
			return;
		String text = status.getText()
				.replaceAll(commands[0]+"( |\\n)"+commands[1]+"( |\\n)", "")
				.replaceAll("確率$", "") + "確率";



		Random rand = new Random(System.currentTimeMillis());
		int integer = rand.nextInt(101); //0~100

		String decimalStr = "";
		rand = new Random(System.currentTimeMillis()+1000);
		int decimal = rand.nextInt(100); //0~99
		if(integer == 100){
			decimalStr = "00";
		}else if(decimal < 10){
			decimalStr = "0"+decimal;
		}else{
			decimalStr = decimal+"";
		}

		String probability = integer+"."+decimalStr+"％";

		MentionListenerPlus.reply("\n"+text+"\n【"+probability+"】", status, false);
	}

	@Override
	public void whenMentioned(Status status) {
	}

}
