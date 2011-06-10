package org.flexpay.bti.action.building;

import org.flexpay.bti.process.BuildingAttributesImportJob;
import org.flexpay.common.action.FPFileActionSupport;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.util.DateUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

import static org.flexpay.common.util.CollectionUtils.map;

public class BuildingAttributesImportAction extends FPFileActionSupport {

	private BeginDateFilter beginDateFilter = new BeginDateFilter(DateUtil.now());

	private ProcessManager processManager;

	/**
	 * Perform action execution.
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return execution result code
	 * @throws Exception if failure occurs
	 */
	@NotNull
	@Override
	protected String doExecute() throws Exception {

		if (isNotSubmit()) {
			return SUCCESS;
		}

		if (fpFile.isNew()) {
			addActionError(getText("bti.error.building.attribute.import.no_file"));
			return SUCCESS;
		}
		if (!beginDateFilter.needFilter()) {
			addActionError(getText("bti.error.building.attribute.import.no_date"));
			return SUCCESS;
		}

		Map<String, Object> params = map();
		params.put(BuildingAttributesImportJob.PARAM_FILE_ID, fpFile.getId());
		params.put(BuildingAttributesImportJob.PARAM_IMPORT_DATE, beginDateFilter.getDate());

		processManager.startProcess("BuildingAttributesImport", params);
		addActionMessage(getText("bti.building.attribute.import.started"));
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

	public BeginDateFilter getBeginDateFilter() {
		return beginDateFilter;
	}

	public void setBeginDateFilter(BeginDateFilter beginDateFilter) {
		this.beginDateFilter = beginDateFilter;
	}

	@Required
	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}

}
