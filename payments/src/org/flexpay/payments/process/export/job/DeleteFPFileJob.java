package org.flexpay.payments.process.export.job;

import org.flexpay.common.process.job.Job;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.Stub;

import java.io.Serializable;
import java.util.Map;

/**
 * Delete file
 */
public class DeleteFPFileJob extends Job {

    private FPFileService fpFileService;

    public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {
        Long fileId = (Long)parameters.get("FileId");
        if (fileId == null) {
            log.error("Did not find file in job parameters");
            return RESULT_ERROR;
        }

        FPFile file = fpFileService.read(new Stub<FPFile>(fileId));
        if (file == null) {
            log.error("File {} not found", fileId);
            return RESULT_ERROR;
        }

        log.debug("Delete file {} ", file.getOriginalName());
        fpFileService.delete(file);
        return RESULT_NEXT;
    }

    public void setFpFileService(FPFileService fpFileService) {
        this.fpFileService = fpFileService;
    }
}
