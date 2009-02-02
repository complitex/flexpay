package org.flexpay.sz.actions;

import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.common.persistence.FPFileStatus;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.sz.persistence.SzFile;
import org.flexpay.sz.service.SzFileService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SzFileOperationAction extends FPActionWithPagerSupport<SzFile> {

	private Set<Long> szFileIds = new HashSet<Long>();
	private String action1;

	private ProcessManager processManager;
	private String moduleName;
	private SzFileService szFileService;
	private FPFileService fpFileService;

	@NotNull
	public String doExecute() throws Exception {

		log.debug("Action - {}; SzFileIds - {}", action1, szFileIds);

		if (szFileIds == null || szFileIds.isEmpty()) {
			return SUCCESS;
		}

		String processName;
		Long statusCode;

		if ("loadToDB".equals(action1)) {
			processName = "SzFileLoadToDbProcess";
			statusCode = SzFile.PROCESSING_FILE_STATUS;
		} else if ("fullDelete".equals(action1)) {
			processName = "SzFileFullDeleteProcess";
			statusCode = SzFile.DELETING_FILE_STATUS;
		} else if ("loadFromDB".equals(action1)) {
			processName = "SzFileLoadFromDbProcess";
			statusCode = SzFile.PROCESSING_FILE_STATUS;
		} else if ("deleteFromDB".equals(action1)) {
			processName = "SzFileDeleteFromDbProcess";
			statusCode = SzFile.DELETING_FILE_STATUS;
		} else {
			log.warn("Incorrect action parameter - {}", action1);
			return SUCCESS;
		}
		FPFileStatus status = fpFileService.getStatusByCodeAndModule(statusCode, moduleName);

		szFileService.updateStatus(szFileIds, status);

		Map<Serializable, Serializable> contextVariables = CollectionUtils.map();
		contextVariables.put("fileIds", (Serializable) szFileIds);
		processManager.createProcess(processName, contextVariables);

		log.debug("Process for operation \"{}\" and fileIds={} started", action1, szFileIds);

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

	public void setSzFileIds(Set<Long> szFileIds) {
		this.szFileIds = szFileIds;
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
