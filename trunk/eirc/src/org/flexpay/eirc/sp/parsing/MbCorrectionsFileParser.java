package org.flexpay.eirc.sp.parsing;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.FPFile;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.eirc.sp.MbFileParser;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class MbCorrectionsFileParser extends MbFileParser<Registry> {

	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = false)
	public Registry parseFile(@NotNull FPFile spFile) throws FlexPayException {

		return null;
	}

}
