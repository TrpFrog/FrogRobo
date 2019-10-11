package net.trpfrog.frogrobo.update_name;

import net.trpfrog.frogrobo.streaming.TrpFrogUserStream;
import twitter4j.TwitterException;

import java.io.IOException;

public class UpdateNamerSetter {
	public static void main(){
		TrpFrogUserStream.getInstance().addMentionListener(new UpdateNameListener());
	}
}