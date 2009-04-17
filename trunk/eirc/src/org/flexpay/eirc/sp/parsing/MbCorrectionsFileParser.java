package org.flexpay.eirc.sp.parsing;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.flexpay.eirc.sp.FileParser;
import org.flexpay.eirc.sp.Validator;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.FPFile;
import org.flexpay.common.exception.FlexPayException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jetbrains.annotations.NotNull;

@Transactional(readOnly = true)
public class MbCorrectionsFileParser implements FileParser {

	private Logger log = LoggerFactory.getLogger(getClass());

	private Validator validator;

	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = false)
	public Registry parse(@NotNull FPFile spFile) throws FlexPayException {

		if (validator != null) {
			log.info("Starting validation MB corrections file...");
			validator.validate(spFile);
			log.info("MB corrections file validation completed");
		}

		return null;
	}

	public void setValidator(Validator validator) {
		this.validator = validator;
	}

}
