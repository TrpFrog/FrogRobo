package net.trpfrog.frogrobo.mini_tools;

import java.util.Random;

import org.apache.commons.lang3.text.StrBuilder;

import net.trpfrog.frogrobo.streaming.MentionListenerPlus;
import twitter4j.Status;

public class FortuneListener extends MentionListenerPlus {

	public static void main(String[] args) {

		FortuneListener fl = new FortuneListener();

		int[] lucks = new int[5];

		lucks[fl.TOTAL_LUCK] = fl.fortuneTeller();
		for(int i=1; i < lucks.length ; i++){
			if(lucks[i-1] >= 9){
				lucks[i] = fl.unscrupulousFortuneTeller();
			}else{
				lucks[i] = fl.fortuneTeller();
			}
		}

		StringBuilder sb = new StringBuilder();

		sb = appendln(sb,"【総合運】");
		sb = appendln(sb,fl.starBuilder(lucks[fl.TOTAL_LUCK]));
		sb = appendln(sb,"【金銭運】");
		sb = appendln(sb,fl.starBuilder(lucks[fl.LUCK_OF_MONEY]));
		sb = appendln(sb,"【恋愛運】");
		sb = appendln(sb,fl.starBuilder(lucks[fl.LUCK_OF_LOVE]));
		sb = appendln(sb,"【仕事運】");
		sb = appendln(sb,fl.starBuilder(lucks[fl.LUCK_OF_WORK]));
		sb = appendln(sb,"【健康運】");
		sb = appendln(sb,fl.starBuilder(lucks[fl.LUCK_OF_HEALTH]));

		System.out.println(sb.toString());
	}

	private static StringBuilder appendln (StringBuilder sb, String str){
		sb.append(str);
		sb.append(System.lineSeparator());
		return sb;
	}

	@Override
	public String getCommandName() { return "Fortune"; }


	@Override
	public String getCommandUsage() { return this.getCommandLowerCase(); }
	@Override
	public String getCommandDescription() {return "占います☆";}
	@Override
	public String[] getCommandExceptionDescription() { return null; }

	private final byte TOTAL_LUCK = 0;
	private final byte LUCK_OF_MONEY = 1;
	private final byte LUCK_OF_LOVE = 2;
	private final byte LUCK_OF_WORK = 3;
	private final byte LUCK_OF_HEALTH = 4;
	@Override
	public void doWhenReceiveCommand(Status status, String[] commands) {

		int[] lucks = new int[5];

		lucks[0] = fortuneTeller();
		for(int i=1; i < lucks.length ; i++){
			if(lucks[i-1] >= 9){
				lucks[i] = unscrupulousFortuneTeller();
			}else{
				lucks[i] = fortuneTeller();
			}
		}

		String user = status.getUser().getScreenName().toLowerCase();
		if(user.equals("isseki0512")||user.equals("densan_asobi")||user.equals("sirorenga")||user.equals("sirodokei")){
			for(int i=0; i < lucks.length ; i++){
				lucks[i]=1;
			}
		}

		String msg = "";
		StrBuilder sb = new StrBuilder();
		if(commands.length==2){
			msg = (originalFortune(lucks));
		}else{
			sb.appendNewLine();
			for(int i=2; i < (commands.length); i++){
				if(commands[i].length()>13){
					sb.appendln("【占う内容の文字数は12文字以下にしてください】");
					continue;
				}
				sb.appendln("【"+commands[i]+"運】");
				sb.appendln(starBuilder(lucks[i-2]));
			}
			msg = sb.toString();
		}

		MentionListenerPlus.reply(msg, status, false);
	}

	@Override
	public void whenMentioned(Status status) {
	}

	private int fortuneTeller(){

		//------[０～５ダイス＋１～６ダイス]------//
		/*
		 * ★☆☆☆☆☆☆☆☆☆ - 3.33%
		 * ★★☆☆☆☆☆☆☆☆ - 6.67%
		 * ★★★☆☆☆☆☆☆☆ - 10.00%
		 * ★★★★☆☆☆☆☆☆ - 13.33%
		 * ★★★★★☆☆☆☆☆ - 16.67%
		 * ★★★★★★☆☆☆☆ - 16.67%
		 * ★★★★★★★☆☆☆ - 13.33%
		 * ★★★★★★★★☆☆ - 10.00%
		 * ★★★★★★★★★☆ - 6.67%
		 * ★★★★★★★★★★ - 3.33%
		 */

		Random dice = new Random();
		return dice.nextInt(7)+(dice.nextInt(4)+1);
	}

	private int unscrupulousFortuneTeller(){

		//------[１～３ダイス×１～３ダイス＋０or１コイン]------//
		/*
		 * ★☆☆☆☆☆☆☆☆☆ - 5.56%
		 * ★★☆☆☆☆☆☆☆☆ - 16.67%
		 * ★★★☆☆☆☆☆☆☆ - 22.22%
		 * ★★★★☆☆☆☆☆☆ - 16.67%
		 * ★★★★★☆☆☆☆☆ - 5.56%
		 * ★★★★★★☆☆☆☆ - 11.11%
		 * ★★★★★★★☆☆☆ - 11.11%
		 * ★★★★★★★★☆☆ - 0.00%
		 * ★★★★★★★★★☆ - 5.56%
		 * ★★★★★★★★★★ - 5.56%
		 */

		Random dice = new Random();
		return (dice.nextInt(3)+1)*(dice.nextInt(3)+1)+(dice.nextInt(2));
	}

	private String starBuilder(int number1to10){
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<number1to10; i++){
			sb.append("★");
		}
		for(int i=0; i<(10-number1to10); i++){
			sb.append("☆");
		}
		return sb.toString();
	}

	private String originalFortune(int[] lucks){
		System.out.println("通過");
		StrBuilder sb = new StrBuilder();
		sb.appendNewLine();
		sb.appendln("【総合運】");
		sb.appendln(starBuilder(lucks[0]));
		sb.appendln("【金銭運】");
		sb.appendln(starBuilder(lucks[1]));
		sb.appendln("【恋愛運】");
		sb.appendln(starBuilder(lucks[2]));
		sb.appendln("【仕事運】");
		sb.appendln(starBuilder(lucks[3]));
		sb.appendln("【健康運】");
		sb.appendln(starBuilder(lucks[4]));
		System.out.println(sb.toString());
		return sb.toString();
	}

}
