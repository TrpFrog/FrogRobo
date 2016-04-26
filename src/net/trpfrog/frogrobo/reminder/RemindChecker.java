package net.trpfrog.frogrobo.reminder;

public class RemindChecker {
	static Thread timeCheck = null;
	public static void startThread(){

	}
}

class TimeCheckThread extends Thread {
	@Override
	public void run(){
		while(true){
			System.currentTimeMillis();




			try {
				TimeCheckThread.sleep(60*1000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
