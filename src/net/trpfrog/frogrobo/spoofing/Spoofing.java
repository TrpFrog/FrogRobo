package net.trpfrog.frogrobo.spoofing;

import net.trpfrog.frogrobo.streaming.TrpFrogUserStream;

public class Spoofing{
	public static void main(){
		TrpFrogUserStream.getInstance().addListener(new SpoofingListener());
	}
}