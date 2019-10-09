package net.trpfrog.frogrobo.streaming;

import net.trpfrog.frogrobo.FrogRobo;
import net.trpfrog.frogrobo.autoreply.AutoReply;
import twitter4j.Status;
import twitter4j.TwitterException;

import java.util.*;
import java.util.stream.Stream;

public abstract class CommandStreaming extends StreamingSetter{

    CommandStreaming  (String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret) throws TwitterException {
        super (consumerKey, consumerSecret, accessToken, accessTokenSecret);
        lowerScreenName = super.getTwitter().getScreenName().toLowerCase();
    }

    CommandStreaming(Stream<String> keyStream) throws TwitterException {
        super(keyStream);
        lowerScreenName = super.getTwitter().getScreenName().toLowerCase();
    }

    CommandStreaming () throws TwitterException {
        lowerScreenName = super.getTwitter().getScreenName().toLowerCase();
    }

    Map<String, MentionListener> mentionListenerMap = new HashMap<>();
    List<StreamListener> streamListenerList = new ArrayList<>();

    /**
     * メンションリスナーを追加します。
     * @param l
     */
    public synchronized void addMentionListener(MentionListener l){
        this.mentionListenerMap.put(l.getCommandName().toLowerCase(),l);
    }

    /**
     * メンションリスナーを削除します。
     * @param l
     */
    public synchronized void removeMentionListener(MentionListener l){
        if (this.mentionListenerMap.containsValue(l)) {
            this.mentionListenerMap.remove(l.getCommandName().toLowerCase());
        }
    }

    /**
     * ストリームリスナーを追加します。
     * ストリームリスナーはストリームを流れる全てのツイートに反応します。
     * @param l
     */
    public synchronized void addStreamListener(StreamListener l){
        this.streamListenerList.add(l);
    }

    /**
     * ストリームリスナーを削除します。
     * @param l
     */
    public synchronized void removeStreamListener(StreamListener l){
        if(this.streamListenerList.contains(l)){
            this.streamListenerList.remove(l);
        }
    }

    /**
     * メンションリスナーのマップを取得します。
     * @return
     */
    public Map<String,MentionListener> getMentionListenerMap(){
        return this.mentionListenerMap;
    }

    /**
     * ストリームリスナーのリストを取得します。
     * @return ストリームリスナー
     */
    public List<StreamListener> getStreamListenerList(){
        return this.streamListenerList;
    }

    /**
     * ツイートをスペースor改行単位で区切り、空白の要素を除いた配列を返します。
     * @param tweet
     * @return スペースor改行で区切ったツイートの配列
     */
    synchronized public static String[] splitCommands(String tweet){
        String spaceRegex = "[ \\n	　]";
        String[] commands = tweet.split(spaceRegex);
        List<String> cmdList = new ArrayList<>(Arrays.asList(commands));
        cmdList.removeIf(s -> s.equals(""));
        commands = cmdList.toArray(new String[cmdList.size()]);
        return commands;
    }

    private static final String SPACE_REGEX = "[ \\n	　](.|\\n)*";

    private String lowerScreenName;

    public static boolean isReply(String tweet, String yourScreenName){
        return tweet.toLowerCase().matches("^@"+ yourScreenName.toLowerCase() + SPACE_REGEX);
    }

    public boolean isReply(String tweet){
        return tweet.toLowerCase().matches("^@"+lowerScreenName + SPACE_REGEX);
    }

    public boolean isReply(Status status){
        return isReply(status.getText());
    }

    public static boolean isMention(String tweet, String yourScreenName){
        return tweet.toLowerCase().matches("@"+yourScreenName.toLowerCase()+ SPACE_REGEX)||isReply(tweet,yourScreenName);
    }

    public boolean isMention(String tweet){
        return tweet.toLowerCase().matches("@"+lowerScreenName+ SPACE_REGEX)||this.isReply(tweet);
    }

    public boolean isMention(Status status){
        return isMention(status.getText());
    }

    /**
     * ツイートをリスナーに通します。また、リプライでない場合は自動返信を行います。
     * @param status
     */
    public void tweetAction(Status status){

        if(status.isRetweet()) return;

        final boolean REPLY = isReply(status);
        final boolean MENTION = isMention(status);

        System.out.println("  isReply:"+REPLY);
        System.out.println("isMention:"+MENTION);

        if (MENTION) {

            String[] commands = splitCommands(status.getText());

            if(REPLY){
                String firstCmd = commands[1].trim().toLowerCase();
                if(mentionListenerMap.containsKey(firstCmd)) {
                    MentionListener listener = mentionListenerMap.get(firstCmd);
                    System.out.println("[cmd: " + firstCmd + "]");
                    listener.whenReplied(status, commands);
                }else {
                    System.out.println("キーがありません！");
                    new AutoReply().doWhenReceiveCommand(status,commands);
                }
            }else {
                //TODO: isMentionの処理を行う
            }

        } else {
            streamListenerList.forEach(listener->listener.normalTweet(status));
        }
    }

}
