package org.flexpay.common.util;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringUtil {
	private static Map<Integer, String> monthRusMap;

	static {
		monthRusMap = new HashMap<Integer, String>(12);
		monthRusMap.put(0, "январь");
		monthRusMap.put(1, "февраль");
		monthRusMap.put(2, "март");
		monthRusMap.put(3, "апрель");
		monthRusMap.put(4, "май");
		monthRusMap.put(5, "июнь");
		monthRusMap.put(6, "июль");
		monthRusMap.put(7, "август");
		monthRusMap.put(8, "сентябрь");
		monthRusMap.put(9, "октябрь");
		monthRusMap.put(10, "ноябрь");
		monthRusMap.put(11, "декабрь");
	}

	public static String getMonthRus(Integer month) {
		return monthRusMap.get(month);
	}

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
		int ind2 = -1;
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
	 * @throws IllegalArgumentException if <code>str</code> have final escapeChar without
	 *                                  following character
	 */
	public static List<String> splitEscapable(String str, char delimiter, char escapeChar)
			throws IllegalArgumentException {

		List<String> datum = new ArrayList<String>();
		StringBuilder buf = new StringBuilder(str.length());
		boolean escaped = false;
		for (char c : str.toCharArray()) {
			if (c == escapeChar && !escaped) {
				escaped = true;
				buf.append(c);
				continue;
			}
			if (c == delimiter && !escaped) {
				datum.add(buf.toString());
				buf.setLength(0);
			} else {
				buf.append(c);
			}
			escaped = false;
		}
		if (escaped) {
			throw new IllegalArgumentException("Not found final escaped simbol: " + str);
		}
		datum.add(buf.toString());

		return datum;
	}

	public static String fillLeadingZero(String source, int targetLength) {
		return StringUtils.leftPad(source, targetLength, '0');
	}
}
