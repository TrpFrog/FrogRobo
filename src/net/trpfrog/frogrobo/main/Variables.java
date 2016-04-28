package net.trpfrog.frogrobo.main;


/**
 * 全体で扱う変数を管理するクラス
 * @author つまみ <Twitter:@TrpFrog>
 * @deprecated このクラスは新方式のON/OFF管理機能の登場により廃止されました
 */
@Deprecated
public class Variables {

	private static boolean roboOnOffSwitch;

	private static boolean copycatSwitch;

	private static boolean updateNameSwitch;

	private static boolean[] actions =
		{copycatSwitch,updateNameSwitch};


	public static boolean isUpdateNameSwitch() {
		return updateNameSwitch;
	}

	public static void setUpdateNameSwitch(boolean updateNameSwitch) {
		Variables.updateNameSwitch = updateNameSwitch;
	}

	public static synchronized boolean isRoboOnOffSwitch() {
		return roboOnOffSwitch;
	}

	public static synchronized void setRoboOnOffSwitch(boolean roboOnOffSwitch) {
		Variables.roboOnOffSwitch = roboOnOffSwitch;
	}

	public static synchronized boolean isCopycatSwitch() {
		return copycatSwitch;
	}

	public static synchronized void setCopycatSwitch(boolean b) {
		Variables.copycatSwitch = b;
	}

	public static void stopAllBot(){
		for(int i = 0;i<actions.length;i++){
			actions[i] = false;
		}
	}

	private Variables(){
	}

}
