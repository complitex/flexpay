package org.flexpay.sz.actions;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.FPFileStatus;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.sz.convert.SzFileUtil;
import org.flexpay.sz.persistence.SzFile;
import org.flexpay.sz.service.SzFileService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

public class SzFileOperationAction extends FPActionSupport {

	private Collection<Long> szFileIds;
	private String action1;
	private Long szFileId;

	private ProcessManager processManager;

	private String moduleName;
	private SzFileService szFileService;
	private FPFileService fpFileService;

	@NotNull
	public String doExecute() throws Exception {

		log.debug("Action - {}; SzFileIds - {}", action1, szFileIds);

		if ("loadFromDB".equals(action1)) {
			SzFileUtil.loadFromDb(szFileService.readFull(szFileId));
		} else if ("deleteFromDB".equals(action1)) {
			SzFileUtil.deleteRecords(szFileService.readFull(szFileId));
		}

		String processName = null;
		FPFileStatus status = null;

		if ("loadToDB".equals(action1)) {
			processName = "ParseSzFileProcess";
			status = fpFileService.getStatusByCodeAndModule(SzFile.PROCESSING_FILE_STATUS, moduleName);
		} else if ("fullDelete".equals(action1)) {
			processName = "DeleteSzFileProcess";
			status = fpFileService.getStatusByCodeAndModule(SzFile.DELETING_FILE_STATUS, moduleName);
		}

		if (processName != null) {

			szFileService.updateStatus(szFileIds, status);

			for (Long szFileId : szFileIds) {
				Map<Serializable, Serializable> contextVariables = CollectionUtils.map();
				contextVariables.put("fileId", szFileId);
				processManager.createProcess(processName, contextVariables);
			}
		}

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

	public void setSzFileIds(Collection<Long> szFileIds) {
		this.szFileIds = szFileIds;
	}

	public void setSzFileId(Long szFileId) {
		this.szFileId = szFileId;
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
