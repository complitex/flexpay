package org.flexpay.payments.process.export.job;

import org.flexpay.common.process.job.Job;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.Stub;
import org.springframework.beans.factory.annotation.Required;

import java.io.Serializable;
import java.util.Map;

/**
 * Delete file
 */
public class DeleteFPFileJob extends Job {

	// required services
    private FPFileService fpFileService;

	// parameter names
	private static final String FILE_ID_PARAMETER_NAME = "FileId";

	public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {

		FPFile file = getFile(parameters);
        if (file == null) {            
            return RESULT_ERROR;
        }

        log.debug("Deleting file {}", file.getOriginalName());
        fpFileService.delete(file);

        return RESULT_NEXT;
    }

	private FPFile getFile(Map<Serializable, Serializable> parameters) {

		Long fileId = (Long) parameters.get(FILE_ID_PARAMETER_NAME);
        if (fileId == null) {
            log.error("File was not found as a job parameter");
            return null;
        }

		FPFile file = fpFileService.read(new Stub<FPFile>(fileId));
		if (file == null) {
            log.error("File with id {} was not found", fileId);
            return null;
        }

		return file;
	}


	@Required
    public void setFpFileService(FPFileService fpFileService) {
        this.fpFileService = fpFileService;
    }
}
