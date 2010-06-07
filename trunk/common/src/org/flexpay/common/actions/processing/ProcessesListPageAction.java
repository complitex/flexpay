package org.flexpay.common.actions.processing;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.persistence.filter.BeginTimeFilter;
import org.flexpay.common.persistence.filter.EndDateFilter;
import org.flexpay.common.persistence.filter.EndTimeFilter;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.filter.ProcessNameFilter;
import org.flexpay.common.process.filter.ProcessStateFilter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

import static org.flexpay.common.util.DateUtil.now;

public class ProcessesListPageAction extends FPActionSupport implements InitializingBean {

	private ProcessStateFilter processStateFilter = new ProcessStateFilter();
	private ProcessNameFilter processNameFilter = new ProcessNameFilter();
    private BeginDateFilter beginDateFilter = new BeginDateFilter(now());
    private EndDateFilter endDateFilter = new EndDateFilter(now());
    private BeginTimeFilter beginTimeFilter = new BeginTimeFilter(false);
    private EndTimeFilter endTimeFilter = new EndTimeFilter(false);

	private ProcessManager processManager;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		processNameFilter.loadProcessNames();

		return SUCCESS;
	}

	@NotNull
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

    @Override
	public void afterPropertiesSet() throws Exception {
		processNameFilter.setProcessManager(processManager);
	}

	public ProcessStateFilter getProcessStateFilter() {
		return processStateFilter;
	}

	public ProcessNameFilter getProcessNameFilter() {
		return processNameFilter;
	}

    public BeginDateFilter getBeginDateFilter() {
        return beginDateFilter;
    }

    public EndDateFilter getEndDateFilter() {
        return endDateFilter;
    }

    public BeginTimeFilter getBeginTimeFilter() {
        return beginTimeFilter;
    }

    public EndTimeFilter getEndTimeFilter() {
        return endTimeFilter;
    }

	@Required
	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}

}
