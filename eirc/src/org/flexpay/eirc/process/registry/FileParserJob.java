package org.flexpay.eirc.process.registry;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.process.job.Job;
import org.flexpay.eirc.persistence.SpFile;
import org.flexpay.eirc.service.RegistryFileService;
import org.flexpay.eirc.sp.RegistryFileParser;

import java.io.Serializable;
import java.util.Map;

public class FileParserJob extends Job {

	private RegistryFileParser parser;
	private RegistryFileService registryFileService;

	public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {
		Long fileID = (Long) parameters.get("FileId");

		SpFile spFile = registryFileService.read(fileID);
		if (spFile == null) {
			log.warn("Invalid File ID");
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

	public void setParser(RegistryFileParser parser) {
		this.parser = parser;
	}

	public void setSpFileService(RegistryFileService registryFileService) {
		this.registryFileService = registryFileService;
	}
}
