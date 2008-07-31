package org.flexpay.eirc.actions;

import java.util.Date;
import java.util.HashMap;

import org.flexpay.common.process.Process;
import org.flexpay.common.process.ProcessManager;

public class ProcessViewAction {
	
	private Process process;
	
	
	public String execute() {
		process = ProcessManager.getInstance().getProcessInastanceInfo(process.getId());
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
	
	

}
