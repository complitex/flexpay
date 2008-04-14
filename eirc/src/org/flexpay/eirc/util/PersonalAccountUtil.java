package org.flexpay.eirc.util;

import org.flexpay.common.service.SequenceService;
import org.flexpay.common.util.Luhn;
import org.flexpay.common.util.StringUtil;
import org.flexpay.eirc.util.config.ApplicationConfig;

public class PersonalAccountUtil {
	private static SequenceService sequenceService;

	public static String nextPersonalAccount() {
		String eircId = ApplicationConfig.getInstance().getEircId();
		String result = sequenceService.next(
				SequenceService.PERSONAL_ACCOUNT_SEQUENCE_ID).toString();
		result = StringUtil.fillLeadingZero(result, 7);
		result = eircId + result;
		result = Luhn.insertControlDigit(result, 1);
		return result;
	}

	public void setSequenceService(SequenceService sequenceService) {
		PersonalAccountUtil.sequenceService = sequenceService;
	}
}
