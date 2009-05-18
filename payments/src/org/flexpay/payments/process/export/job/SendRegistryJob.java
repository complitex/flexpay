package org.flexpay.payments.process.export.job;

import org.flexpay.common.process.job.Job;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.service.transport.OutTransport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.Stub;

import java.io.Serializable;
import java.util.Map;

public class SendRegistryJob extends Job {
    private FPFileService fpFileService;
    private OutTransport outTransport;

    public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {
        Long fileId = (Long) parameters.get("FileId");

        FPFile spFile = fpFileService.read(new Stub<FPFile>(fileId));
        if (spFile == null) {
            log.warn("Invalid File Id");
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

    public void setFpFileService(FPFileService fpFileService) {
        this.fpFileService = fpFileService;
    }

    public void setOutTransport(OutTransport outTransport) {
        this.outTransport = outTransport;
    }
}
