package net.trpfrog.frogrobo.weather;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import net.trpfrog.frogrobo.webapi.ApiTools;

public class WeatherApi {

	private static ApiTools weather_api = new ApiTools("OpenWeatherMapAPIkey.txt");
	private static final String APPLICATION_ID = weather_api.getAPIKey();

	public static final int SURFACE = 0;
	public static final int READING = 1;
	public static final int POS     = 2;

	private static final int LOCATION = 0;
	private static final int SUN = 3;
	private static final int FORECAST = 4;

	private static final int NAME = 0;

	private static final int SUNRISE = 0;
	private static final int SUNSET = 1;

	private int daysLater = 0;

	private Document doc = null;

	public WeatherApi(String location, int daysLater) throws APINetworkErrorException{
		this.daysLater = daysLater;
		try {
			this.doc = documentBuilder(generateRequestURL(location));
			doc.getDocumentElement()
			.getChildNodes().item(LOCATION)
			.getChildNodes().item(NAME)
			.getFirstChild().getTextContent();
		} catch (DOMException|NullPointerException e) {
			throw new APINetworkErrorException("クソみたいな文字列投げるな！サルかよ！");
		}
	}

	public WeatherApi(double lat,double lon,int daysLater) throws APINetworkErrorException{
		this.daysLater = daysLater;
		try {
			this.doc = documentBuilder(generateRequestURL(lat,lon));
			doc.getDocumentElement()
			.getChildNodes().item(LOCATION)
			.getChildNodes().item(NAME)
			.getFirstChild().getTextContent();
		} catch (DOMException|NullPointerException e) {
			throw new APINetworkErrorException("クソみたいな文字列投げるな！サルかよ！");
		}
	}

	public static void main(String[] args) throws APINetworkErrorException {
		WeatherApi wp = null;
		try {
			wp = new WeatherApi("Tokyo",0);
			System.out.println(wp.getLocationName());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		System.out.println(wp.getEveningTemperature());
		System.out.println(wp.getCountryCode());

	}

	private static String generateRequestURL(String location) {
		StringBuilder url = new StringBuilder();
		url.append("http://api.openweathermap.org/data/2.5/forecast/daily?q=");
		url.append(location);
		url.append("&mode=xml&APPID=");
		url.append(getApiKey());
		return url.toString();
	}

	private static String generateRequestURL(double lat, double lon) {
		StringBuilder url = new StringBuilder();
		url.append("http://api.openweathermap.org/data/2.5/forecast/daily?");
		url.append("lat=");
		url.append(lat);
		url.append("&lon=");
		url.append(lat);
		url.append("&mode=xml&APPID=");
		url.append(getApiKey());
		return url.toString();
	}

	private static synchronized String getApiKey(){
		return APPLICATION_ID;
	}

	private static Document documentBuilder(String url){
		Document doc = null;
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(url);
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		}
		Node errorNode = doc.getDocumentElement();

		if(errorNode.getNodeName()=="Error"){
			errorNode = errorNode.getFirstChild().getFirstChild();
			String errorMsg = errorNode.getNodeValue();
			System.err.println(errorMsg);
		}

		return doc;
	}

	public String getLocationName(){
		return this.doc.getDocumentElement()
		.getChildNodes().item(LOCATION)
		.getChildNodes().item(NAME)
		.getFirstChild().getTextContent();
	}

	public String getCountryCode(){
		return this.doc.getDocumentElement()
		.getChildNodes().item(LOCATION)
		.getChildNodes().item(2) //country
		.getFirstChild().getTextContent();
	}

	private String changeTimeZoneUTCtoJST(String utctime){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date utcDate = null;
		try {
			utcDate = sdf.parse(utctime);
		} catch (ParseException e) {
			throw new IllegalArgumentException("日付じゃねええええ");
		}
		sdf = new SimpleDateFormat("HH時mm分");
		sdf.setTimeZone(TimeZone.getTimeZone("JST"));
		return sdf.format(utcDate);
	}

	public String getSunrise(){
		String sunriseTimeUTC = doc.getDocumentElement()
				.getChildNodes().item(SUN)
				.getAttributes().item(SUNRISE)
				.getTextContent();
		return changeTimeZoneUTCtoJST(sunriseTimeUTC);
	}

	public String getSunset(){
		String sunsetTimeUTC = doc.getDocumentElement()
				.getChildNodes().item(SUN)
				.getAttributes().item(SUNSET)
				.getTextContent();
		return changeTimeZoneUTCtoJST(sunsetTimeUTC);
	}

	public String getWeather(){
		String number = getForecastNode().item(0) //Symbol
				.getAttributes().getNamedItem("number").getTextContent();
		return weatherCodeTranstrater.getValue(Integer.parseInt(number));
	}

	private NodeList getForecastNode(){
		return  doc.getDocumentElement()
				.getChildNodes().item(FORECAST)
				.getChildNodes().item(this.daysLater).getChildNodes();
	}

	private NamedNodeMap getTemperatureNode(){
		return  doc.getDocumentElement()
				.getChildNodes().item(FORECAST)
				.getChildNodes().item(this.daysLater)
				.getChildNodes().item(4) //Temperature
				.getAttributes();
	}

	public double getMaxTemperature(){
		String t = getTemperatureNode().getNamedItem("max").getTextContent();
		return round(Double.parseDouble(t));
	}

	public double getMinTemperature(){
		String t = getTemperatureNode().getNamedItem("min").getTextContent();
		return round(Double.parseDouble(t));
	}

	public double getDayTemperature(){
		String t;
		try{
			t = getTemperatureNode().getNamedItem("day").getTextContent();
		}catch(NullPointerException e){
			return 114514;
		}
		return round(Double.parseDouble(t));
	}

	public double getNightTemperature(){
		String t;
		try{
			t = getTemperatureNode().getNamedItem("night").getTextContent();
		}catch(NullPointerException e){
			return 114514;
		}
		return round(Double.parseDouble(t));
	}

	public double getEveningTemperature(){
		String t;
		try{
			t = getTemperatureNode().getNamedItem("eve").getTextContent();
		}catch(NullPointerException e){
			return 114514;
		}
		return round(Double.parseDouble(t));
	}

	public double getMorningTemperature(){
		String t;
		try{
			t = getTemperatureNode().getNamedItem("morn").getTextContent();
		}catch(NullPointerException e){
			return 114514;
		}
		return round(Double.parseDouble(t));
	}

	private double round(double tempreture){
		tempreture -= 273.15; //ケルビン→摂氏
		tempreture *= 10; //10倍
		tempreture = Math.round(tempreture); //小数点以下四捨五入
		tempreture /= 10; //10分の1
		return tempreture;
	}

	public double getPressure(){
		String t = getForecastNode().item(5).getAttributes().getNamedItem("value").getTextContent();
		return Double.parseDouble(t);
	}

	public String getDate(){
		return  doc.getDocumentElement()
		.getChildNodes().item(FORECAST)
		.getChildNodes().item(this.daysLater).getAttributes().getNamedItem("day").getTextContent();
	}
}

class APINetworkErrorException extends Exception{
	public APINetworkErrorException(String str){
		super(str);
	}
}