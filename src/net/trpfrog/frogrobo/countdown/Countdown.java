package net.trpfrog.frogrobo.countdown;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.trpfrog.frogrobo.main.Tools;
import net.trpfrog.frogrobo.streaming.MentionListenerPlus;
import net.trpfrog.frogrobo.streaming.StreamListener;
import net.trpfrog.frogrobo.streaming.TweetStream;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class Countdown {

}


class CountdownListener implements StreamListener {

	@Override
	public void allTweet(Status status) {
	}

	@Override
	public void normalTweet(Status status) {
		if(status.getUser().getId() != TweetStream.FROGROBO_USER_ID){
			return;
		}
		boolean hasTodayBecomeTommorow = status.getText().matches(
				"日付が変わりました\n本日は\\d{4}年[12]?\\d月[1-3]?\\d日\n"
						+ "(月|火|水|木|金|土|日)曜日です"); //つまみロボのツイートを検知する

		if(hasTodayBecomeTommorow==false) return;

		String[] date = status.getText().split(
				"(日付が変わりました\n本日は|年|月|日|\n(月|火|水|木|金|土|日)曜日です)");
		List<Integer> dateList = new ArrayList<>();
		Arrays.stream(date)
		.filter(e->e!="")
		.forEach(e->dateList.add(Integer.parseInt(e)));
		final byte YEAR = 0;
		final byte MONTH = 1;
		final byte DAY = 2;
		Map<String,Date> eventMap = new HashMap<>();
		try {
			Tools.getTextLineStream(".","SecretFiles","CountdownList.csv")
			.forEach(e->{
				String[] data = e.split(",");
				if(data.length != 2) return;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
				try {
					Date d = sdf.parse(data[1]);
					eventMap.put(data[0], d);
				} catch (ParseException e1) {
					return;
				}
			});
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		//long today = new Date().getTime();








		//-----------------[臨時]------------------//
		String event = "東京都中学校吹奏楽コンクール第６日目(由木中本番)";
		if(dateList.get(YEAR)==2015){
			if(dateList.get(MONTH)==7){
				int count = 38-dateList.get(DAY);
				countdown(count,event);
			}else if(dateList.get(MONTH)==8){
				int count = 7-dateList.get(DAY);
				countdown(count,event);
			}
		}



	}

	public static void countdown(int day,String eventName){
		ConfigurationBuilder cb = new ConfigurationBuilder()
		.setOAuthConsumerKey("3H5oRwJSdq9VFNbcQljVDTolX")
		.setOAuthConsumerSecret("WJcWa1sPPkxokICfYQdUOADbfdIziYLH1DanzjZZS0xhc4KhH3")
		.setOAuthAccessToken("3150113654-BYtfuWuEY17noxX7Wv95ssnYm939MihtFb3RxKB")
		.setOAuthAccessTokenSecret("cAGpJGTOEQa4hADVfx3gaej3ypPEnPsmAMK3XShhR9FOb")
		.setDebugEnabled(true);
		Twitter twitter = new TwitterFactory(cb.build()).getInstance();
		try {
			twitter.updateStatus(eventName+" まであと "+day+"日");
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}

}

class CountdownLoadListener extends MentionListenerPlus {

	@Override
	public String getCommandName() {
		return null;
	}

	@Override
	public String getShortCommand() { return "ctdw"; }

	@Override
	public String getCommandUsage() {
		return null;
	}

	@Override
	public String getCommandDescription() {
		return null;
	}

	@Override
	public String[] getCommandExceptionDescription() {
		return null;
	}

	@Override
	public void doWhenReceiveCommand(Status status, String[] commands) {
	}

	@Override
	public void mention(Status status) {
	}

}

/*
 * CountDown.csv Format
 * EVENT,day
 */