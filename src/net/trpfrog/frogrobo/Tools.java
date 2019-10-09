package net.trpfrog.frogrobo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

import org.apache.commons.lang3.SystemUtils;

public class Tools {
	public static Stream<String> getTextLineStream (String... path) throws IOException{
		final String FS = File.separator;
		StringBuilder filePath = new StringBuilder();
		Arrays.stream(path).forEach(e->filePath.append(e+FS));

		return Files.lines(Paths.get(filePath.toString().replaceFirst(FS+"$", "")));
	}

	public static void writeAllNewText (String path, String... text) throws IOException{
		File file = new File(path);
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
		for (String string : text) {
			pw.println(string);
		}
		pw.close();

	}

	public static String generatePath (String... path){
		final String FS = File.separator;
		StringBuilder filePath = new StringBuilder();
		Arrays.stream(path).forEach(e->filePath.append(e+FS));
		return filePath.toString();
	}
}
