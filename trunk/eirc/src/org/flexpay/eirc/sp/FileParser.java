package org.flexpay.eirc.sp;

import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.Registry;

import java.util.List;

public interface FileParser {
	
	List<Registry> parse(FPFile file) throws Exception;

}
