package net.trpfrog.frogrobo.update_name;

import java.util.Date;

import net.trpfrog.frogrobo.streaming.MentionListener;
import net.trpfrog.frogrobo.streaming.TrpFrogUserStream;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class OldUpdateNameListener implements MentionListener {

	private static Twitter myAccount = TrpFrogUserStream.getInstance().getTwitter();

	@Override
	public void mention(Status status) {

		if(disable){ return;}

		final String WRITER_ID = "@"+status.getUser().getScreenName();
		final String BR = "\n";

		System.out.println(status.getUser().getName()+": "+status.getText());

		String myAccountSN = null;
		try {
			myAccountSN = myAccount.getScreenName();
		} catch (IllegalStateException | TwitterException e) {
			e.printStackTrace();
		}
		final String MY_ACCOUNT_SN = myAccountSN;
		final String LOWER_MY_ACCOUNT_SN = MY_ACCOUNT_SN.toLowerCase();
		myAccountSN = null;

		System.out.println(status.getText().split("[ ,"+BR+"]")[1]);

		final boolean UPDATE_NAME_TWEET_TYPE_1 =
				status.getText().split("[ ,\n]")[1].equals("update_name");
		final boolean UPDATE_NAME_TWEET_TYPE_2 =
				status.getText().toLowerCase()
					.matches("\\( *@"+LOWER_MY_ACCOUNT_SN+" *\\)$");

		final boolean UPDATE_NAME_TWEET =
				status.isRetweet() == false &&
				(UPDATE_NAME_TWEET_TYPE_1 || UPDATE_NAME_TWEET_TYPE_2);

		if(UPDATE_NAME_TWEET){

			System.out.println("this is update name");

			String name = null;
			if(UPDATE_NAME_TWEET_TYPE_1){
				name = status.getText().replaceAll("^@.* update_name","");//match -> delete
				name = name.trim();
				name = name.replaceAll(BR, "");
			}else if(UPDATE_NAME_TWEET_TYPE_2){
				name = status.getText().replaceAll("\\( *@.*\\)$","");//match -> delete
			}

			final byte NAME_LENGTH_MAX = 20;
			final byte NAME_LENGTH_MIN = 1;
			if(name.length() > NAME_LENGTH_MAX||NAME_LENGTH_MIN > name.length()){

				String msg = null;

				if(NAME_LENGTH_MIN > name.length() && UPDATE_NAME_TWEET_TYPE_1){
					msg = WRITER_ID +BR+ "名前が空欄だと改名できません！"
							+BR+"["+ new Date()+"]";

				}if(name.length() > NAME_LENGTH_MAX && UPDATE_NAME_TWEET_TYPE_1){
					msg = WRITER_ID +BR+ "名前が長過ぎます...20文字以下にしてください..."
							+BR+"["+ new Date()+"]";
				}

				StatusUpdate reply = new StatusUpdate(msg).inReplyToStatusId(status.getId());
				try {
					myAccount.updateStatus(reply);
				} catch (TwitterException e) {
					e.printStackTrace();
				}

			}else{
				StringBuilder sb = new StringBuilder();
				sb.append(name);
				sb.append(" になりました");
				sb.append(BR);
				sb.append("(@");
				sb.append(status.getUser().getScreenName());
				sb.append(" の命令)");

				try {
					myAccount.updateProfile(name,null,null,null);
					myAccount.updateStatus(sb.toString());
				} catch (TwitterException e) {
					String msg = WRITER_ID + BR + "エラーが発生しました...";
					StatusUpdate reply = new StatusUpdate(msg).inReplyToStatusId(status.getId());
					try {
						myAccount.updateStatus(reply);
					} catch (TwitterException ex) {
						ex.printStackTrace();
					}
				}//end catch

			}//end else
		}//end if
	}

	@Override
	public String getCommandName() {
		return "update_name";
	}

	@Override
	public String getShortCommand() { return "upnm"; }

	@Override
	public String getCommandUsage() {
		return "update_name [改名させたい名前]";
	}

	@Override
	public String getCommandDescription() {
		return "@TrpFrogの名前を強制的に改名させます";
	}

	@Override
	public String[] getCommandExceptionDescription() {
		String[] str = {"指定した名前が0文字か20文字を超えた場合"};
		return str;
	}

	@Override
	public void reply(Status status, String[] commands) {
	}

	private boolean disable = false;
	@Override
	public void setDisable(boolean b) {
		disable = b;
	}
	@Override
	public boolean isDisable() {
		return disable;
	}


}
