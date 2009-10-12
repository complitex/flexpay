package org.flexpay.eirc.process.registry;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.process.ProcessLogger;
import org.flexpay.common.process.job.Job;
import org.flexpay.eirc.sp.FileParser;
import org.springframework.beans.factory.annotation.Required;

import java.io.Serializable;
import java.util.Map;

public class FileParserJob extends Job {

	public static final String PARAM_FILE = "file";
	public static final String PARAM_FILE_TYPE = "fileType";

	private FileParser registryFileParser;
	private FileParser mbCorrectionsFileParser;
	private FileParser mbRegistryFileParser;

	public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {

		FPFile spFile = (FPFile) parameters.get(PARAM_FILE);
		String fileType = (String) parameters.get(PARAM_FILE_TYPE);

		try {
			if (FileParser.MB_CORRECTIONS_FILE_TYPE.equals(fileType)) {
				mbCorrectionsFileParser.parse(spFile, ProcessLogger.getLogger(getClass()));
			} else if (FileParser.MB_REGISTRY_FILE_TYPE.equals(fileType)) {
				mbRegistryFileParser.parse(spFile, ProcessLogger.getLogger(getClass()));
			} else if (FileParser.REGISTRY_FILE_TYPE.equals(fileType)) {
				registryFileParser.parse(spFile, ProcessLogger.getLogger(getClass()));
			} else {
				log.error("Incorrect fileType variable - " + fileType);
				return RESULT_ERROR;
			}
		} catch (Exception e) {
			log.warn("Parser exception", e);
			return RESULT_ERROR;
		}
		return RESULT_NEXT;
	}

	@Required
	public void setRegistryFileParser(FileParser registryFileParser) {
		this.registryFileParser = registryFileParser;
	}

	@Required
	public void setMbCorrectionsFileParser(FileParser mbCorrectionsFileParser) {
		this.mbCorrectionsFileParser = mbCorrectionsFileParser;
	}

	@Required
	public void setMbRegistryFileParser(FileParser mbRegistryFileParser) {
		this.mbRegistryFileParser = mbRegistryFileParser;
	}

}
