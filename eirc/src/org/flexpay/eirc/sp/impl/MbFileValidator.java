package org.flexpay.eirc.sp.impl;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.file.FPFile;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang.time.StopWatch;

public abstract class MbFileValidator {

	protected Logger log = LoggerFactory.getLogger(getClass());

	public static final String FILE_CREATION_DATE_FORMAT = "ddMMyy";
	public static final String LAST_FILE_STRING_BEGIN = "999999999";
	public static final String REGISTRY_FILE_ENCODING = "Cp866";
	public static final String FIRST_FILE_STRING =
			"                                                                                                    "
			+ "                                                                                                    "
			+ "                                                                                                    ";

	public boolean validate(FPFile spFile) throws FlexPayException {

		log.info("Started file validation: {}", spFile);

		StopWatch watch = new StopWatch();
		watch.start();

		boolean ret = validateFile(spFile);

		watch.stop();

		log.info("Validation if file {} finished in {}", spFile, watch);

		return ret;
	}

	protected abstract boolean validateFile(@NotNull FPFile spFile) throws FlexPayException;

	protected void validateFields(String[] fields) throws FlexPayException {
		String errorFields = "";
		for (int i = 0; i < fields.length; i++) {
			if (fields[i].startsWith(" ") || (fields[i].endsWith(" ") && i < fields.length - 1)) {
				errorFields += (errorFields.length() > 0 ? ", " : "") + (i + 1);
			}
		}
		if (errorFields.length() > 0) {
			throw new FlexPayException("Incorrect fields in record. Detect spaces after or before \"=\" (fields: " + errorFields + ")");
		}
	}

}
