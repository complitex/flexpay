package org.flexpay.eirc.sp;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.FPFile;
import org.flexpay.common.persistence.registry.RegistryRecordStatus;
import org.flexpay.eirc.service.SpRegistryRecordStatusService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public abstract class MbFileParser<T> {

	protected Logger log = LoggerFactory.getLogger(getClass());

	public static final String LAST_FILE_STRING_BEGIN = "999999999";
	public static final String REGISTRY_FILE_ENCODING = "Cp866";
	public static final DateFormat OPERATION_DATE_FORMAT = new SimpleDateFormat("MMyy");

	protected RegistryRecordStatus statusLoaded;

	private MbFileValidator validator;
	private SpRegistryRecordStatusService spRegistryRecordStatusService;

	public T parse(FPFile spFile) throws FlexPayException {

		if (validator != null) {
			if (!validator.validate(spFile)) {
				log.debug("Validation failed");
				return null;
			}
		} else {
			log.debug("Validator is null. Skipping validation");
		}

		log.info("Starting parsing file: {}", spFile.getOriginalName());

		long beginTime = System.currentTimeMillis();

		File file = spFile.getFile();
		if (file == null) {
			log.debug("Incorrect spFile: can't find file on server (spFile.id = {})", spFile.getId());
			throw new FlexPayException("For FPFile (id = " + spFile.getId() + ") not found temp file: " + spFile.getNameOnServer());
		}

		statusLoaded = spRegistryRecordStatusService.findByCode(RegistryRecordStatus.LOADED);
		if (statusLoaded == null) {
			throw new FlexPayException("Can't get registry record status \"loaded\" from database");
		}

		T ret = parseFile(spFile);

		long endTime = System.currentTimeMillis();
		long time = (endTime - beginTime) / 1000;

		log.info("Parsing file {} finished in {}", spFile.getOriginalName(), (time / 60 + "m " + time % 60 + "s"));

		return ret;

	}

	protected abstract T parseFile(@NotNull FPFile spFile) throws FlexPayException;

	public void setValidator(MbFileValidator validator) {
		this.validator = validator;
	}

	@Required
	public void setSpRegistryRecordStatusService(SpRegistryRecordStatusService spRegistryRecordStatusService) {
		this.spRegistryRecordStatusService = spRegistryRecordStatusService;
	}

}
