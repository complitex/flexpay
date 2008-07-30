package org.flexpay.common.util;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class StringUtil {

	/**
	 * TODO: find out what's for this method
	 *
	 * @return random string
	 */
	public static String getRandomString() {
		return System.currentTimeMillis() + "-" + Math.random();
	}

	public static List<String> tokenize(String line, String delim) {
		int ind1 = 0;
		int ind2;
		List<String> fieldList = new ArrayList<String>();
		while ((ind2 = line.indexOf(delim, ind1)) != -1) {
			fieldList.add(line.substring(ind1, ind2));
			ind1 = ind2 + 1;
		}
		fieldList.add(line.substring(ind1, line.length()));

		return fieldList;
	}

	/**
	 * Split string with delimiter taking in account escape character
	 *
	 * @param str		Containers data
	 * @param delimiter  Delimiter character
	 * @param escapeChar Escape character
	 * @return List of separate containers
	 * @throws IllegalArgumentException if <code>str</code> have final escapeChar without following character
	 */
	public static List<String> splitEscapable(String str, char delimiter, char escapeChar)
			throws IllegalArgumentException {

		if (str == null) {
			return Collections.emptyList();
		}

		List<String> datum = new ArrayList<String>();
		StringBuilder buf = new StringBuilder(str.length());
		boolean escaped = false;
		boolean haveFinalDelimiter = false;
		for (char c : str.toCharArray()) {
			haveFinalDelimiter = false;
			if (c == escapeChar && !escaped) {
				escaped = true;
				// remove escape symbol from result
				continue;
			}
			if (c == delimiter && !escaped) {
				datum.add(buf.toString());
				buf.setLength(0);
				haveFinalDelimiter = true;
			} else if (c != delimiter && escaped) {
				buf.append(escapeChar).append(c);
			} else {
				buf.append(c);
			}
			escaped = false;
		}
		if (escaped) {
			throw new IllegalArgumentException("Not found final escaped simbol: " + str);
		}
		if (buf.length() > 0 || haveFinalDelimiter) {
			datum.add(buf.toString());
		}

		return datum;
	}

	public static String fillLeadingZero(String source, int targetLength) {
		return StringUtils.leftPad(source, targetLength, '0');
	}
}
