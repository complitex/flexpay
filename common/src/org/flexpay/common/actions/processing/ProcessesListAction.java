package org.flexpay.common.actions.processing;

import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.persistence.filter.EndDateFilter;
import org.flexpay.common.process.Process;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.ProcessState;
import org.flexpay.common.process.filter.ProcessNameFilter;
import org.flexpay.common.process.filter.ProcessStateFilter;
import org.flexpay.common.process.filter.ProcessStateObject;
import org.flexpay.common.process.sorter.*;
import org.flexpay.common.util.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;
import java.util.List;

public class ProcessesListAction extends FPActionWithPagerSupport<Process> implements InitializingBean {

	private List<Process> processes = CollectionUtils.list();

	private ProcessSorterByName processSorterByName = new ProcessSorterByName();
	private ProcessSorterByStartDate processSorterByStartDate = new ProcessSorterByStartDate();
	private ProcessSorterByEndDate processSorterByEndDate = new ProcessSorterByEndDate();
	private ProcessSorterByState processSorterByState = new ProcessSorterByState();

	private BeginDateFilter beginDateFilter = new BeginDateFilter();
	private EndDateFilter endDateFilter = new EndDateFilter();
	private ProcessStateFilter processStateFilter = new ProcessStateFilter();
	private ProcessNameFilter processNameFilter = new ProcessNameFilter();

	private ProcessManager processManager;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		processNameFilter.loadAllProcessNames();

		processes = getProcessListMethod();

		return SUCCESS;
	}

	@NotNull
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

	private List<Process> getProcessListMethod() {

		Date startFrom = beginDateFilter.dateIsNotEmpty() ? beginDateFilter.getDate() : null;
		Date endBefore = endDateFilter.dateIsNotEmpty() ? endDateFilter.getDate() : null;

		return processManager.getProcesses(getActiveSorter(), getPager(), startFrom, endBefore,
				processStateFilter.getProcessState(), processNameFilter.getSelectedName());
	}

	private ProcessSorter getActiveSorter() {

		ProcessSorter[] sorters = {processSorterByName, processSorterByStartDate, processSorterByEndDate, processSorterByState};
		for (ProcessSorter sorter : sorters) {
			if (sorter.isActivated()) {
				return sorter;
			}
		}

		return null;
	}

	public String getTranslation(ProcessState state) {
		return getText(ProcessStateObject.getByProcessState(state).getName());
	}

	public List<Process> getProcesses() {
		return processes;
	}

	public ProcessSorterByName getProcessSorterByName() {
		return processSorterByName;
	}

	public void setProcessSorterByName(ProcessSorterByName processSorterByName) {
		this.processSorterByName = processSorterByName;
	}

	public ProcessSorterByStartDate getProcessSorterByStartDate() {
		return processSorterByStartDate;
	}

	public void setProcessSorterByStartDate(ProcessSorterByStartDate processSorterByStartDate) {
		this.processSorterByStartDate = processSorterByStartDate;
	}

	public ProcessSorterByEndDate getProcessSorterByEndDate() {
		return processSorterByEndDate;
	}

	public void setProcessSorterByEndDate(ProcessSorterByEndDate processSorterByEndDate) {
		this.processSorterByEndDate = processSorterByEndDate;
	}

	public ProcessSorterByState getProcessSorterByState() {
		return processSorterByState;
	}

	public void setProcessSorterByState(ProcessSorterByState processSorterByState) {
		this.processSorterByState = processSorterByState;
	}

	public void setBeginDateFilter(BeginDateFilter beginDateFilter) {
		this.beginDateFilter = beginDateFilter;
	}

	public void setEndDateFilter(EndDateFilter endDateFilter) {
		this.endDateFilter = endDateFilter;
	}

	public void setProcessStateFilter(ProcessStateFilter processStateFilter) {
		this.processStateFilter = processStateFilter;
	}

	public void setProcessNameFilter(ProcessNameFilter processNameFilter) {
		this.processNameFilter = processNameFilter;
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
