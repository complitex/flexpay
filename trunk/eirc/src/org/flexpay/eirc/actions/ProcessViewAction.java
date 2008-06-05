package org.flexpay.eirc.actions;

import java.util.Date;
import java.util.HashMap;

import org.flexpay.common.process.Process;

public class ProcessViewAction {
	
	private Process process;
	
	
	public String execute() {
		process = getProcess(process.getId());
		
		
		return "success";
	}
	
	private Process getProcess(Long id) {
		// TODO get real process
		Process process = new Process();
		process.setId(id);
		process.setLogFileName("logFileName");
		process.setProcess_end_date(new Date());
		process.setProcess_start_date(new Date());
		process.setProcessDefenitionVersion(1);
		process.setProcessDefinitionName("processDefinitionName");
		process.setProcessInstaceId(1);
		HashMap parameters = new HashMap();
        parameters.put("key1", "val1");
        parameters.put("key2", "val2");
        parameters.put("key3", "val3");
        process.setParameters(parameters);
		
		return process;
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
