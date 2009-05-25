package org.flexpay.eirc.process.registry;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.process.job.Job;
import org.flexpay.common.service.FPFileService;
import org.flexpay.eirc.sp.FileParser;
import org.springframework.beans.factory.annotation.Required;

import java.io.Serializable;
import java.util.Map;

public class FileParserJob extends Job {

	private FileParser registryFileParser;
	private FileParser mbCorrectionsFileParser;
	private FileParser mbRegistryFileParser;
    private FPFileService fpFileService;

	public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {
		Long fileId = (Long) parameters.get("FileId");
		String fileType = (String) parameters.get("FileType");

		FPFile spFile = fpFileService.read(new Stub<FPFile>(fileId));
		if (spFile == null) {
			log.warn("Invalid File Id");
			return RESULT_ERROR;
		}
		try {
			if ("mbCorrections".equals(fileType)) {
				mbCorrectionsFileParser.parse(spFile);
			} else if ("mbRegistry".equals(fileType)) {
				mbRegistryFileParser.parse(spFile);
			} else {
				registryFileParser.parse(spFile);
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

	@Required
	public void setFpFileService(FPFileService fpFileService) {
		this.fpFileService = fpFileService;
	}

}
