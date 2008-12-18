package org.flexpay.eirc.process.registry;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.FlexPayFile;
import org.flexpay.common.process.job.Job;
import org.flexpay.common.service.FlexPayFileService;
import org.flexpay.eirc.sp.RegistryFileParser;
import org.springframework.beans.factory.annotation.Required;

import java.io.Serializable;
import java.util.Map;

public class FileParserJob extends Job {

	private RegistryFileParser parser;
    private FlexPayFileService flexPayFileService;

	public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {
		Long fileId = (Long) parameters.get("FileId");

		FlexPayFile spFile = flexPayFileService.read(fileId);
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
    public void setFlexPayFileService(FlexPayFileService flexPayFileService) {
        this.flexPayFileService = flexPayFileService;
    }

}
