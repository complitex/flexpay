package org.flexpay.eirc.sp;

import org.flexpay.common.persistence.FPFile;
import org.flexpay.common.exception.FlexPayException;

public interface FileParser {

	Object parse(FPFile file) throws FlexPayException;

}
