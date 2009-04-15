package org.flexpay.eirc.sp;

import org.flexpay.common.persistence.FPFile;
import org.flexpay.common.exception.FlexPayException;

public interface Validator {

	void validate(FPFile file) throws FlexPayException;

}
