package org.flexpay.eirc.actions;

import java.util.Collections;
import java.util.List;

import org.flexpay.common.process.Process;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.ProcessStateComparator;

public class ProcessListAction {
	
	private List<Process> processList;
	
	public String execute() {
		processList = getProcessListMethod();
		
		return "success";
	}
	
	private List<Process> getProcessListMethod() {
		List<Process> processes = ProcessManager.getInstance().getProcessList();
        Collections.sort(processes, new ProcessStateComparator());
      
        
        return processes;
	}

	/**
	 * @return the processList
	 */
	public List<Process> getProcessList() {
		return processList;
	}

}
