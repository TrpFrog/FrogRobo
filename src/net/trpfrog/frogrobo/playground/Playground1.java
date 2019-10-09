package net.trpfrog.frogrobo.playground;

import java.io.IOException;
import java.util.*;

import net.trpfrog.frogrobo.FrogRobo;
import net.trpfrog.frogrobo.Tools;

public class Playground1 {

	public static void main(String[] args) {
		String spaceRegex = "[ \\n	　]";
		String[] commands = "@TrpFrog ping つまみ".split(spaceRegex);
		List<String> cmdList = new ArrayList<>(Arrays.asList(commands));
		cmdList.removeIf(s -> s.equals(""));
		String[] stLs = cmdList.toArray(new String[cmdList.size()]);

		for (String s : stLs) {
			System.out.println(s);
		}

	}
}
