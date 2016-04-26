package net.trpfrog.frogrobo.update_name;

import net.trpfrog.frogrobo.streaming.TrpFrogUserStream;

public class UpdateNamer{
	public static void main(){
		TrpFrogUserStream.getInstance().addListener(new OldUpdateNameListener());
	}
}