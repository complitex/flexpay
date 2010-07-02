package org.flexpay.common.util;

import java.util.Map;

import static org.flexpay.common.util.CollectionUtils.ar;

/**
 * Util class for Luhn algorithm
 */
public class Luhn {

	private static final Map<Integer, Integer> directMap = CollectionUtils.map(
			ar(0, 1, 2, 3, 4, 5, 6, 7, 8, 9),
			ar(0, 2, 4, 6, 8, 1, 3, 5, 7, 9));
	private static final Map<Integer, Integer> reverseMap = CollectionUtils.map(
			ar(0, 1, 2, 3, 4, 5, 6, 7, 8, 9),
			ar(0, 5, 1, 6, 2, 7, 3, 8, 4, 9));

	/**
	 * @param number String(contains only digits) for validating by Luhn algorithm
	 * @return true if valid by Luhn algorithm, false otherwise
	 */
	public static boolean isValidNumber(String number) {
		return getChecksum(number) % 10 == 0;
	}

	/**
	 * @param number String contains only digits
	 * @param ind	index(start from right to left) of inserting control digit
	 * @return source string with inserted control digit that is valid by Luhn
	 */
	public static String insertControlDigit(String number, int ind) {
		if (ind > number.length()) {
			throw new IllegalArgumentException(
					"ind value must be equal or less then number length. number="
					+ number + "; ind=" + ind);
		}

		int controlDigitInd = number.length() - ind;
		String leftPart = number.substring(0, controlDigitInd);
		String rightPart = number.substring(controlDigitInd);

		int controlDigit = 0;
		int reminder = getChecksum(leftPart + controlDigit + rightPart) % 10;
		if (reminder != 0) {
			controlDigit = isEven(controlDigitInd) ? (10 - reminder)
												   : getSourceDigit(10 - reminder);
		}

		return leftPart + controlDigit + rightPart;
	}

	public static String controlDigit(String str) {

		int controlDigit = 0;
		int reminder = getChecksum(str + 0) % 10;
		if (reminder != 0) {
			controlDigit = isEven(str.length()) ? (10 - reminder)
												   : getSourceDigit(10 - reminder);
		}
		return String.valueOf(controlDigit);
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
