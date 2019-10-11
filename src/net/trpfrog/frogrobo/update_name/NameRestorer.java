package net.trpfrog.frogrobo.update_name;

import net.trpfrog.frogrobo.FrogRobo;
import net.trpfrog.frogrobo.Tools;
import twitter4j.TwitterException;

import java.io.IOException;
import java.util.Iterator;

public class NameRestorer {
    static long lastUpdateTime;
    static String lastUpdateName;
    static String defaultName;
    public static long getLastUpdateTime() {
        return lastUpdateTime;
    }
    public static String getLastUpdateName() {
        return lastUpdateName;
    }
    public static String getDefaultName() {
        return defaultName;
    }

    public static void main(String[] args) {
        readUpdateNameProperties();
        System.out.println(lastUpdateTime);
        System.out.println(lastUpdateName);
        System.out.println(defaultName);
        setLatestName("うんこ");
        writeUpdateNameProperties();
    }

    private static boolean isFirst = true;

    static NameCheckThread thread = new NameCheckThread();

    synchronized static void setLatestName(String name){
        if(isFirst){
            defaultSetting();
            thread.start();
        }
        lastUpdateTime = System.currentTimeMillis();
        lastUpdateName = name;
    }

    static void setDefaultName(String name){
        defaultName = name;
    }

    private static void defaultSetting(){
        readUpdateNameProperties();
        Runtime.getRuntime().addShutdownHook(new Thread(NameRestorer::writeUpdateNameProperties));
    }

    public static void writeUpdateNameProperties(){
        String filePath1 = Tools.generatePath (FrogRobo.FILE_PATH,"Files","update_name.txt");
        try {
            Tools.writeAllNewText(filePath1,
                    getLastUpdateTime()+"",
                    getLastUpdateName(),
                    getDefaultName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readUpdateNameProperties(){
        String filePath = Tools.generatePath (FrogRobo.FILE_PATH,"Files","update_name.txt");
        try {
            Iterator<String> iterator = Tools.getTextLineStream(filePath).iterator();
            lastUpdateTime = Long.parseLong(iterator.next());
            lastUpdateName = iterator.next();
            defaultName = iterator.next();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static void restoreName(){
        StringBuilder sb = new StringBuilder();
        sb.append("名前を"+defaultName+"に戻しました。");
        sb.append(System.lineSeparator());
        sb.append("(つまみロボの命令)");
        try {
            UpdateNameListener.myAccount.updateProfile(defaultName, null, null, null);
            UpdateNameListener.myAccount.updateStatus(sb.toString());
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

}

class NameCheckThread extends Thread {
    @Override
    public void run(){
        final long ID = 92482871;
        String name = "";
        boolean is12HourPassed;
        boolean isRenamed;
        boolean isDefaultName;
        while(true){
            try {
                sleep(1000*60*5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                name = UpdateNameListener.myAccount.showUser(ID).getName();
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            is12HourPassed = System.currentTimeMillis() - NameRestorer.getLastUpdateTime() > 1000*60*60*12; //12時間経過
            isRenamed = name.equals(NameRestorer.getLastUpdateName());
            isDefaultName = name.equals(NameRestorer.getDefaultName());
            if(is12HourPassed && isRenamed && !isDefaultName){
                NameRestorer.restoreName();
            }
        }
    }
}