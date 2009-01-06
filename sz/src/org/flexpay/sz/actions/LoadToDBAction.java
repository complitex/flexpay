package org.flexpay.sz.actions;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.sz.convert.SzFileUtil;
import org.flexpay.sz.service.SzFileService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

public class LoadToDBAction extends FPActionSupport {

	private Collection<Long> szFileIds;
	private String action1;
	private Long szFileId;

	private ProcessManager processManager;

	private SzFileService szFileService;

	@NotNull
	public String doExecute() throws Exception {

		log.debug("Action - {}; SzFileIds - {}", action1, szFileIds);

		if ("loadToDb".equals(action1)) {
			for (Long szFileId : szFileIds) {
				Map<Serializable, Serializable> contextVariables = CollectionUtils.map();
				contextVariables.put("fileId", szFileId);
				processManager.createProcess("ParseSzFileProcess", contextVariables);
			}
		} else if ("loadFromDb".equals(action1)) {
			SzFileUtil.loadFromDb(szFileService.readFull(szFileId));
		} else if ("deleteFromDb".equals(action1)) {
			SzFileUtil.deleteRecords(szFileService.readFull(szFileId));
		} else if ("fullDelete".equals(action1)) {
			for (Long szFileId : szFileIds) {
				Map<Serializable, Serializable> contextVariables = CollectionUtils.map();
				contextVariables.put("fileId", szFileId);
				processManager.createProcess("DeleteSzFileProcess", contextVariables);
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
	public void setSzFileService(SzFileService szFileService) {
		this.szFileService = szFileService;
	}

	@Required
	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}

}
