package net.trpfrog.frogrobo;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import net.trpfrog.frogrobo.streaming.MentionListener;
import net.trpfrog.frogrobo.streaming.TweetStream;

/**
 * Files/ListenerLoader.txt からListenerのFQCNを受け取り追加するクラス
 */

public class ListenerLoader {

	public static void load() {
		String path = FrogRobo.FILE_PATH + "Files" + File.separator + "ListenerLoader.txt";
		try (Stream<String> listenerFQCN = Files.lines(Paths.get(path))){
			listenerFQCN.forEach(ListenerLoader::appendClassToListenerList);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void appendClassToListenerList(String listenerClassName){
		Class<?> listener;
		try {
			//get listener object from FQCN
			listener = Class.forName(listenerClassName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.err.println("クラスが見つかりません。");
			return;
		}

		try {
			MentionListener l = (MentionListener)listener.getDeclaredConstructor().newInstance();
			TweetStream.getInstance().addMentionListener(l);
		} catch (InvocationTargetException | IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException | ClassCastException | NoSuchMethodException e) {
			System.err.println(listenerClassName+"はMentionListenerを実装していない可能性があります。");
		}
	}
}

