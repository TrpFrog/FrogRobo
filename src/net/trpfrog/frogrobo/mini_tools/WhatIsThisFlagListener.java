package net.trpfrog.frogrobo.mini_tools;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang3.text.StrBuilder;

import net.trpfrog.frogrobo.streaming.MentionListenerPlus;
import twitter4j.Status;

public class WhatIsThisFlagListener extends MentionListenerPlus{

	@Override
	public String getCommandName() { return "whatIsThisFlag"; }
	@Override
	public String getShortCommand() { return "flag"; }
	@Override
	public String getCommandUsage() { return this.getCommandLowerCase(); }
	@Override
	public String getCommandDescription() {return "これなんの旗？";}
	@Override
	public String[] getCommandExceptionDescription() { return null; }


	public static final byte FLAGS_AREA = 2;
	public static final int LENGTH_OF_FLAGS = 4;
	@Override
	public void doWhenReceiveCommand(Status status, String[] commands) {
		if(commands.length==2)
			return;
		ArrayList<String> flagsArray = new ArrayList<>();
		for (int i = 0; i < commands[FLAGS_AREA].length()/LENGTH_OF_FLAGS; i++) {
			flagsArray.add(commands[FLAGS_AREA]
					.substring(i*LENGTH_OF_FLAGS, i*LENGTH_OF_FLAGS + LENGTH_OF_FLAGS));
		}
		StrBuilder sb = new StrBuilder();
		sb.appendNewLine();
		sb.appendln("【結果】");

		flagsArray.stream().forEach(element -> {
			String name = FlagsTransrater.getFlagName(element);
			if(name == null){
				return;
			}
			sb.append(element);
			sb.append(": ");
			sb.appendln(name);
		});

		ToolsLoader.reply(sb.toString(), status, false);
	}

	@Override
	public void mention(Status status) {
	}
}

class FlagsTransrater {
	private static HashMap<String,String> flagsMap = putOnMap();

	public static String getFlagName(String key){
		return flagsMap.get(key);
	}

