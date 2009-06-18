package org.flexpay.payments.process.export.job;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.process.job.Job;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.service.transport.OutTransport;
import org.springframework.beans.factory.annotation.Required;

import java.io.Serializable;
import java.util.Map;

public class SendRegistryJob extends Job {

    private FPFileService fpFileService;
    private OutTransport outTransport;

    public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {

        FPFile spFile = null;

        if (parameters.containsKey("File")) {
            Object o = parameters.get("File");
            if (o instanceof FPFile) {
                spFile = (FPFile) o;
				spFile = fpFileService.read(new Stub<FPFile>(spFile.getId()));
            } else {
                log.warn("Invalid file`s instance class");
            }
        } else if (parameters.containsKey("FileId")) {
            Long fileId = (Long) parameters.get("FileId");
            spFile = fpFileService.read(new Stub<FPFile>(fileId));
        }

        if (spFile == null) {
            log.warn("Did not find file in job parameters");
            return RESULT_ERROR;
        }
        
        try {
            outTransport.send(spFile);
        } catch (Exception e) {
            log.warn("Send file exception", e);
            return RESULT_ERROR;
        }

        return RESULT_NEXT;
    }

	@Required
    public void setFpFileService(FPFileService fpFileService) {
        this.fpFileService = fpFileService;
    }

	@Required
    public void setOutTransport(OutTransport outTransport) {
        this.outTransport = outTransport;
    }

}
