package org.flexpay.common.util;

import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.flexpay.common.util.CollectionUtils.list;

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
		List<String> fieldList = list();
		while ((ind2 = line.indexOf(delim, ind1)) != -1) {
			fieldList.add(line.substring(ind1, ind2));
			ind1 = ind2 + 1;
		}
		fieldList.add(line.substring(ind1, line.length()));

		return fieldList;
	}

    public static String format(String str, char delimiter, char escapeChar) {
        if (str == null) {
            return str;
        }

        StringBuilder buf = new StringBuilder(str.length());
        for (char c : str.toCharArray()) {
            if (c == delimiter) {
                buf.append(escapeChar);
            }
            buf.append(c);
        }

        return buf.toString();
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

		if (str == null) {
			return Collections.emptyList();
		}

		List<String> datum = list();
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
			throw new IllegalArgumentException("Not found final escaped symbol: " + str);
		}
		if (buf.length() > 0 || haveFinalDelimiter) {
			datum.add(buf.toString());
		}

		return datum;
	}

	public static String fillLeadingZero(String source, int targetLength) {
		return StringUtils.leftPad(source, targetLength, '0');
	}

	public static String fillLeadingZero(long source, int targetLength) {
		return fillLeadingZero(String.valueOf(source), targetLength);
	}

	/**
	 * Get file name from its path
	 * <p/>
	 * For example <code>boo/bar.txt</code> path has <code>bar.txt</code> name
	 *
	 * @param path File path
	 * @return File name
	 */
	public static String getFileName(@NotNull String path) {
		int slashPos = path.lastIndexOf('/') + 1;
		return path.substring(slashPos);
	}

	/**
	 * Get file extension from its name
	 * <p/>
	 * For example <code>boo/bar.txt</code> path has <code>.txt</code> extension
	 *
	 * @param path File path
	 * @return File extension if available, or empty string
	 */
	@NotNull
	public static String getFileExtension(@NotNull String path) {
		String fileName = getFileName(path);
		int slashPos = fileName.lastIndexOf('.');
		if (slashPos == -1) {
			return "";
		}
		return fileName.substring(slashPos);
	}

	/**
	 * Returns file name without extension
	 *
	 * @param path full file name
	 * @return file name without extension
	 */
	public static String getFileNameWithoutExtension(@NotNull String path) {
		String fName = getFileName(path);
		int pos = fName.lastIndexOf(".");
		return pos > 0 ? fName.substring(0, pos) : fName;
	}

	/**
	 * Parse digits of a <code>bd</code> at specified position
	 * <p/>
	 * For example the digit <i>10.03</i> at position 0 is 0, at position -1 is 0, at
	 * position -2 is 3 and in position 1 is 1
	 *
	 * @param bd	   Number to get digit from
	 * @param position Position to get digit of
	 * @return Digit in a specified position, <code>0</code> if position does not have a digit 
	 */
	@NotNull
	public static String getDigit(BigDecimal bd, int position) {

		BigDecimal multiplier = BigDecimal.ONE.divide(BigDecimal.TEN);
		if (position < 0) {
			position = -position;
			multiplier = BigDecimal.TEN;
		}
		BigDecimal power = multiplier.pow(position);
		BigDecimal value = bd.abs().multiply(power).remainder(BigDecimal.TEN);

		return String.valueOf(value.intValue());
	}

	public static String getString(Object str) {
		return str == null ? "" : "" + str.toString();
	}

}