	private static HashMap<String,String> putOnMap(){
		HashMap<String,String> flagsMap = new HashMap<>();
		flagsMap.put("🇮🇸","アイスランド");
		flagsMap.put("🇮🇪","アイルランド");
		flagsMap.put("🇦🇿","アゼルバイジャン");
		flagsMap.put("🇦🇫","アフガニスタン");
		flagsMap.put("🇺🇸","アメリカ");
		flagsMap.put("🇦🇪","アラブ首長国連邦");
		flagsMap.put("🇩🇿","アルジェリア");
		flagsMap.put("🇦🇷","アルゼンチン");
		flagsMap.put("🇦🇼","アルバ");
		flagsMap.put("🇦🇱","アルバニア");
		flagsMap.put("🇦🇲","アルメニア");
		flagsMap.put("🇦🇮","アンギラ");
		flagsMap.put("🇦🇴","アンゴラ");
		flagsMap.put("🇦🇬","アンティグア・バーブーダ");
		flagsMap.put("🇦🇩","アンドラ");
		flagsMap.put("🇾🇪","イエメン");
		flagsMap.put("🇬🇧","イギリス");
		flagsMap.put("🇮🇱","イスラエル");
		flagsMap.put("🇮🇹","イタリア");
		flagsMap.put("🇮🇶","イラク");
		flagsMap.put("🇮🇷","イラン");
		flagsMap.put("🇮🇳","インド");
		flagsMap.put("🇮🇩","インドネシア");
		flagsMap.put("🇼🇫","ウォリス・フツナ");
		flagsMap.put("🇺🇬","ウガンダ");
		flagsMap.put("🇺🇦","ウクライナ");
		flagsMap.put("🇺🇿","ウズベキスタン");
		flagsMap.put("🇺🇾","ウルグアイ");
		flagsMap.put("🇪🇨","エクアドル");
		flagsMap.put("🇪🇬","エジプト");
		flagsMap.put("🇪🇪","エストニア");
		flagsMap.put("🇪🇹","エチオピア");
		flagsMap.put("🇪🇷","エリトリア");
		flagsMap.put("🇸🇻","エルサルバドル");
		flagsMap.put("🇦🇺","オーストラリア");
		flagsMap.put("🇦🇹","オーストリア");
		flagsMap.put("🇦🇽","オーランド諸島");
		flagsMap.put("🇴🇲","オマーン");
		flagsMap.put("🇳🇱","オランダ");
		flagsMap.put("🇧🇶","カリブ・オランダ(BES諸島)");
		flagsMap.put("🇬🇭","ガーナ");
		flagsMap.put("🇨🇻","カーボベルデ");
		flagsMap.put("🇬🇬","ガーンジー");
		flagsMap.put("🇬🇾","ガイアナ");
		flagsMap.put("🇰🇿","カザフスタン");
		flagsMap.put("🇶🇦","カタール");
		flagsMap.put("🇨🇦","カナダ");
		flagsMap.put("🇮🇨","カナリア諸島");
		flagsMap.put("🇬🇦","ガボン");
		flagsMap.put("🇨🇲","カメルーン");
		flagsMap.put("🇬🇲","ガンビア");
		flagsMap.put("🇰🇭","カンボジア");
		flagsMap.put("🇬🇳","ギニア");
		flagsMap.put("🇬🇼","ギニアビサウ");
		flagsMap.put("🇨🇾","キプロス");
		flagsMap.put("🇨🇺","キューバ");
		flagsMap.put("🇨🇼","キュラソー島");
		flagsMap.put("🇬🇷","ギリシャ");
		flagsMap.put("🇰🇮","キリバス");
		flagsMap.put("🇰🇬","キルギス");
		flagsMap.put("🇬🇹","グアテマラ");
		flagsMap.put("🇬🇵","グアドループ");
		flagsMap.put("🇬🇺","グアム");
		flagsMap.put("🇰🇼","クウェート");
		flagsMap.put("🇨🇰","クック諸島");
		flagsMap.put("🇬🇱","グリーンランド");
		flagsMap.put("🇨🇽","クリスマス島");
		flagsMap.put("🇬🇩","グレナダ");
		flagsMap.put("🇭🇷","クロアチア");
		flagsMap.put("🇰🇾","ケイマン諸島");
		flagsMap.put("🇰🇪","ケニア");
		flagsMap.put("🇨🇮","コートジボワール");
		flagsMap.put("🇨🇨","ココス諸島");
		flagsMap.put("🇨🇷","コスタリカ");
		flagsMap.put("🇽🇰","コソボ共和国");
		flagsMap.put("🇰🇲","コモロ");
		flagsMap.put("🇨🇴","コロンビア");
		flagsMap.put("🇨🇬","コンゴ共和国");
		flagsMap.put("🇨🇩","コンゴ民主共和国");
		flagsMap.put("🇸🇦","サウジアラビア");
		flagsMap.put("🇼🇸","サモア");
		flagsMap.put("🇧🇱","サン・バルテルミー島");
		flagsMap.put("🇸🇹","サントメ・プリンシペ");
		flagsMap.put("🇿🇲","ザンビア");
		flagsMap.put("🇵🇲","サンピエール島・ミクロン島");
		flagsMap.put("🇸🇲","サンマリノ");
		flagsMap.put("🇸🇱","シエラレオネ");
		flagsMap.put("🇩🇯","ジブチ");
		flagsMap.put("🇬🇮","ジブラルタル");
		flagsMap.put("🇯🇪","ジャージー島");
		flagsMap.put("🇯🇲","ジャマイカ");
		flagsMap.put("🇬🇪","ジョージア");
		flagsMap.put("🇸🇾","シリア");
		flagsMap.put("🇸🇬","シンガポール");
		flagsMap.put("🇸🇽","シント・マールテン");
		flagsMap.put("🇿🇼","ジンバブエ");
		flagsMap.put("🇨🇭","スイス");
		flagsMap.put("🇸🇪","スウェーデン");
		flagsMap.put("🇸🇩","スーダン");
		flagsMap.put("🇪🇸","スペイン");
		flagsMap.put("🇸🇷","スリナム");
		flagsMap.put("🇱🇰","スリランカ");
		flagsMap.put("🇸🇰","スロバキア");
		flagsMap.put("🇸🇮","スロベニア");
		flagsMap.put("🇸🇿","スワジランド");
		flagsMap.put("🇸🇨","セーシェル");
		flagsMap.put("🇸🇳","セネガル");
		flagsMap.put("🇷🇸","セルビア");
		flagsMap.put("🇰🇳","セントクリストファー・ネビス");
		flagsMap.put("🇻🇨","セントビンセント・グレナディーン");
		flagsMap.put("🇸🇭","セントヘレナ");
		flagsMap.put("🇱🇨","セントルシア");
		flagsMap.put("🇸🇴","ソマリア");
		flagsMap.put("🇸🇧","ソロモン諸島");
		flagsMap.put("🇹🇨","タークス・カイコス諸島");
		flagsMap.put("🇹🇭","タイ");
		flagsMap.put("🇹🇯","タジキスタン");
		flagsMap.put("🇹🇿","タンザニア");
		flagsMap.put("🇨🇿","チェコ");
		flagsMap.put("🇹🇩","チャド");
		flagsMap.put("🇹🇳","チュニジア");
		flagsMap.put("🇨🇱","チリ");
		flagsMap.put("🇹🇻","ツバル");
		flagsMap.put("🇩🇰","デンマーク");
		flagsMap.put("🇩🇪","ドイツ");
		flagsMap.put("🇹🇬","トーゴ");
		flagsMap.put("🇹🇰","トケラウ諸島");
		flagsMap.put("🇩🇴","ドミニカ共和国");
		flagsMap.put("🇩🇲","ドミニカ");
		flagsMap.put("🇹🇹","トリニダード・トバゴ");
		flagsMap.put("🇹🇲","トルクメニスタン");
		flagsMap.put("🇹🇷","トルコ");
		flagsMap.put("🇹🇴","トンガ");
		flagsMap.put("🇳🇬","ナイジェリア");
		flagsMap.put("🇳🇷","ナウル");
		flagsMap.put("🇳🇦","ナミビア");
		flagsMap.put("🇳🇺","ニウエ");
		flagsMap.put("🇳🇮","ニカラグア");
		flagsMap.put("🇳🇪","ニジェール");
		flagsMap.put("🇳🇨","ニューカレドニア");
		flagsMap.put("🇳🇿","ニュージーランド");
		flagsMap.put("🇳🇵","ネパール");
		flagsMap.put("🇳🇫","ノーフォーク島");
		flagsMap.put("🇳🇴","ノルウェー");
		flagsMap.put("🇧🇭","ノーフォーク島");
		flagsMap.put("🇭🇹","ハイチ");
		flagsMap.put("🇵🇰","パキスタン");
		flagsMap.put("🇻🇦","バチカン市国");
		flagsMap.put("🇵🇦","パナマ");
		flagsMap.put("🇻🇺","バヌアツ");
		flagsMap.put("🇧🇸","バハマ");
		flagsMap.put("🇵🇬","パプアニューギニア");
		flagsMap.put("🇧🇲","バミューダ諸島");
		flagsMap.put("🇵🇼","パラオ");
		flagsMap.put("🇵🇾","パラグアイ");
		flagsMap.put("🇧🇧","バルバドス");
		flagsMap.put("🇵🇸","パレスチナ");
		flagsMap.put("🇭🇺","ハンガリー");
		flagsMap.put("🇧🇩","バングラデシュ");
		flagsMap.put("🇵🇳","ピトケアン");
		flagsMap.put("🇫🇯","フィジー");
		flagsMap.put("🇵🇭","フィリピン");
		flagsMap.put("🇫🇮","フィンランド");
		flagsMap.put("🇧🇹","ブータン");
		flagsMap.put("🇵🇷","プエルトリコ");
		flagsMap.put("🇫🇴","フェロー諸島");
		flagsMap.put("🇫🇰","フォークランド諸島");
		flagsMap.put("🇧🇷","ブラジル");
		flagsMap.put("🇫🇷","フランス");
		flagsMap.put("🇧🇬","ブルガリア");
		flagsMap.put("🇧🇫","ブルキナファソ");
		flagsMap.put("🇧🇳","ブルネイ・ダルサラーム");
		flagsMap.put("🇧🇮","ブルンジ");
		flagsMap.put("🇻🇳","ベトナム");
		flagsMap.put("🇧🇯","ベナン");
		flagsMap.put("🇻🇪","ベネズエラ");
		flagsMap.put("🇧🇾","ベラルーシ");
		flagsMap.put("🇧🇿","ベリーズ");
		flagsMap.put("🇵🇪","ペルー");
		flagsMap.put("🇧🇪","ベルギー");
		flagsMap.put("🇵🇱","ポーランド");
		flagsMap.put("🇧🇦","ボスニア・ヘルツェゴヴィナ");
		flagsMap.put("🇧🇼","ボツワナ");
		flagsMap.put("🇧🇴","ボリビア");
		flagsMap.put("🇵🇹","ポルトガル");
		flagsMap.put("🇭🇳","ホンジュラス");
		flagsMap.put("🇲🇭","マーシャル諸島");
		flagsMap.put("🇲🇰","マケドニア");
		flagsMap.put("🇲🇬","マダガスカル");
		flagsMap.put("🇾🇹","マヨット島");
		flagsMap.put("🇲🇼","マラウイ");
		flagsMap.put("🇲🇱","マリ");
		flagsMap.put("🇲🇹","マルタ");
		flagsMap.put("🇲🇶","マルティニク");
		flagsMap.put("🇲🇾","マレーシア");
		flagsMap.put("🇮🇲","マン島");
		flagsMap.put("🇫🇲","ミクロネシア");
		flagsMap.put("🇲🇲","ミャンマー");
		flagsMap.put("🇲🇽","メキシコ");
		flagsMap.put("🇲🇺","モーリシャス");
		flagsMap.put("🇲🇷","モーリタニア");
		flagsMap.put("🇲🇿","モザンビーク");
		flagsMap.put("🇲🇨","モナコ");
		flagsMap.put("🇲🇻","モルディブ");
		flagsMap.put("🇲🇩","モルドバ");
		flagsMap.put("🇲🇦","モロッコ");
		flagsMap.put("🇲🇳","モンゴル");
		flagsMap.put("🇲🇪","モンテネグロ");
		flagsMap.put("🇲🇸","モントセラト");
		flagsMap.put("🇯🇴","ヨルダン");
		flagsMap.put("🇱🇦","ラオス");
		flagsMap.put("🇱🇻","ラトビア");
		flagsMap.put("🇱🇹","リトアニア");
		flagsMap.put("🇱🇾","リビア");
		flagsMap.put("🇱🇮","リヒテンシュタイン");
		flagsMap.put("🇱🇷","リベリア");
		flagsMap.put("🇷🇴","ルーマニア");
		flagsMap.put("🇱🇺","ルクセンブルク");
		flagsMap.put("🇷🇼","ルワンダ");
		flagsMap.put("🇱🇸","レソト");
		flagsMap.put("🇱🇧","レバノン");
		flagsMap.put("🇷🇪","レユニオン");
		flagsMap.put("🇷🇺","ロシア");
		flagsMap.put("🇮🇴","イギリス領インド洋地域");
		flagsMap.put("🇻🇬","イギリス領バージン諸島");
		flagsMap.put("🇪🇺","欧州連合(EU)");
		flagsMap.put("🇪🇭","西サハラ");
		flagsMap.put("🇬🇶","赤道ギニア");
		flagsMap.put("🇹🇼","台湾");
		flagsMap.put("🇰🇷","韓国");
		flagsMap.put("🇨🇫","中央アフリカ");
		flagsMap.put("🇲🇴","マカオ");
		flagsMap.put("🇭🇰","香港");
		flagsMap.put("🇨🇳","中華人民共和国");
		flagsMap.put("🇰🇵","北朝鮮");
		flagsMap.put("🇹🇱","東ティモール");
		flagsMap.put("🇿🇦","南アフリカ共和国");
		flagsMap.put("🇬🇸","サウスジョージア・サウスサンドウィッチ諸島");
		flagsMap.put("🇸🇸","南スーダン");
		flagsMap.put("🇦🇶","南極大陸");
		flagsMap.put("🇯🇵","日本");
		flagsMap.put("🇬🇫","フランス領ギアナ");
		flagsMap.put("🇵🇫","フランス領ポリネシア");
		flagsMap.put("🇹🇫","フランス領南方・南極地域");
		flagsMap.put("🇻🇮","アメリカ領バージン諸島");
		flagsMap.put("🇦🇸","アメリカ領サモア");
		flagsMap.put("🇲🇵","北マリアナ諸島");

		return flagsMap;
	}
}
