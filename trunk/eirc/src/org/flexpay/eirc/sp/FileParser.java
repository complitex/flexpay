package org.flexpay.eirc.sp;

import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.Registry;

public interface FileParser {
	
	Registry parse(FPFile file) throws Exception;

}
