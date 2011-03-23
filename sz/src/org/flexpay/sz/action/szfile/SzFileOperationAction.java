package org.flexpay.sz.action.szfile;

import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.persistence.file.FPFileStatus;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.service.FPFileService;
import org.flexpay.sz.persistence.SzFile;
import org.flexpay.sz.service.SzFileService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import static org.flexpay.common.util.CollectionUtils.map;
import static org.flexpay.common.util.CollectionUtils.set;
import static org.flexpay.sz.process.szfile.SzFileOperationJobParameterNames.FILE_IDS;

public class SzFileOperationAction extends FPActionSupport {

	public final static String OPERATION_LOAD_TO_DB = "loadToDB";
	public final static String OPERATION_LOAD_FROM_DB = "loadFromDB";
	public final static String OPERATION_DELETE_FULL = "fullDelete";
	public final static String OPERATION_DELETE_FROM_DB = "deleteFromDB";

	private Set<Long> objectIds = set();
	private String action1;
	private String message = "";

	private String moduleName;
	private ProcessManager processManager;
	private SzFileService szFileService;
	private FPFileService fpFileService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		log.debug("action - {}; objectIds - {}", action1, objectIds);

		if (objectIds == null || objectIds.isEmpty()) {
			return SUCCESS;
		}

		String processName;
		Long statusCode;

		if (OPERATION_LOAD_TO_DB.equals(action1)) {
			processName = "SzFileLoadToDbProcess";
			statusCode = SzFile.PROCESSING_FILE_STATUS;
		} else if (OPERATION_DELETE_FULL.equals(action1)) {
			processName = "SzFileFullDeleteProcess";
			statusCode = SzFile.DELETING_FILE_STATUS;
		} else if (OPERATION_LOAD_FROM_DB.equals(action1)) {
			processName = "SzFileLoadFromDbProcess";
			statusCode = SzFile.PROCESSING_FILE_STATUS;
		} else if (OPERATION_DELETE_FROM_DB.equals(action1)) {
			processName = "SzFileDeleteFromDbProcess";
			statusCode = SzFile.DELETING_FILE_STATUS;
		} else {
			log.warn("Incorrect action parameter - {}", action1);
			return SUCCESS;
		}
		FPFileStatus status = fpFileService.getStatusByCodeAndModule(statusCode, moduleName);

		szFileService.updateStatus(objectIds, status);

		Map<Serializable, Serializable> contextVariables = map();
		contextVariables.put(FILE_IDS, (Serializable) objectIds);
		processManager.createProcess(processName, contextVariables);

		log.debug("Process for operation \"{}\" and fileIds={} started", action1, objectIds);

		return SUCCESS;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

	public void setAction1(String action1) {
		this.action1 = action1;
	}

	public void setObjectIds(Set<Long> objectIds) {
		this.objectIds = objectIds;
	}

	public String getMessage() {
		return message;
	}

	@Required
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	@Required
	public void setFpFileService(FPFileService fpFileService) {
		this.fpFileService = fpFileService;
	}

	@Required
	public void setSzFileService(SzFileService szFileService) {
		this.szFileService = szFileService;
	}

	@Required
	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}

}
