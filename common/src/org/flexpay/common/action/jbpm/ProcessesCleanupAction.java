package org.flexpay.common.action.jbpm;

import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.persistence.filter.EndDateFilter;
import org.flexpay.common.process.ProcessDefinitionManager;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.filter.ProcessNameFilter;
import org.flexpay.common.process.jobs.ProcessesCleanupJob;
import org.flexpay.common.process.persistence.ProcessInstance;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

import static org.flexpay.common.util.CollectionUtils.map;

public class ProcessesCleanupAction extends FPActionSupport implements InitializingBean {

	private BeginDateFilter beginDateFilter = new BeginDateFilter();
	private EndDateFilter endDateFilter = new EndDateFilter();
	private ProcessNameFilter processNameFilter = new ProcessNameFilter();

	private Long processId;

	private ProcessManager processManager;
	private ProcessDefinitionManager processDefinitionManager;

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
				addActionError("common.error.process.cleanup.need_filter");
			} else {
				Map<String, Object> params = map();
				params.put(ProcessesCleanupJob.PARAM_COMPLETE_BEGIN_TIME, beginDateFilter.getDate());
				params.put(ProcessesCleanupJob.PARAM_COMPLETE_END_TIME, endDateFilter.getDate());
				params.put(ProcessesCleanupJob.PARAM_DEFINITION_NAME, processNameFilter.getSelectedName());
				ProcessInstance processInstance = processManager.startProcess("ProcessesCleanupProcess", params);

				processId = processInstance != null? processInstance.getId(): null;

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
		processNameFilter.setProcessDefinitionManager(processDefinitionManager);
	}

	@Required
	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}

	@Required
	public void setProcessDefinitionManager(ProcessDefinitionManager processDefinitionManager) {
		this.processDefinitionManager = processDefinitionManager;
	}
}
