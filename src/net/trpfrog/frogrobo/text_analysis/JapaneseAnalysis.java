package net.trpfrog.frogrobo.text_analysis;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.SystemUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import net.trpfrog.frogrobo.FrogRobo;

public class JapaneseAnalysis {

	private static final String APPLICATION_ID = getApplicationKey();

	private static final int WORD_LIST = 2;

	public static final int SURFACE = 0;
	public static final int READING = 1;
	public static final int POS     = 2;

	public static ArrayList<String[]> morphological(String text) throws APINetworkErrorException {

		final String URL = generateRequestURL(text);

		ArrayList<String[]> words = new ArrayList<>();

		Document doc = null;
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(URL);
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		}
		Node errorNode = doc.getDocumentElement();

		if(errorNode.getNodeName()=="Error"){
			errorNode = errorNode.getFirstChild().getFirstChild();
			String errorMsg = errorNode.getNodeValue();
			throw new APINetworkErrorException(errorMsg);
		}


		Node wordsNode =
				doc.getDocumentElement().getFirstChild().getChildNodes().item(WORD_LIST).getFirstChild();
		do{
			String[] wordsArray = new String[3];
			wordsArray[0] = wordsNode.getChildNodes().item(SURFACE).getFirstChild().getNodeValue();
			wordsArray[1] = wordsNode.getChildNodes().item(READING).getFirstChild().getNodeValue();
			wordsArray[2] = posShorter(wordsNode.getChildNodes().item(POS).getFirstChild().getNodeValue());
			words.add(wordsArray);
			if(wordsNode.getNextSibling() == null) break;
		}while((wordsNode = wordsNode.getNextSibling()).getChildNodes().item(0) != null);

		return words;
	}

	private static String getApplicationKey(){
		List<String> key = new ArrayList<>();
		final String FS = SystemUtils.FILE_SEPARATOR;
		StringBuilder path = new StringBuilder();
		path.append(FrogRobo.FILE_PASS);
		path.append("SecretFiles");
		path.append(FS);
		path.append("YahooApplicationKey.txt");
		try {
			key = Files.lines(Paths.get(path.toString())).collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return key.get(0);
	}

	private static String generateRequestURL(String text) {
		StringBuilder url = new StringBuilder();
		url.append("http://jlp.yahooapis.jp/MAService/V1/parse?appid=");
		url.append(APPLICATION_ID);
		url.append("&sentence=");
		url.append(urlEncode(text));
		return url.toString();
	}

	private static String posShorter(String pos){
		if(pos==null){
			throw new IllegalArgumentException("ぬるぽ");
		} else switch (pos) {
		case "動詞":
		case "形容詞":
		case "名詞":
		case "連体詞":
		case "副詞":
		case "接続詞":
		case "感動詞":
		case "助詞":
		case "特殊":
			Character c = pos.toCharArray()[0];
			return c.toString();
		case "形容動詞":
		case "助動詞":
			Character c1 = pos.toCharArray()[0];
			Character c2 = pos.toCharArray()[1];
			return c1.toString()+c2.toString();
		case "接尾辞":
		case "接頭辞":
			Character c3 = pos.toCharArray()[1];
			return c3.toString();

		default:
			break;
		}
		return null;
	}

	private static synchronized String urlEncode(String text) {
		String encodeStr = null;
		try {
			encodeStr = URLEncoder.encode(text, "UTF-8");
			encodeStr = encodeStr.replace("*", "%2a").replace("-", "%2d").replace("+", "%20");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encodeStr;
	}
}
class APINetworkErrorException extends Exception{
	public APINetworkErrorException(String str){
		super(str);
	}
}