package org.flexpay.common.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Vladislav
 * 
 * Util class for Luhn algorithm
 */
public class Luhn {

	private static Map<Integer, Integer> directMap = new HashMap<Integer, Integer>(
			10);;
	private static Map<Integer, Integer> reverseMap = new HashMap<Integer, Integer>(
			10);

	static {
		directMap.put(0, 0);
		directMap.put(1, 2);
		directMap.put(2, 4);
		directMap.put(3, 6);
		directMap.put(4, 8);
		directMap.put(5, 1);
		directMap.put(6, 3);
		directMap.put(7, 5);
		directMap.put(8, 7);
		directMap.put(9, 9);

		reverseMap.put(0, 0);
		reverseMap.put(1, 5);
		reverseMap.put(2, 1);
		reverseMap.put(3, 6);
		reverseMap.put(4, 2);
		reverseMap.put(5, 7);
		reverseMap.put(6, 3);
		reverseMap.put(7, 8);
		reverseMap.put(8, 4);
		reverseMap.put(9, 9);
	}

	/**
	 * @param number
	 *            String(contains only digits) for validating by Luhn algorithm
	 * @return true if valid by Luhn algorithm, false otherwise
	 */
	public static boolean isValidNumber(String number) {
		return getChecksum(number) % 10 == 0;
	}

	/**
	 * @param number
	 *            String contains only digits
	 * @param ind
	 *            index(start from right to left) of inserting control digit
	 * @return source string with inserted control digit that is valid by Luhn
	 */
	public static String insertControlDigit(String number, int ind) {
		if (ind > number.length()) {
			throw new IllegalArgumentException(
					"ind value must be equal or less then number lenth. number="
							+ number + "; ind=" + ind);
		}

		int controlDigitInd = number.length() - ind;
		String leftPart = number.substring(0, controlDigitInd);
		String rightPart = number.substring(controlDigitInd);

		int controlDigit = 0;
		int ostatok = getChecksum(leftPart + controlDigit + rightPart) % 10;
		if (ostatok != 0) {
			controlDigit = isEven(controlDigitInd) ? (10 - ostatok)
					: getSourceDigit(10 - ostatok);
		}

		return leftPart + controlDigit + rightPart;
	}

	private static int getChecksum(String number) {
		int sum = 0;
		for (int i = number.length() - 1; i >= 0; i--) {
			int digit = Integer.parseInt(number.substring(i, i + 1));
			sum = sum + getResultDigit(digit, number.length() - i - 1);
		}

		return sum;
	}

	private static int getResultDigit(int source) {
		return directMap.get(source);
	}

	private static int getResultDigit(int source, int ind) {
		if (isEven(ind)) {
			return source;
		} else {
			return getResultDigit(source);
		}
	}

	private static int getSourceDigit(int result) {
		return reverseMap.get(result);

	}

	private static boolean isEven(int i) {
		return i % 2 == 0;
	}
}
