package net.trpfrog.frogrobo.mini_tools;

import java.util.ArrayList;

import net.trpfrog.frogrobo.streaming.MentionListenerPlus;
import twitter4j.Status;

public class TransposeListener extends MentionListenerPlus {

	public static void main(String[] args){

		System.out.println("[テスト開始]\n");

		String id = "@FrogRobo";
		String cmd = "transpose";
		String bfr = "Bb";
		String aft = "C";

		System.out.println("移調前: in "+bfr);
		System.out.println("移調先: in "+aft+"\n");

		System.out.println("[移調する音]");

		String txt = id+" "+cmd+" "+bfr+" "+aft+"\n"
		+"ドレミファソラシド";

		TransposeListener tl = new TransposeListener();
		final String TRANSPOSED_CHORD_BEFORE = tl.replaceFlatAndSharpToSymbol(bfr);
		final String TRANSPOSED_CHORD_AFTER = tl.replaceFlatAndSharpToSymbol(aft);
		final String NOTES_TO_TRANSPOSE = txt
				.replaceAll(id+"( |\n)"+cmd+"( |\n)"+bfr+"( |\n)"+aft+"( |\n)", "");

		System.out.println(NOTES_TO_TRANSPOSE);

		byte numOfBefore = tl.toTransposionNumber(TRANSPOSED_CHORD_BEFORE);
		byte numOfAfter = tl.toTransposionNumber(TRANSPOSED_CHORD_AFTER);

		System.out.println("\n[結果]\n"+tl.transpose(NOTES_TO_TRANSPOSE,numOfBefore,numOfAfter));
	}

	@Override
	public String getCommandName() { return "Transpose"; }

	@Override
	public String getCommandUsage() { return this.getCommandLowerCase()+" 移調元キー 移調先キー"; }
	@Override
	public String getCommandDescription() {return "音名を移調してドレミで返します。";}
	@Override
	public String[] getCommandExceptionDescription() { return null; }

	@Override
	public void doWhenReceiveCommand(Status status, String[] commands) {
		final String TRANSPOSED_CHORD_BEFORE = replaceFlatAndSharpToSymbol(commands[2]);
		final String TRANSPOSED_CHORD_AFTER = replaceFlatAndSharpToSymbol(commands[3]);
		final String NOTES_TO_TRANSPOSE = status.getText()
				.replaceAll(commands[0]+"( |\n)"+commands[1]+"( |\n)"+commands[2]+"( |\n)"+commands[3]+"( |\n)", "");

		byte numOfBefore = toTransposionNumber(TRANSPOSED_CHORD_BEFORE);
		byte numOfAfter = toTransposionNumber(TRANSPOSED_CHORD_AFTER);

		StringBuilder sb = new StringBuilder();
		sb.append("\n[");
		sb.append(commands[2]);
		sb.append("→");
		sb.append(commands[3]);
		sb.append("への移調結果]\n");
		sb.append(transpose(NOTES_TO_TRANSPOSE,numOfBefore,numOfAfter));

		ToolsLoader.reply(sb.toString(),status,false);
	}

	private String replaceFlatAndSharpToSymbol(String note){
		return note
				.replace('♭', 'b')
				.replace('ｂ', 'b')
				.replaceAll("es","b")
				.replace('s', 'b')
				.replace('♯', '#')
				.replace('＃', '#')
				.replaceAll("is","#");
	}

	@Override
	public void whenMentioned(Status status) {
		// TODO 自動生成されたメソッド・スタブ

	}

	private final int NOT_NOTE_ERROR_NUMBER = 114514;

	private boolean checkHasNext(int size,int now){
		return now+1 < size;
	}

	private int generateNumberToTranspose(int before , int after){
		int diff = before - after;
//		if(before > after){
//			diff = before - after;
//		}else if(after > before){
//			diff = after - before;
//		}else if(before == after){
//			diff = 0;
//		}
		return diff;
	}

