package org.flexpay.payments.process.export.handler2;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.process.handler.TaskHandler;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.util.FPFileUtil;
import org.flexpay.payments.process.export.ExportJobParameterNames;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

import static org.flexpay.payments.process.export.ExportJobParameterNames.GENERATED_FILE_NAME;

public class CreateFPFileHandler extends TaskHandler {

	private String moduleName;
    private String userName;
    private String fileName;

    private FPFileService fpFileService;

	public String execute(Map<String, Object> parameters) throws FlexPayException {

		FPFile fpFile = null;
        try {
            fpFile = new FPFile();
            fpFile.setModule(fpFileService.getModuleByName(moduleName));
            fpFile.setUserName(userName);

			setFileName(parameters, fpFile);
			FPFileUtil.createEmptyFile(fpFile);
            fpFileService.create(fpFile);

            parameters.put(ExportJobParameterNames.FILE_ID, fpFile.getId());

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

	private void setFileName(Map<String, Object> parameters, FPFile fpFile) {

		String generatedFileName = (String) parameters.get(GENERATED_FILE_NAME);
		if (generatedFileName != null) {
			fpFile.setOriginalName(generatedFileName);
		} else {
			fpFile.setOriginalName(fileName);
		}
	}

	@Required
    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

	@Required
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Required
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Required
    public void setFpFileService(FPFileService fpFileService) {
        this.fpFileService = fpFileService;
    }
}
