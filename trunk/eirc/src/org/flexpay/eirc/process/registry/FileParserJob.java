package org.flexpay.eirc.process.registry;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.process.job.Job;
import org.flexpay.common.service.FPFileService;
import org.flexpay.eirc.sp.RegistryFileParser;
import org.springframework.beans.factory.annotation.Required;

import java.io.Serializable;
import java.util.Map;

public class FileParserJob extends Job {

	private RegistryFileParser parser;
    private FPFileService fpFileService;

	public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {
		Long fileId = (Long) parameters.get("FileId");

		FPFile spFile = fpFileService.read(new Stub<FPFile>(fileId));
		if (spFile == null) {
			log.warn("Invalid File Id");
			return RESULT_ERROR;
		}
		try {
			parser.parse(spFile);
		} catch (Exception e) {
			log.warn("Parser exception", e);
			return RESULT_ERROR;
		}
		return RESULT_NEXT;
	}

    @Required
	public void setParser(RegistryFileParser parser) {
		this.parser = parser;
	}

    @Required
	public void setFpFileService(FPFileService fpFileService) {
		this.fpFileService = fpFileService;
	}

}
