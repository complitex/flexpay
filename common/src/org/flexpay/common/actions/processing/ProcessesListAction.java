package org.flexpay.common.actions.processing;

import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.persistence.filter.EndDateFilter;
import org.flexpay.common.process.Process;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.ProcessState;
import org.flexpay.common.process.filter.ProcessStateFilter;
import org.flexpay.common.process.filter.ProcessStateObject;
import org.flexpay.common.process.sorter.*;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class  ProcessesListAction extends FPActionWithPagerSupport<Process> {

	// form data
	private List<Process> processList;
	private Set<Long> objectIds = new HashSet<Long>();

	// sorters
	private ProcessSorterByName processSorterByName = new ProcessSorterByName();
	private ProcessSorterByStartDate processSorterByStartDate = new ProcessSorterByStartDate();
	private ProcessSorterByEndDate processSorterByEndDate = new ProcessSorterByEndDate();
	private ProcessSorterByState processSorterByState = new ProcessSorterByState();
	private ProcessSorterByUser processSorterByUser = new ProcessSorterByUser();

	// filters
	private BeginDateFilter beginDateFilter = new BeginDateFilter();
	private EndDateFilter endDateFilter = new EndDateFilter();
	private ProcessStateFilter processStateFilter = new ProcessStateFilter();

	// process manager
	private ProcessManager processManager;

	/**
	 * {@inheritDoc}
	 */
	@NotNull
	protected String doExecute() throws Exception {
		
		if (objectIds != null && objectIds.size() > 0) {
			processManager.deleteProcessInstances(objectIds);
		}
		processList = getProcessListMethod();
		return SUCCESS;
	}

	/**
	 * {@inheritDoc}
	 */
	@NotNull
	protected String getErrorResult() {
		return SUCCESS;
	}

	private List<Process> getProcessListMethod() {

		// if start from date is not set it should be set to past infinite
		Date startFrom = beginDateFilter.getDate();
		if (dateIsNotSet(startFrom)) {
			startFrom = null;
		}
		Date endBefore = endDateFilter.getDate();
		if (dateIsNotSet(endBefore)) {
			endBefore = null;
		}

		return processManager.getProcesses(getActiveSorter(), getPager(), startFrom, endBefore, processStateFilter.getProcessState());
	}

	private boolean dateIsNotSet(Date date) {
		return date.equals(ApplicationConfig.getFutureInfinite());
	}

	private ProcessSorter getActiveSorter() {

		ProcessSorter[] sorters = { processSorterByName, processSorterByStartDate, processSorterByEndDate, processSorterByState, processSorterByUser };
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

	public ProcessSorterByUser getProcessSorterByUser() {
		return processSorterByUser;
	}

	public void setProcessSorterByUser(ProcessSorterByUser processSorterByUser) {
		this.processSorterByUser = processSorterByUser;
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

	// process manager
	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}
}
