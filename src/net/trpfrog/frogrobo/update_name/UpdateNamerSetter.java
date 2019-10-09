package net.trpfrog.frogrobo.update_name;

import net.trpfrog.frogrobo.streaming.TrpFrogUserStream;

public class UpdateNamerSetter {
	public static void main(){
		TrpFrogUserStream.getInstance().addListener(new UpdateNameListener());
	}
}