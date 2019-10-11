package net.trpfrog.frogrobo.weather;

import net.trpfrog.frogrobo.streaming.MentionListener;
import org.apache.commons.lang3.text.StrBuilder;

import net.trpfrog.frogrobo.mini_tools.ToolsLoader;
import net.trpfrog.frogrobo.streaming.MentionListenerPlus;
import twitter4j.Status;

public class WeatherListener extends MentionListenerPlus{

	@Override
	public String getCommandName() { return "weather"; }

	@Override
	public String getCommandUsage() { return this.getCommandLowerCase()+" [地名] [Opt.:何日後の天気(0-6)]"; }
	@Override
	public String getCommandDescription() {return "起動しているかの確認に使います";}
	@Override
	public String[] getCommandExceptionDescription() { return null; }

	private static final int LOCATION = 2;
	private static final int DAYS_LATER = 3;
	private static final int ALLOW_VIEW_LOCATION = 4;

	@Override
	public void doWhenReceiveCommand(Status status, String[] commands) {
		int lengthOfCmds = commands.length -1;
		if(lengthOfCmds < LOCATION){
			return;
		}

		boolean allowViewLocation = true;
		int daysLater = 0;
		if (lengthOfCmds >= DAYS_LATER) {
			try {
				daysLater = Integer.parseInt(commands[DAYS_LATER]);
			} catch (NumberFormatException e) {
				//そんなこともあるさ
				daysLater = 0;
			}
			if(daysLater > 6){
				MentionListenerPlus.reply("6日後よりあと、今日より前の天気は表示できません", status, true);
				return;
			}
		}

		WeatherApi wp = null;
		if(commands[LOCATION].toLowerCase().equals("geo")){
			allowViewLocation = false;
			double lat = status.getGeoLocation().getLatitude();
			double lon = status.getGeoLocation().getLongitude();
			try {
				wp = new WeatherApi(lat, lon, daysLater);
			} catch (APINetworkErrorException e) {
				MentionListenerPlus.reply("お住まいの地域は対応していないか、位置情報機能がオフになっている可能性があります", status, true);
				return;
			}
		}else{
			try {
				wp = new WeatherApi(commands[LOCATION],daysLater);
				System.out.println(wp.getLocationName());
			} catch (IllegalArgumentException | APINetworkErrorException e) {
				MentionListenerPlus.reply("存在しない地名です", status, true);
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
		String date = "今日";
		if(daysLater > 0){
			date = wp.getDate();
		}

		StrBuilder sb = new StrBuilder();
		sb.appendNewLine();
		sb.append("【");
		sb.append(date);
		sb.appendln("の天気】");
		if(allowViewLocation){
			sb.append("場所: ");
			sb.append(wp.getLocationName());
			sb.append(" (");
			sb.append(wp.getCountryCode());
			sb.appendln(")");
		}
		sb.append("天気: ");
		sb.appendln(wp.getWeather());
		sb.append("気圧: ");
		sb.append(wp.getPressure());
		sb.appendln("hPa");
		sb.append("最高気温: ");
		sb.append(wp.getMaxTemperature());
		sb.appendln("℃");
		sb.append("最低気温: ");
		sb.append(wp.getMinTemperature());
		sb.appendln("℃");
		sb.appendNewLine();

		MentionListenerPlus.reply(sb.toString(), status, true);
	}

	@Override
	public void whenMentioned(Status status) {
	}

}