package org.flexpay.eirc.sp.impl;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.payments.service.SPService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public abstract class MbFileValidator {

	protected Logger log = LoggerFactory.getLogger(getClass());

	public static final String FILE_CREATION_DATE_FORMAT = "ddMMyy";
	public static final String LAST_FILE_STRING_BEGIN = "999999999";
	public static final String REGISTRY_FILE_ENCODING = "Cp866";
	public static final String FIRST_FILE_STRING =
			"                                                                                                    "
			+ "                                                                                                    "
			+ "                                                                                                    ";

	public static final String BUILDING_BULK_PREFIX = "К";
	public static final String SERVICE_CODES_SEPARATOR = ";";


	protected ServiceTypesMapper serviceTypesMapper;
	protected SPService spService;
	protected CorrectionsService correctionsService;
	protected Stub<DataSourceDescription> megabankSD;

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

	public static String[] parseBuildingAddress(String mbBuidingAddress) throws FlexPayException {
		String[] parts = StringUtils.split(mbBuidingAddress, ' ');
		if (parts.length > 1) {
			if (!parts[1].startsWith(BUILDING_BULK_PREFIX)) {
				throw new FlexPayException("Invalid building bulk value: " + parts[1]);
			}
			parts[1] = parts[1].substring(BUILDING_BULK_PREFIX.length());
		}
		return parts;
	}

	@Required
	public void setServiceTypesMapper(ServiceTypesMapper serviceTypesMapper) {
		this.serviceTypesMapper = serviceTypesMapper;
	}

	@Required
	public void setSpService(SPService spService) {
		this.spService = spService;
	}

	@Required
	public void setCorrectionsService(CorrectionsService correctionsService) {
		this.correctionsService = correctionsService;
	}

	@Required
	public void setMegabankSD(DataSourceDescription megabankSD) {
		this.megabankSD = stub(megabankSD);
	}
}
