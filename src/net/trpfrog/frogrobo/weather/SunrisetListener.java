package net.trpfrog.frogrobo.weather;

import org.apache.commons.lang3.text.StrBuilder;

import net.trpfrog.frogrobo.mini_tools.ToolsLoader;
import net.trpfrog.frogrobo.streaming.MentionListenerPlus;
import twitter4j.Status;

public class SunrisetListener extends MentionListenerPlus {

	@Override
	public String getCommandName() { return "sunriset"; }

	@Override
	public String getCommandUsage() { return this.getCommandLowerCase()+" [地名]"; }
	@Override
	public String getCommandDescription() {return "日の出日の入りチェッカーーーーーーーーー！";}
	@Override
	public String[] getCommandExceptionDescription() { return null; }


	private static final int LOCATION = 2;
	private static final int ALLOW_VIEW_LOCATION = 3;

	@Override
	public void doWhenReceiveCommand(Status status, String[] commands) {
		int lengthOfCmds = commands.length -1;
		
		boolean allowViewLocation = true;

		System.out.println(commands.length);
		System.out.println(super.getCmdsLength());

		if(lengthOfCmds < LOCATION){
			return;
		}

		WeatherApi wp = null;
		if(commands[LOCATION].toLowerCase().equals("geo")){
			allowViewLocation = false;
			double lat = status.getGeoLocation().getLatitude();
			double lon = status.getGeoLocation().getLongitude();
			try {
				wp = new WeatherApi(lat, lon, 0);
			} catch (APINetworkErrorException e) {
				ToolsLoader.reply("お住まいの地域は対応していないか、位置情報機能がオフになっている可能性があります", status, true);
				return;
			}
		}else{
			try {
				wp = new WeatherApi(commands[LOCATION],0);
				System.out.println(wp.getLocationName());
			} catch (IllegalArgumentException | APINetworkErrorException e) {
				ToolsLoader.reply("存在しない地名です", status, true);
				return;
			}
		}

		if (lengthOfCmds >= ALLOW_VIEW_LOCATION) {
			if (commands[ALLOW_VIEW_LOCATION].equalsIgnoreCase("true")) {
				allowViewLocation = true;
			} else if (commands[ALLOW_VIEW_LOCATION].equalsIgnoreCase("false")) {
				allowViewLocation = false;
			}
		}

		StrBuilder sb = new StrBuilder();
		sb.appendNewLine();
		sb.append("【");
		if(allowViewLocation){
			sb.append(wp.getLocationName());
			sb.append(" (");
			sb.append(wp.getCountryCode());
			sb.append(")の");
		}
		sb.appendln("今日の日出没の時間】");
		sb.append("　日の出: ");
		sb.appendln(wp.getSunrise());
		sb.append("日の入り: ");
		sb.appendln(wp.getSunset());

		ToolsLoader.reply(sb.toString(), status, true);
	}

	@Override
	public void whenMentioned(Status status) {
	}


}
