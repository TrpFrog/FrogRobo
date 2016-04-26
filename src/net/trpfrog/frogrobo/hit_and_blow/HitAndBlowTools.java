package net.trpfrog.frogrobo.hit_and_blow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class HitAndBlowTools {
	public int generateNumber(int length){
		String generatedNumber = "";

		ArrayList<Integer> numbers = new ArrayList<>();
		for (int i = 1; i < 10; i++) {
			numbers.add(i);
		}
		Collections.shuffle(numbers,new Random(System.currentTimeMillis()));
		generatedNumber += numbers.get(0);
		numbers.remove(0);

		numbers.add(0);
		Collections.shuffle(numbers,new Random(System.currentTimeMillis()));

		if(length > 5){
			length = 3;
		}

		for (int i = 1; i < length; i++) {
			generatedNumber += numbers.get(0);
			numbers.remove(0);
		}

		System.out.println(generatedNumber);

		return Integer.parseInt(generatedNumber);
	}

	public HitAndBlowResult checkHitAndBlow(int solve, int userAns){

		int hit = 0;
		int blow = 0;

		int length = (solve+"").length();
		if(length != (userAns+"").length()){
			throw new IllegalArgumentException("桁数が違うぽに");
		}

		char[] solveArray = (solve+"").toCharArray();
		char[] userAnsArray = (userAns+"").toCharArray();

		for (int i = 0; i < userAnsArray.length; i++) {
			for (int j = 0; j < solveArray.length; j++) {
				boolean isBlow = userAnsArray[i]==solveArray[j];
				boolean isHit = isBlow && i==j;
				if(isHit){
					hit++;
					break;
				}else if(isBlow){
					blow++;
					break;
				}
			}
		}

		return new HitAndBlowResult(hit,blow);
	}

	public boolean isAnswer(String userAns,int solve){
		if (userAns.matches("^[0-9]+$")==false) {
			return false;
		}
		int ansNum = Integer.parseInt(userAns);
		int length = (solve+"").length();
		if(length != (ansNum+"").length()){
			return false;
		}
		return true;
	}

	public boolean isRepeated(int number){
		char[] numArray = (number+"").toCharArray();
		for (int i = 0; i < numArray.length; i++) {
			for (int j = 0; j < numArray.length; j++) {
				if(i==j)
					continue;
				if(numArray[i] == numArray[j])
					return true;
			}
		}
		return false;
	}
}
