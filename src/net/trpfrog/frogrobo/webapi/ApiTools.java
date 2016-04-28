package net.trpfrog.frogrobo.webapi;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.SystemUtils;

import net.trpfrog.frogrobo.main.FrogRobo;

public class ApiTools {

	private String apiKeyFileName = null;

	public ApiTools(String apiKeyFileName){
		this.apiKeyFileName = apiKeyFileName;
	}

	public synchronized String urlEncode(String text) {
		String encodeStr = null;
		try {
			encodeStr = URLEncoder.encode(text, "UTF-8");
			encodeStr = encodeStr.replace("*", "%2a").replace("-", "%2d").replace("+", "%20");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encodeStr;


	}

	public String getAPIKey(){
		List<String> key = new ArrayList<>();
		final String FS = SystemUtils.FILE_SEPARATOR;
		StringBuilder path = new StringBuilder();
		path.append(FrogRobo.FILE_PASS);
		path.append("SecretFiles");
		path.append(FS);
		path.append(this.apiKeyFileName);
		try {
			key = Files.lines(Paths.get(path.toString())).collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return key.get(0);
	}


}
