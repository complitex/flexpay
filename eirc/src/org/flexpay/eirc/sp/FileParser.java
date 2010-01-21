package org.flexpay.eirc.sp;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.Registry;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.util.List;
import java.util.Map;

public interface FileParser {

	public final static String MB_CORRECTIONS_FILE_TYPE = "mbCorrections";
	public final static String MB_REGISTRY_FILE_TYPE = "mbRegistry";
	public final static String REGISTRY_FILE_TYPE = "registry";

	List<Registry> parse(FPFile file) throws Exception;

	List<Registry> parse(FPFile file, Logger log) throws Exception;

	int iterateParseFile(@NotNull BufferedReader reader, @NotNull Map<String, Object> properties) throws FlexPayException;

}
