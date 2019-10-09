package net.trpfrog.frogrobo.hit_and_blow;

import org.apache.commons.lang3.text.StrBuilder;

public class HitAndBlowMessageFactory {
	public static String gameOver(HitAndBlowPlayer player){
		StrBuilder gameover = new StrBuilder();
		gameover.appendNewLine();
		gameover.appendln("【ゲームオーバー！】");
		gameover.appendln("手数オーバーです......");
		gameover.append("答え: ");
		gameover.appendln(player.getSolution());
		gameover.appendln("手数: ");
		return gameover.toString();
	}
	public static String cleared(HitAndBlowPlayer player){
		StrBuilder clear = new StrBuilder();
		clear.appendNewLine();
		clear.appendln("【正解！】");
		clear.append("答え: ");
		clear.appendln(player.getSolution());
		clear.append("手数: ");
		clear.appendln(player.getTrials());
		return clear.toString();
	}

	public static String progress(HitAndBlowPlayer player, HitAndBlowResult result){
		StrBuilder sb = new StrBuilder();
		sb.appendNewLine();
		sb.append("【");
		sb.append(player.getTrials());
		sb.append("回目 (残り");
		sb.append(10 - player.getTrials());
		sb.appendln("回)】");
		sb.append(result.getHit());
		sb.appendln(" Hit");
		sb.append(result.getBlow());
		sb.appendln(" Blow");
		return sb.toString();
	}
}
