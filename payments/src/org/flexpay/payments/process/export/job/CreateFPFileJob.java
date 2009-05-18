package org.flexpay.payments.process.export.job;

import org.flexpay.common.process.job.Job;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.util.FPFileUtil;

import java.io.Serializable;
import java.util.Map;

public class CreateFPFileJob extends Job {
    private String moduleName;
    private String userName;

    private FPFileService fpFileService;

    public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {
        FPFile fpFile = null;
        try {
            fpFile = new FPFile();
            fpFile.setModule(fpFileService.getModuleByName(moduleName));
            fpFile.setUserName(userName);
            FPFileUtil.createEmptyFile(fpFile);

            fpFileService.create(fpFile);
            parameters.put("FileId", fpFile.getId());
            log.info("File created {}", fpFile);
        } catch (Exception e) {
            log.error("Unknown file type", e);
            if (fpFile != null) {
                if (fpFile.getId() != null) {
                    fpFileService.delete(fpFile);
                } else {
                    fpFileService.deleteFromFileSystem(fpFile);
                }
            }
            return RESULT_ERROR;
        }
        return RESULT_NEXT;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setFpFileService(FPFileService fpFileService) {
        this.fpFileService = fpFileService;
    }
}

