package org.flexpay.eirc.sp;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.FPFile;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public abstract class MbFileValidator {

	protected Logger log = LoggerFactory.getLogger(getClass());

	public static final DateFormat FILE_CREATION_DATE_FORMAT = new SimpleDateFormat("ddMMyy");
	public static final String LAST_FILE_STRING_BEGIN = "999999999";
	public static final String REGISTRY_FILE_ENCODING = "Cp866";
	public static final String FIRST_FILE_STRING =
			"                                                                                                    "
			+ "                                                                                                    "
			+ "                                                                                                    ";

	public boolean validate(FPFile spFile) throws FlexPayException  {

		File file = spFile.getFile();
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

}
