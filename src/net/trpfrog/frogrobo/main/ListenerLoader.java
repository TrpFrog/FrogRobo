package net.trpfrog.frogrobo.main;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.SystemUtils;

import net.trpfrog.frogrobo.streaming.MentionListener;
import net.trpfrog.frogrobo.streaming.TweetStream;

public class ListenerLoader {

	public static void load() {
		final String FS = SystemUtils.FILE_SEPARATOR;
		StringBuilder path = new StringBuilder();
		path.append(FrogRobo.FILE_PASS);
		path.append("Files");
		path.append(FS);
		path.append("ListenerLoader.txt");

		List<String> newListenerFQCN = new ArrayList<>();
		try (Stream<String> listenerFQCN = Files.lines(Paths.get(path.toString()));){
			listenerFQCN.forEach(listenerClassName -> {
				try {
					Class<?> listener = Class.forName(listenerClassName);

					if(listener.getSuperclass().getInterfaces()[0]==MentionListener.class||
					   listener.getSuperclass()==MentionListener.class){
						try {
							TweetStream.getInstance().addMentionListener((MentionListener)listener.newInstance());
							newListenerFQCN.add(listener.getName());
						} catch (Exception e) {
							//そういうこともあるさ
						}
					}else{
						newListenerFQCN.add("");
					}
				} catch (ClassNotFoundException e) {
					newListenerFQCN.add("");
				}
			});

			final String BR = SystemUtils.LINE_SEPARATOR;

			try(Writer fw = new FileWriter(path.toString())){

				newListenerFQCN.stream()
				.filter(fqcn -> fqcn.equals("")==false)
				.forEach(fqcn ->{
					try {
						fw.write(fqcn+BR);
					} catch (Exception e) {
						e.printStackTrace();
					}
				});

			}

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}

