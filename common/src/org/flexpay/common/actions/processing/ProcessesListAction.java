package org.flexpay.common.actions.processing;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.process.Process;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.ProcessStateComparator;
import org.flexpay.common.process.sorter.*;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class  ProcessesListAction extends FPActionSupport {

	// form data
	private List<Process> processList;
	private Set<Long> objectIds = new HashSet<Long>();

	// sorters
	private ProcessSorterByName processSorterByName = new ProcessSorterByName();
	private ProcessSorterByStartDate processSorterByStartDate = new ProcessSorterByStartDate();
	private ProcessSorterByEndDate processSorterByEndDate = new ProcessSorterByEndDate();
	private ProcessSorterByState processSorterByState = new ProcessSorterByState();
	private ProcessSorterByUser processSorterByUser = new ProcessSorterByUser();

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

		ProcessSorter activeSorter = getActiveSorter();

		List<Process> processes;
		if (activeSorter instanceof ProcessSorterByState) {
			processes = processManager.getProcesses();
			ProcessStateComparator stateComparator = new ProcessStateComparator();
			stateComparator.setOrder(activeSorter.getOrder());
			Collections.sort(processes, stateComparator);
		} else {
			processes = processManager.getProcesses(activeSorter);
		}

		return processes;
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

	// process manager
	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}
}
