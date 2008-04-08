package org.flexpay.eirc.util;

import org.flexpay.common.service.SequenceService;
import org.flexpay.common.util.Luhn;
import org.flexpay.eirc.util.config.ApplicationConfig;

public class PersonalAccountUtil {
	private static SequenceService sequenceService;

	public static String nextPersonalAccount() {
		String eircId = ApplicationConfig.getInstance().getEircId();
		String result = sequenceService.next(
				SequenceService.PERSONAL_ACCOUNT_SEQUENCE_ID).toString();
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

	public void setSequenceService(SequenceService sequenceService) {
		PersonalAccountUtil.sequenceService = sequenceService;
	}
}
