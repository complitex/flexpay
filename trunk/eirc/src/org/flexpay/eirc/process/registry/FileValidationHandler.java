package org.flexpay.eirc.process.registry;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.process.ProcessLogger;
import org.flexpay.common.process.handler.TaskHandler;
import org.flexpay.common.service.FPFileService;
import org.flexpay.eirc.sp.impl.FileValidationSchema;
import org.flexpay.eirc.sp.impl.LineParser;
import org.flexpay.eirc.sp.impl.ServiceValidationFactory;
import org.flexpay.eirc.sp.impl.validation.FileValidator;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

public class FileValidationHandler extends TaskHandler {

	private ServiceValidationFactory serviceValidationFactory;
    private FileValidationSchema fileValidationSchema;
	private LineParser lineParser;
	private FPFileService fpFileService;

	@Override
	public String execute(Map<String, Object> parameters) throws FlexPayException {

		// prepare process logger
		ProcessLogger.setThreadProcessId(getProcessInstanceId(parameters));

		Logger processLog = ProcessLogger.getLogger(getClass());

		Long spFileId = (Long) parameters.get(org.flexpay.eirc.process.registry.helper.ParseRegistryConstants.PARAM_FILE_ID);
		FPFile spFile = fpFileService.read(new Stub<FPFile>(spFileId));
		if (spFile == null) {
			processLog.error("Inner error");
			log.error("Can't get spFile from DB (id = {})", spFileId);
			return RESULT_ERROR;
		}

		FileValidator validator = serviceValidationFactory.createFileValidator(fileValidationSchema, lineParser, processLog);

        if (!validator.validate(spFile)) {
            log.debug("Validation failed");
            return RESULT_ERROR;
        }

		return RESULT_NEXT;
	}

	@Required
	public void setServiceValidationFactory(ServiceValidationFactory serviceValidationFactory) {
		this.serviceValidationFactory = serviceValidationFactory;
	}

	@Required
	public void setFileValidationSchema(FileValidationSchema fileValidationSchema) {
		this.fileValidationSchema = fileValidationSchema;
	}

	@Required
	public void setLineParser(LineParser lineParser) {
		this.lineParser = lineParser;
	}

	@Required
	public void setFpFileService(FPFileService fpFileService) {
		this.fpFileService = fpFileService;
	}
}
