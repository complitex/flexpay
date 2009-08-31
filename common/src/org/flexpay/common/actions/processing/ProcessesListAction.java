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
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProcessesListAction extends FPActionWithPagerSupport<Process> implements InitializingBean {

	// form data
	private List<Process> processList;
	private Set<Long> objectIds = new HashSet<Long>();

	// sorters
	private ProcessSorterByName processSorterByName = new ProcessSorterByName();
	private ProcessSorterByStartDate processSorterByStartDate = new ProcessSorterByStartDate();
	private ProcessSorterByEndDate processSorterByEndDate = new ProcessSorterByEndDate();
	private ProcessSorterByState processSorterByState = new ProcessSorterByState();

	// filters
	private BeginDateFilter beginDateFilter = new BeginDateFilter();
	private EndDateFilter endDateFilter = new EndDateFilter();
	private ProcessStateFilter processStateFilter = new ProcessStateFilter();
	private ProcessNameFilter processNameFilter = new ProcessNameFilter();

	// process manager
	private ProcessManager processManager;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		initFilters();

		if (objectIds != null && objectIds.size() > 0) {
			processManager.deleteProcessInstances(objectIds);
		}

		processList = getProcessListMethod();
		return SUCCESS;
	}

	private void initFilters() {
		processNameFilter.loadAllProcessNames();
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

	// rendering utility methods
	public String getTranslation(ProcessState state) {
		return getText(ProcessStateObject.getByProcessState(state).getName());
	}

	public boolean resultsAreNotEmpty() {
		return !processList.isEmpty();
	}

	// form data
	public List<Process> getProcessList() {
		return processList;
	}

	public void setObjectIds(Set<Long> objectIds) {
		this.objectIds = objectIds;
	}

	// sorters
	public ProcessSorterByName getProcessSorterByName() {
		return processSorterByName;
	}

	public ProcessSorterByStartDate getProcessSorterByStartDate() {
		return processSorterByStartDate;
	}

	public ProcessSorterByEndDate getProcessSorterByEndDate() {
		return processSorterByEndDate;
	}

	public ProcessSorterByState getProcessSorterByState() {
		return processSorterByState;
	}

	// filters
	public BeginDateFilter getBeginDateFilter() {
		return beginDateFilter;
	}

	public EndDateFilter getEndDateFilter() {
		return endDateFilter;
	}

	public ProcessStateFilter getProcessStateFilter() {
		return processStateFilter;
	}

	public ProcessNameFilter getProcessNameFilter() {
		return processNameFilter;
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
