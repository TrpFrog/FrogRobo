package net.trpfrog.frogrobo.update_name;

import net.trpfrog.frogrobo.streaming.MentionListenerPlus;
import net.trpfrog.frogrobo.streaming.TrpFrogUserStream;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;


public class UpdateNameListener extends MentionListenerPlus {
    @Override
    public String getCommandName() {
        return "update_name";
    }

    @Override
    public String getCommandUsage() {
        return getCommandName()+" [名前]";
    }

    @Override
    public String getCommandDescription() {
        return "TrpFrogの名前を変えます";
    }

    @Override
    public String[] getCommandExceptionDescription() {
        String[] str = {"0字か50字を超える場合","名前に使用できない文字が含まれる場合"};
        return str;
    }


    protected static Twitter myAccount = TrpFrogUserStream.getInstance().getTwitter();

    final byte NAME_LENGTH_MAX = 50;
    final byte NAME_LENGTH_MIN = 1;


    @Override
    public void doWhenReceiveCommand(Status status, String[] commands) {
        final String BR = "\n";

        String name = status.getText();
        int start = name.toLowerCase().indexOf("update_name")+11; //初めのupdate_nameのすぐ後ろをさす
        name = name.substring(start);
        name = name.strip();
        name = name.replaceAll(BR, "");

        if (name.length() < NAME_LENGTH_MIN) {

            String msg = "名前が空欄だと改名できません！";
            reply(msg, status, true);
            return;

        } else if (NAME_LENGTH_MAX < name.length())  {

            String msg = "名前が長過ぎます......"+NAME_LENGTH_MAX+"文字以下にしてください......";
            reply(msg, status, true);
            return;

        } else if (NGWordListener.isNG(name)){
            String msg = "エラーが発生しました...";
            reply(msg, status, true);
            return;

        } else {

            try {
                rename(name, status);
            } catch (TwitterException e) {
                String msg = "エラーが発生しました...";
                reply(msg, status, true);
            }

        }
    }

    @Override
    public void whenMentioned(Status status) {
        boolean updNameTweet = false;
        try {
            updNameTweet = status.getText().toLowerCase().matches("\\(*@"+myAccount.getScreenName().toLowerCase()+"*\\)$");
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        if (!updNameTweet){
            return;
        }

        String name = status.getText().replaceAll("\\( *@.*\\)$","");
        if (name.length() < NAME_LENGTH_MIN || NAME_LENGTH_MAX < name.length()){
            return;
        }

        try {
            rename(name,status);
        } catch (TwitterException e) {
            //nothing to do
        }
    }

    protected static void rename(String name, Status status) throws TwitterException {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(" になりました");
        sb.append(System.lineSeparator());
        sb.append("(@");
        sb.append(status.getUser().getScreenName());
        sb.append(" の命令)");
        myAccount.updateProfile(name, null, null, null);
        myAccount.updateStatus(sb.toString());
    }


}
