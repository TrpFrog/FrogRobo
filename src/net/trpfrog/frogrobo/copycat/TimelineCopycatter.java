package net.trpfrog.frogrobo.copycat;

import java.util.List;
import java.util.Random;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TimelineCopycatter{
	private static Thread theInstance = new Thread(new Runnable(){
		@Override
		public void run(){

			Twitter frogRobo = new TwitterFactory().getInstance();

			ConfigurationBuilder cb = new ConfigurationBuilder();
			 cb.setDebugEnabled(true)
			 	.setOAuthConsumerKey("3H5oRwJSdq9VFNbcQljVDTolX")
			 	.setOAuthConsumerSecret("WJcWa1sPPkxokICfYQdUOADbfdIziYLH1DanzjZZS0xhc4KhH3")
			 	.setOAuthAccessToken("3150113654-BYtfuWuEY17noxX7Wv95ssnYm939MihtFb3RxKB")
			 	.setOAuthAccessTokenSecret("cAGpJGTOEQa4hADVfx3gaej3ypPEnPsmAMK3XShhR9FOb");
			 Twitter lockedRobo = new TwitterFactory(cb.build()).getInstance();

			do{

				while(running){
					final int MAX_TWEET_COUNT = 200;
					final byte PAGE_COUNT = 1;
					List<Status> list_status = null;
					try {
						list_status = frogRobo.getHomeTimeline(new Paging(PAGE_COUNT,MAX_TWEET_COUNT));
					} catch (TwitterException e1) {
						e1.printStackTrace();
					}

					do{
						System.out.println(list_status.size());
						final int TWEET_SELECTER = new Random().nextInt(list_status.size()); // 乱数の生成
						Status status = list_status.get(TWEET_SELECTER);
						System.out.println(TWEET_SELECTER+"："+status.getText());

						final boolean MY_TWEET = status.getUser().getScreenName().equals("FrogRobo");
						final boolean RETWEET  = status.isRetweet();
						final boolean LOCKED   = status.getUser().isProtected();
						final boolean FAVORITE = 0 < status.getFavoriteCount();
						final boolean REPLY    = status.getInReplyToScreenName()!=null;
						final boolean MENTION  = status.getText().matches("@[a-z]");


						final String LS = System.getProperty("line.separator");

						StringBuilder result = new StringBuilder();
						result.append("Writer  : @"+ status.getUser().getScreenName() +LS);
						result.append("Date    : "+ status.getCreatedAt()+LS);
						result.append("ReplyTo : "+ status.getInReplyToScreenName() +LS);
						result.append("MyTweet : "+ MY_TWEET +LS);
						result.append("Retweet : "+ RETWEET  +LS);
						result.append("Locked  : "+ LOCKED   +LS);
						result.append("Favorite: "+ FAVORITE +LS);
						result.append("Reply   : "+ REPLY    +LS);
						result.append("Mention : "+ MENTION  +LS);
						System.out.println(result);

						final boolean DO_COPY = (MY_TWEET||RETWEET||REPLY)==false;
						if(DO_COPY==true){
							try{
								StringBuilder sb = new StringBuilder();
								sb.append("パクツイ(from ");
								sb.append(status.getUser().getScreenName());
								sb.append(")：");
								sb.append(LS);
								sb.append(status.getText());

								if(LOCKED==false) frogRobo.updateStatus(sb.toString());
								if(LOCKED==true) lockedRobo.updateStatus(sb.toString());
								System.out.println(sb);

							}catch(Exception e){
								continue;
							}
							break;// 1回パクツイしたら速攻逃げるスタイル
						}


					}while(true);


					try {
						final short TO_MS = 1000;
						final byte TO_SEC = 60;
						final byte MINUTE = 15;
						Thread.sleep(MINUTE*TO_SEC*TO_MS);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}while(true);
		}
	});
	private static volatile boolean running = true;

	private TimelineCopycatter(){
	}

	public static Thread getInstance(){
		return theInstance;
	}

	public static void startRunning(){
		if(!theInstance.isAlive()) theInstance.start();
		setRunning(true);
	}

	public static void stopRunning(){
		setRunning(false);
	}

	public static void toggleRunning(){
		setRunning(!running);
	}

	private static synchronized void setRunning(boolean b){
		running = b;
	}
}





