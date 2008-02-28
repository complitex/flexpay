package org.flexpay.common.util;

public class StringUtil {
	public static String getRandomString() {
		return System.currentTimeMillis() + "-" + Math.random();
	}

}
