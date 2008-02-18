package org.flexpay.common.util;

import java.util.HashMap;
import java.util.Map;

public class Luhn {
	
	private static Map<Integer, Integer> digitTransformMap;
	
	static {
		digitTransformMap = new HashMap<Integer, Integer>();
		// key - result digit; value - source digit
		digitTransformMap.put(0, 0);
		digitTransformMap.put(1, 5);
		digitTransformMap.put(2, 1);
		digitTransformMap.put(3, 6);
		digitTransformMap.put(4, 2);
		digitTransformMap.put(5, 7);
		digitTransformMap.put(6, 3);
		digitTransformMap.put(7, 8);
		digitTransformMap.put(8, 4);
		digitTransformMap.put(9, 9);
		
		
	}

	public static boolean isValidNumber(String number) {
		return getControlDigit(number, true) == 0;
	}

	public static String insertControlDigit(String number, int ind) {
		if(ind > number.length()) {
			throw new IllegalArgumentException("ind value must be equal or less then number lenth. number=" + number + "; ind=" + ind);
		}
		
		int delimeterInd = number.length() - ind;
		String leftPart = number.substring(0, delimeterInd);
		String rightPart = number.substring(delimeterInd);
		
		return leftPart + getControlDigit(number, ind % 2 == 0) + rightPart;
	}
	
	private static int getChecksum(String number) {
		int sum = 0;
		boolean alternate = false;
		for (int i = number.length() - 1; i >= 0; i--) {
			int n = Integer.parseInt(number.substring(i, i + 1));
			if (alternate) {
				n *= 2;
				if (n > 9) {
					n = (n % 10) + 1;
				}
			}
			sum += n;
			alternate = !alternate;
		}

		return sum;
	}

	private static int getChecksumCorrectDigit(int checksum) {
		return (checksum % 10) == 0 ? 0 : 10 - (checksum % 10);
	}

	private static int getControlDigit(String number, boolean isEven) {
		int controlDigit = getChecksumCorrectDigit(getChecksum(number));
		return isEven ? controlDigit : digitTransformMap.get(controlDigit);
	}
}