	private String transpose(String notes, int beforeTransposeKey, int afterTransposeKey){

		final int THE_NUMBER_TO_TRANSPOSE =
				generateNumberToTranspose(beforeTransposeKey, afterTransposeKey);

		System.out.println("\nずらす数: "+THE_NUMBER_TO_TRANSPOSE);

		ArrayList<Character> notesArray = new ArrayList<>();
		for(char c : replaceFlatAndSharpToSymbol(notes).toCharArray()){
			notesArray.add(c);
		}

		ArrayList<Integer> noteNumberArray = new ArrayList<>();

		for(int i=0; i<notesArray.size(); i++){

//			System.err.println("[NEW NOTE] i="+i);
//			System.out.println(notesArray.get(i));

			int num = searchNum(notesArray.get(i));

			num += THE_NUMBER_TO_TRANSPOSE; //移調

			if(checkHasNext(notesArray.size(),i)==false){
//				System.err.println("checkerr");
//				System.out.println("added:"+num);
				noteNumberArray.add(convertTo0to12(num));
				break;
			}

			int numberOfSharps = 0;
			int numberOfFlats = 0;
//			int plusNumberF = 0; //もし音がカタカナひらがなファなら1にする

			boolean noteIsF = ((notesArray.get(i)+"").equals("フ")||(notesArray.get(i)+"").equals("ふ")||(notesArray.get(i)+"").equals("ﾌ")) &&
					((notesArray.get(i+1)+"").equals("ァ")||(notesArray.get(i+1)+"").equals("ぁ")||(notesArray.get(i+1)+"").equals("ｧ"));

			if(noteIsF){
				i++; //飛ばす
				num = 5; //ファの音コード５に設定
				num += THE_NUMBER_TO_TRANSPOSE; //移調
//				plusNumberF = 1;
			}

			boolean hasSharpOrFlat = false;
			do{
				hasSharpOrFlat = false;
				if(checkHasNext(notesArray.size(),i)==false){
					break;
				}

				if(notesArray.get(i+1).equals('#')){
					System.out.println("It found a sharp.");
					numberOfSharps++;
					i++;
					hasSharpOrFlat = true;
				}

				if(checkHasNext(notesArray.size(),i)==false){
					break;
				}

				if(notesArray.get(i+1).equals('b')){
					System.out.println("It found a flat.");
					numberOfFlats++;
					i++;
					hasSharpOrFlat = true;
				}
			}while(hasSharpOrFlat==true);


			if(num == NOT_NOTE_ERROR_NUMBER){
				noteNumberArray.add(num);
				continue;
			}

			num += numberOfSharps; //シャープの数×半音(1) 上げる
			num -= numberOfFlats; //フラットの数×半音(1) 下げる




			System.out.println("added:"+convertTo0to12(num));
			noteNumberArray.add(convertTo0to12(num));

//			System.err.println("i="+i);
		}

		StringBuilder sb = new StringBuilder();

		noteNumberArray.stream().forEach(num -> {
			sb.append(convertToPitchName(num));
		});

		return sb.toString();
	}

	private int searchNum(char note){
		int number = 0;
		switch (note){
//			case 'C' :
//		case 'Ｃ' :
		case 'ド' :
		case 'ど':
			number = 0;
			break;

//		case 'D':
//		case 'Ｄ':
		case 'レ':
		case 'れ':
			number = 2;
			break;

//		case 'E':
//		case 'Ｅ':
		case 'ミ':
		case 'み':
			number = 4;
			break;

////		case 'F':
////		case 'Ｆ':
//			number = 5;
//			break;

//		case 'G':
//		case 'Ｇ':
		case 'ソ':
		case 'そ':
			number = 7;
			break;

//		case 'A':
//		case 'Ａ':
		case 'ラ':
		case 'ら':
			number = 9;
			break;

//		case 'B':
//		case 'Ｂ':
		case 'シ':
		case 'し':
			number = 11;
			break;

		default:
			number = NOT_NOTE_ERROR_NUMBER; //音名ではない場合
			break;

				}
		return number;
	}

	private int convertTo0to12(int num){
		while(true){ //ここで数字を0~12の範囲にする
			if(num >= 12){
				num = num - 12;
				continue;
			}else if(num < 0){
				num = num + 12;
				continue;
			}else{
				break;
			}
		}
		return num;
	}

	private String convertToPitchName(int number){
		switch(number){
		case 0:
			return "ド";
		case 1:
			return "ド♯";
		case 2:
			return "レ";
		case 3:
			return "レ♯";
		case 4:
			return "ミ";
		case 5:
			return "ﾌｧ";
		case 6:
			return "ﾌｧ♯";
		case 7:
			return "ソ";
		case 8:
			return "ソ♯";
		case 9:
			return "ラ";
		case 10:
			return "シ♭";
		case 11:
			return "シ";
		default:
			return "☆";
		}
	}

	private byte toTransposionNumber(String note){
		byte numOfSharpFlat = 0;
		switch (note){
			case "B#" :
			case "C" : //ド
				numOfSharpFlat = 0;
				break;
			case "C#": //ド＃
			case "Db": //レ♭
				numOfSharpFlat = 1;
				break;
			case "D" : //レ
				numOfSharpFlat = 2;
				break;
			case "D#" : //レ＃
			case "Eb" : //ミ♭
				numOfSharpFlat = 3;
				break;
			case "E" : //ミ
			case "Fb" :
				numOfSharpFlat = 4;
				break;
			case "E#" :
			case "F" : //ファ
				numOfSharpFlat = 5;
				break;
			case "F#" : //ファ＃
			case "Gb" : //ソ♭
				numOfSharpFlat = 6;
				break;
			case "G" : //ソ
				numOfSharpFlat = 7;
				break;
			case "G#" : //ソ＃
			case "Ab" : //ラ♭
				numOfSharpFlat = 8;
				break;
			case "A" : //ラ
				numOfSharpFlat = 9;
				break;
			case "Bb" : //シ♭
			case "A#" : //ラ＃
				numOfSharpFlat = 10;
				break;
			case "H" : //ハーーーーー
			case "B" : //シ
			case "Cb" :
				numOfSharpFlat = 11;
				break;
			default :
					break;
				}
		return numOfSharpFlat;
	}
}
