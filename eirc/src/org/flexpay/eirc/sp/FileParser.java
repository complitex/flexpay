package org.flexpay.eirc.sp;

import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.Registry;
import org.slf4j.Logger;

import java.util.List;

public interface FileParser {

	public final static String MB_CORRECTIONS_FILE_TYPE = "mbCorrections";
	public final static String MB_REGISTRY_FILE_TYPE = "mbRegistry";
	public final static String REGISTRY_FILE_TYPE = "registry";

	List<Registry> parse(FPFile file) throws Exception;

	List<Registry> parse(FPFile file, Logger log) throws Exception;

}
