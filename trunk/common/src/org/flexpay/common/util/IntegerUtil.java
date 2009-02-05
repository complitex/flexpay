package org.flexpay.common.util;

public class IntegerUtil {

	private static final int XLS_RADIX = 26;

	/**
	 * Parses the string argument as a signed integer in the radix of 26. The characters in the
	 * string must all be in range [A-Z]
	 *
	 * @param s	 the <code>String</code> containing the integer representation to be parsed
	 * @return the integer represented by the string argument.
	 * @throws NumberFormatException if the <code>String</code> does not contain a parsable <code>int</code>.
	 */
	public static int parseXLSInt(String s) throws NumberFormatException {
		if (s == null) {
			throw new NumberFormatException("null");
		}
		if (s.length() == 0) {
			throw new NumberFormatException("empty string");
		}


		int result = -1;
		for (int i = 0, max = s.length(); i < max; ++i) {
			int ch = s.charAt(i);
			if (ch < 'A' || 'Z' < ch) {
				throw forInputString(s);
			}
			result = (result + 1) * XLS_RADIX + (ch - 'A');
		}

		return result + 1;
	}

	public static String toXLSColumnNumber(int n) throws NumberFormatException {

		if (n <= 0) {
			throw new NumberFormatException("Invalid XLS column number " + n);
		}

		StringBuilder sb = new StringBuilder();
		while (n > 0) {
			--n;
			int remainder = n % XLS_RADIX;
			n = n / XLS_RADIX;
			char digit = (char) (remainder + 'A');
			sb.insert(0, digit);
		}

		return sb.toString();
	}

	/**
	 * Factory method for making a <code>NumberFormatException</code> given the specified input which caused the error.
	 *
	 * @param s the input causing the error
	 * @return NumberFormatException
	 */
	static NumberFormatException forInputString(String s) {
		return new NumberFormatException("For input string: \"" + s + "\"");
	}
}
