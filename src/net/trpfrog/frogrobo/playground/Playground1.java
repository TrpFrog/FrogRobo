package net.trpfrog.frogrobo.playground;

import java.io.IOException;

import net.trpfrog.frogrobo.FrogRobo;
import net.trpfrog.frogrobo.Tools;

public class Playground1 {
	public static void main(String[] args) {
		String path = Tools.generatePath(FrogRobo.FILE_PASS,"Files","HitAndBlowPlayers.txt");
		try {
			Tools.writeAllNewText(path,
					"TrpFrog 124124534",
					"negiiiiiissei 42124245",
					"Rei_248 34235545",
					"ilike510 531434311");
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
}
