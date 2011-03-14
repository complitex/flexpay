package org.flexpay.common.action.jbpm;

import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.persistence.filter.EndDateFilter;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.filter.ProcessNameFilter;
import org.flexpay.common.process.jobs.ProcessesCleanupJob;
import static org.flexpay.common.util.CollectionUtils.map;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

import java.io.Serializable;
import java.util.Map;

public class ProcessesCleanupAction extends FPActionSupport implements InitializingBean {

	private BeginDateFilter beginDateFilter = new BeginDateFilter();
	private EndDateFilter endDateFilter = new EndDateFilter();
	private ProcessNameFilter processNameFilter = new ProcessNameFilter();

	private Long processId;

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

		processNameFilter.loadProcessNames();

		if (isSubmit()) {
			log.debug("Form submit!");
			if (!haveFilterSet()) {
				addActionError("error.common.process.cleanup.need_filter");
			} else {
				Map<Serializable, Serializable> params = map();
				params.put(ProcessesCleanupJob.PARAM_COMPLETE_BEGIN_TIME, beginDateFilter.getDate());
				params.put(ProcessesCleanupJob.PARAM_COMPLETE_END_TIME, endDateFilter.getDate());
				params.put(ProcessesCleanupJob.PARAM_DEFINITION_NAME, processNameFilter.getSelectedName());
				processId = processManager.createProcess("ProcessesCleanupProcess", params);

				log.debug("Processes cleanup launched");
				addActionMessage(getText("common.process.cleanup.started"));
				return REDIRECT_SUCCESS;
			}
		}
		return SUCCESS;
	}

	private boolean haveFilterSet() {
		return beginDateFilter.needFilter() || endDateFilter.needFilter() || processNameFilter.needFilter();
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

	public EndDateFilter getEndDateFilter() {
		return endDateFilter;
	}

	public void setEndDateFilter(EndDateFilter endDateFilter) {
		this.endDateFilter = endDateFilter;
	}

	public ProcessNameFilter getProcessNameFilter() {
		return processNameFilter;
	}

	public void setProcessNameFilter(ProcessNameFilter processNameFilter) {
		this.processNameFilter = processNameFilter;
	}

	public Long getProcessId() {
		return processId;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		processNameFilter.setProcessManager(processManager);
	}

	@Required
	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}
}
