package org.flexpay.eirc.sp;

import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.Registry;
import org.slf4j.Logger;

import java.util.List;

public interface FileParser {
	
	List<Registry> parse(FPFile file) throws Exception;
	List<Registry> parse(FPFile file, Logger log) throws Exception;

}
