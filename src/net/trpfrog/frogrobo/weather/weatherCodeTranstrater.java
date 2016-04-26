package net.trpfrog.frogrobo.weather;

import java.util.HashMap;

public class weatherCodeTranstrater {
	private static HashMap<Integer,String> codeMap = putOnTheMap();

	public static synchronized String getValue(int key){
		String value = codeMap.get(key);
		if(value == null)
			return "null";
		return value;
	}

	private static HashMap<Integer,String> putOnTheMap(){
		HashMap<Integer,String> codeMap = new HashMap<>();
		codeMap.put(200,"雷雨(小雨)");
		codeMap.put(201,"雷雨(雨)");
		codeMap.put(202,"雷雨(大雨)");
		codeMap.put(210,"弱い雷雨");
		codeMap.put(211,"雷雨");
		codeMap.put(212,"激しい雷雨");
		codeMap.put(221,"不規則な雷雨");
		codeMap.put(230,"雷雨(薄い霧が出る)");
		codeMap.put(231,"雷雨(霧が出る)");
		codeMap.put(232,"雷雨(濃い霧が出る)");
		codeMap.put(300,"薄い霧");
		codeMap.put(301,"霧");
		codeMap.put(302,"濃い霧");
		codeMap.put(310,"軽い霧雨");
		codeMap.put(311,"霧雨");
		codeMap.put(312,"濃い霧雨");
		codeMap.put(313,"にわか雨と霧");
		codeMap.put(314,"激しいにわか雨と霧");
		codeMap.put(321,"にわか霧");
		codeMap.put(500,"小雨");
		codeMap.put(501,"普通の雨");
		codeMap.put(502,"激しい雨");
		codeMap.put(503,"とても激しい雨");
		codeMap.put(504,"エクストリーム雨");
		codeMap.put(511,"冷たい雨");
		codeMap.put(520,"軽いにわか雨");
		codeMap.put(521,"にわか雨");
		codeMap.put(522,"激しいにわか雨");
		codeMap.put(531,"不規則なにわか雨");
		codeMap.put(600,"小雪");
		codeMap.put(601,"雪");
		codeMap.put(602,"大雪");
		codeMap.put(611,"みぞれ");
		codeMap.put(612,"にわかみぞれ");
		codeMap.put(615,"小雨ときどき雪");
		codeMap.put(616,"雨ときどき雪");
		codeMap.put(620,"にわか小雪");
		codeMap.put(621,"にわか雪");
		codeMap.put(622,"激しいにわか雪");
		codeMap.put(710,"ミスト");
		codeMap.put(711,"煙たい");
		codeMap.put(721,"霧");
		codeMap.put(731,"砂塵旋風, 黄砂");
		codeMap.put(741,"濃霧");
		codeMap.put(751,"黄砂");
		codeMap.put(761,"塵");
		codeMap.put(762,"火山灰");
		codeMap.put(771,"スコール");
		codeMap.put(781,"竜巻");
		codeMap.put(800,"快晴");
		codeMap.put(801,"ほとんど雲なし");
		codeMap.put(802,"ちぎれ雲");
		codeMap.put(803,"ちぎれ雲");
		codeMap.put(804,"くもり");
		codeMap.put(900,"竜巻");
		codeMap.put(901,"熱帯暴風雨");
		codeMap.put(902,"ハリケーン");
		codeMap.put(903,"寒い");
		codeMap.put(904,"暑い");
		codeMap.put(905,"風が強い");
		codeMap.put(906,"ひょう");
		codeMap.put(951,"風がなくおだやか");
		codeMap.put(952,"風力２");
		codeMap.put(953,"風力３");
		codeMap.put(954,"風力４");
		codeMap.put(955,"風力５");
		codeMap.put(956,"風力６");
		codeMap.put(957,"風力７");
		codeMap.put(958,"風力８");
		codeMap.put(959,"風力９");
		codeMap.put(960,"風力10");
		codeMap.put(961,"風力11");
		codeMap.put(962,"風力12");
		return codeMap;
	}
}
