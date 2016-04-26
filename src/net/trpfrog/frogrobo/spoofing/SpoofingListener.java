package net.trpfrog.frogrobo.spoofing;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Date;

import net.trpfrog.frogrobo.streaming.MentionListener;
import net.trpfrog.frogrobo.streaming.TrpFrogUserStream;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

public class SpoofingListener implements MentionListener {

	private static Twitter myAccount = TrpFrogUserStream.getInstance().getTwitter();

	private static User myUserInstance;

	//カレントディレクトリはプロジェクトフォルダになっている
	@Deprecated
	private static final String FILES_FOLDER_PASS = ".\\Files\\";

	private static File myProfileImage = new File(FILES_FOLDER_PASS+"TrpFrogProfileImage.png");


	public void reply(Status status, String[] commands) {

		if(disable){ return;}

		final boolean SPOOFING_TWEET = commands[1].toLowerCase().equals("spoofing");

		if(SPOOFING_TWEET==false) { return; }

		final boolean SPOOFING_DEFAULT = commands[1].equals("spoofing")
				&& commands[2].matches("DEFAULT");

		System.out.println(SPOOFING_TWEET+":"+SPOOFING_DEFAULT+";"+commands[2]);

		if (SPOOFING_TWEET&&SPOOFING_DEFAULT==false) {

			try {
				myUserInstance = myAccount.verifyCredentials();

				System.out.println("this is spoofing");

				writeProfileFile();

				User user = null;
				user = myAccount.showUser(commands[2]);

				File profileImage = new File(FILES_FOLDER_PASS+"SpoofingImage.jpg");

				try {
					myAccount.updateProfileImage(imageDownloader(profileImage, user));
				} catch (IOException e) {
					e.printStackTrace();
				}

				myAccount.updateProfile(
						user.getName(),
						user.getURL(),
						user.getLocation(),
						user.getDescription());

				StringBuilder sb = new StringBuilder();
				sb.append(user.getName());
				sb.append("(");
				sb.append(user.getScreenName());
				sb.append(") \nになりすましました");
				sb.append("\n(@");
				sb.append(status.getUser().getScreenName());
				sb.append(" の命令)");
				sb.append("\n[");
				sb.append(new Date());
				sb.append("]");
				myAccount.updateStatus(sb.toString());
				spoofingModeChanger(true);

			} catch (TwitterException ex) {
				ex.printStackTrace();
			}

		} else if (SPOOFING_DEFAULT) {

			//TODO なんとかしろ

			System.out.println("this is default");
			try{
				String[] profileData = readProfileFile();

				final String NAME = profileData[1];
				final String HP = profileData[2];
				final String LOCATION = profileData[3];
				final String INTRO = profileData[4];

				myAccount.updateProfile(NAME, HP, LOCATION, INTRO);
				myAccount.updateProfileImage(new File(FILES_FOLDER_PASS+"TrpFrogProfileImage.png"));

				spoofingModeChanger(false);

				myAccount.updateStatus(NAME+" に戻りました\n(つまみロボより)\n["+new Date()+"]");

			} catch (TwitterException e) {
				e.printStackTrace();
			}
		}
	}

	private File imageDownloader(File file,User user) throws IOException{

		URL url = new URL(user.getOriginalProfileImageURL());
		InputStream in = url.openStream();
		OutputStream out = new FileOutputStream(file);
		try {
			byte[] buffer = new byte[1024];
			int len = 0;

			while ((len = in.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
			out.flush();
		} finally {
			in.close();
			out.close();
		}
		return file;
	}

	private void writeProfileFile(){
		File profileFile = new File(FILES_FOLDER_PASS+"TrpFrogProfile.txt");
		try(FileWriter fw = new FileWriter(profileFile)){

			String[] profileData = readProfileFile();
			final boolean SPOOFING_MODE = profileData[0]!="true";

			if(SPOOFING_MODE==true){
				return;
			}



			imageDownloader(myProfileImage, myUserInstance);

			final String SPLITTER = "[SPLIT]";
			StringBuilder writeSb = new StringBuilder();
			writeSb.append(profileData[0]);
			writeSb.append(SPLITTER);
			writeSb.append(myUserInstance.getName());
			writeSb.append(SPLITTER);
			writeSb.append(myUserInstance.getURL());
			writeSb.append(SPLITTER);
			writeSb.append(myUserInstance.getLocation());
			writeSb.append(SPLITTER);
			writeSb.append(myUserInstance.getDescription());

			fw.write(writeSb.toString());
			fw.flush();
			fw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void spoofingModeChanger(boolean isSpoofingMode){
		StringBuilder writeSb = new StringBuilder();
		String[] profileData = readProfileFile();

		writeSb.append(!isSpoofingMode);
		writeSb.append("[SPLIT]");
		writeSb.append(profileData[1]);
		writeSb.append("[SPLIT]");
		writeSb.append(profileData[2]);
		writeSb.append("[SPLIT]");
		writeSb.append(profileData[3]);
		writeSb.append("[SPLIT]");
		writeSb.append(profileData[4]);

		try(FileWriter fw = new FileWriter(FILES_FOLDER_PASS+"TrpFrogProfile.txt")){
			fw.write(writeSb.toString());
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String[] readProfileFile(){
		String[] profileData = null;
		try(FileReader fr = new FileReader(FILES_FOLDER_PASS+"TrpFrogProfile.txt")){
				StringBuilder sb = new StringBuilder();
				int ch = 0;
				while((ch = fr.read())!= -1){
					sb.append((char)ch);
				}
				profileData = sb.toString().split("\\[SPLIT\\]");
			} catch (IOException e) {
				e.printStackTrace();
			}
		return profileData;
	}


	@Override
	public void mention(Status status) {

	}

	@Override
	public String getCommandName() {
		return "spoofing";
	}

	@Override
	public String getShortCommand() { return "spfg"; }

	@Override
	public String getCommandUsage() {
		return "spoofing [ユーザーID]";
	}

	@Override
	public String getCommandDescription() {
		return "指定したユーザーになりすまします";
	}

	@Override
	public String[] getCommandExceptionDescription() {
		return null;
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
