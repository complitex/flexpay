package org.flexpay.eirc.sp.impl;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.util.FPFileUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public abstract class MbFileValidator {

	protected Logger log = LoggerFactory.getLogger(getClass());

	public static final String FILE_CREATION_DATE_FORMAT = "ddMMyy";
	public static final String LAST_FILE_STRING_BEGIN = "999999999";
	public static final String REGISTRY_FILE_ENCODING = "Cp866";
	public static final String FIRST_FILE_STRING =
			"                                                                                                    "
			+ "                                                                                                    "
			+ "                                                                                                    ";

	public boolean validate(FPFile spFile) throws FlexPayException  {

		File file = FPFileUtil.getFileOnServer(spFile);
		if (file == null) {
			log.debug("Incorrect spFile: can't find file on server (spFile.id = {})", spFile.getId());
			throw new FlexPayException("For FPFile (id = " + spFile.getId() + ") not found temp file: " + spFile.getNameOnServer());
		}

		log.info("Validation file {} started", spFile.getOriginalName());

		long beginTime = System.currentTimeMillis();

		boolean ret = validateFile(spFile);

		long endTime = System.currentTimeMillis();
		long time = (endTime - beginTime) / 1000;

		log.info("Validation file {} finished in {}", spFile.getOriginalName(), (time / 60 + "m " + time % 60 + "s"));

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
