package org.flexpay.eirc.actions;

import org.flexpay.common.process.Process;
import org.flexpay.common.process.ProcessManager;

public class ProcessViewAction {
	
	private Process process;
	private ProcessManager processManager;
	
	public String execute() {
		process = processManager.getProcessInstanceInfo(process.getId());
		return "success";
	}
	
	/**
	 * @return the process
	 */
	public Process getProcess() {
		return process;
	}


	/**
	 * @param process the process to set
	 */
	public void setProcess(Process process) {
		this.process = process;
	}

	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}
}
