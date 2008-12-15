package org.flexpay.eirc.actions.processing;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.process.Process;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.ProcessStateComparator;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProcessesListAction extends FPActionSupport {

	private List<Process> processList;
	private Set<Long> objectIds = new HashSet<Long>();
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
	protected String doExecute() throws Exception {
		if (objectIds != null && objectIds.size() > 0) {
			processManager.deleteProcessInstances(objectIds);
		}
		processList = getProcessListMethod();
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
	protected String getErrorResult() {
		return SUCCESS;
	}

	private List<Process> getProcessListMethod() {
		List<Process> processes = processManager.getProcesses();
		Collections.sort(processes, new ProcessStateComparator());
		return processes;
	}

	/**
	 * @return the processList
	 */
	public List<Process> getProcessList() {
		return processList;
	}

	public void setObjectIds(Set<Long> objectIds) {
		this.objectIds = objectIds;
	}

	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}
}
