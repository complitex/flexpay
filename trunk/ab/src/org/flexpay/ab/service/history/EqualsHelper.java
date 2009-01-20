package org.flexpay.ab.service.history;

import org.apache.commons.lang.StringUtils;

public abstract class EqualsHelper {

	public static boolean strEquals(String str1, String str2) {
		boolean s1Blank = StringUtils.isBlank(str1);
		boolean s2Blank = StringUtils.isBlank(str2);
		return (s1Blank && s2Blank) || (!s1Blank && str1.equals(str2));
	}
}
