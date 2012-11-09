package org.flexpay.payments.process.export.handler2;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryFPFileType;
import org.flexpay.common.process.handler.ProcessInstanceExecuteHandler;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.service.RegistryService;
import org.flexpay.payments.process.export.ExportJobParameterNames;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Delete file
 */
public class DeleteFPFileHandler extends ProcessInstanceExecuteHandler {

	private FPFileService fpFileService;
	private RegistryService registryService;

	@Transactional (readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public String execute(Map<String, Object> parameters) throws FlexPayException {

		FPFile file = getFile(parameters);
        if (file == null) {
            return RESULT_ERROR;
        }

		unlinkFileFromRegistry(file);

        log.debug("Deleting file {}", file.getOriginalName());
		//file = fpFileService.read(Stub.stub(file));
        fpFileService.delete(file);

        return RESULT_NEXT;
    }

	private void unlinkFileFromRegistry(FPFile file) throws FlexPayException {

		List<Registry> registries = registryService.findObjects(new Page<Registry>(1, 1), file.getId());
		if (registries.isEmpty()) {
			log.info("No registry found with file id {}", file.getId());
			return;
		}

		Registry registry = registries.get(0);
        RegistryFPFileType fileType = null;
        for (Map.Entry<RegistryFPFileType, FPFile> fileEntry : registry.getFiles().entrySet()) {
            if (fileEntry.getValue().getId() == file.getId()) {
                fileType = fileEntry.getKey();
                break;
            }
        }
        if (fileType == null) {
            log.info("Not found file id {} in registry id {}", new Object[]{file.getId(), registry.getId()});
        }
		registryService.update(registry);
	}

	private FPFile getFile(Map<String, Object> parameters) {

		Long fileId = (Long) parameters.get(ExportJobParameterNames.FILE_ID);
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

	@Required
	public void setRegistryService(RegistryService registryService) {
		this.registryService = registryService;
	}
}
