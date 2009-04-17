package org.flexpay.eirc.sp;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.FPFile;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public abstract class MbFileParser<T> {

	protected Logger log = LoggerFactory.getLogger(getClass());

	public final static String LAST_FILE_STRING_BEGIN = "999999999";
	public final static String REGISTRY_FILE_ENCODING = "Cp866";
	public final static DateFormat OPERATION_DATE_FORMAT = new SimpleDateFormat("MMyy");

	private Validator validator;

	public T parse(FPFile spFile) throws FlexPayException {

		if (validator != null) {
			log.info("Starting validation MB registry file...");
			validator.validate(spFile);
			log.info("MB registry file validation completed");
		}

		log.info("Starting parsing file: {}", spFile.getOriginalName());

		long beginTime = System.currentTimeMillis();

		File file = spFile.getFile();
		if (file == null) {
			log.debug("Incorrect spFile: can't find file on server (spFile.id = {})", spFile.getId());
			throw new FlexPayException("For FPFile (id = " + spFile.getId() + ") not found temp file: " + spFile.getNameOnServer());
		}

		T ret = parseFile(spFile);

		long endTime = System.currentTimeMillis();
		long time = (endTime - beginTime) / 1000;

		log.info("Parsing file {} finished in {}", spFile.getOriginalName(), (time / 60 + "m " + time % 60 + "s"));

		return ret;

	}

	protected abstract T parseFile(@NotNull FPFile spFile) throws FlexPayException;

	public void setValidator(Validator validator) {
		this.validator = validator;
	}

}
