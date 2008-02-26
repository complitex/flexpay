package org.flexpay.eirc.util;

import org.flexpay.common.util.Luhn;

public class PersonalAccountUtil {

	public static String nextPersonalAccount() {
		String eircId = "090"; // TODO need to retrieve it from EIRC config
		String result = "22"; // TODO need to retrieve it from sequence
		result = fillLeadingZero(result, 7);
		result = eircId + result;
		result = Luhn.insertControlDigit(result, 1);
		return result;
	}

	public static String fillLeadingZero(String source, int targetLenth) {
		StringBuffer buf = new StringBuffer();
		int count = targetLenth - source.length();
		if (count > 0) {
			for (int i = 0; i < count; i++) {
				buf.append("0");
			}
		}
		buf.append(source);

		return buf.toString();
	}
}
