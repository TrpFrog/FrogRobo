package net.trpfrog.frogrobo.reboot;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.SystemUtils;

public class Updater {

	public static void main(String[] args) {
		try {
			update();
		} catch (GitFailedException e) {
			e.printStackTrace();
		}
		launch();
	}

	private static void update() throws GitFailedException {
		if(SystemUtils.IS_OS_MAC_OSX || SystemUtils.IS_OS_LINUX){
			ProcessBuilder pb = new ProcessBuilder();
			pb.directory(new File(System.getProperty("user.dir")));
			pb.command("git");
			pb.command("pull");
			pb.command("https://github.com/TrpFrog/FrogRobo.git");
			Process p;
			try {
				p = pb.start();
				int ret = p.waitFor();
				if(ret != 0) throw new GitFailedException("gitでミスった");
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}

		}else if(SystemUtils.IS_OS_WINDOWS){
			System.out.println("Windowsではアップデートしません");
		}
	}

	private static void launch(){
		if(SystemUtils.IS_OS_MAC_OSX || SystemUtils.IS_OS_LINUX){
			ProcessBuilder pb = new ProcessBuilder();
			final String FILE_SEPARATOR = SystemUtils.FILE_SEPARATOR;

			StringBuilder path = new StringBuilder();
			path.append(".");
			path.append(FILE_SEPARATOR);
			path.append("bin");
			path.append(FILE_SEPARATOR);
			path.append("jp");
			path.append(FILE_SEPARATOR);
			path.append("gr");
			path.append(FILE_SEPARATOR);
			path.append("java_conf");
			path.append(FILE_SEPARATOR);
			path.append("trpfrog");
			path.append(FILE_SEPARATOR);
			path.append("frogrobo");
			path.append(FILE_SEPARATOR);
			path.append("FrogRobo.class");

			pb.directory(new File(System.getProperty("user.dir")));
			pb.command("java");
			pb.command(path.toString());

			Process p;
			try {
				p = pb.start();
				int ret = p.waitFor();
				System.out.println(ret);
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}

			System.exit(0);

		}else if(SystemUtils.IS_OS_WINDOWS){
			System.out.println("Windowsではアップデートしません");
		}
	}
}
class GitFailedException extends Exception{
	public GitFailedException(String msg){
		super(msg);
	}
}
