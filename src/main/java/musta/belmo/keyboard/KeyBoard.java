package musta.belmo.keyboard;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class KeyBoard {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String keyboard = sc.next();
		int numberOfTestCases = sc.nextInt();
		Map<Character, Integer> mapOfChars = createMap(keyboard);
		for (int i = 0; i < numberOfTestCases; i++) {
			String currentPhrase = sc.next();
			System.out.println(computeSumOfDistances(currentPhrase, mapOfChars));
		}
	}
	
	private static Map<Character, Integer> createMap(String keyboard) {
		Map<Character, Integer> map = new LinkedHashMap<>();
		for (int i = 0; i < keyboard.length(); i++) {
			map.put(keyboard.charAt(i), i);
		}
		return map;
	}
	
	private static int computeSumOfDistances(String currentPhrase, Map<Character, Integer> keyboardMap) {
		
		int sum= keyboardMap.get(currentPhrase.charAt(0));
		for (int i = 0; i < currentPhrase.length() - 1; i++) {
			final char char1 = currentPhrase.charAt(i);
			final char char2 = currentPhrase.charAt(i + 1);
			
			sum += Math.abs(keyboardMap.get(char1) - keyboardMap.get(char2));
			
		}
		return sum;
	}
}
