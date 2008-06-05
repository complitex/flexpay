package org.flexpay.eirc.actions;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.flexpay.common.process.Process;

public class ProcessListAction {
	
	private List<Process> processList;
	
	public String execute() {
		processList = getProcessListMethod();
		
		return "success";
	}
	
	private List<Process> getProcessListMethod() {
		//List<Process> processList = ProcessManager.getInstance().getProcessList();
		List<Process> processList = new ArrayList<Process>();
		Process process = new Process();
        process.setProcessDefinitionName("def_name1");
        process.setProcess_end_date(new Date());
        process.setProcess_start_date(new Date());
        process.setProcessDefenitionVersion(0);
        HashMap parameters = new HashMap();
        parameters.put("key1", "val1");
        process.setParameters(parameters);
        processList.add(process);
        
        process = new Process();
        process.setProcessDefinitionName("def_name2");
        process.setProcess_end_date(new Date());
        process.setProcess_start_date(new Date());
        process.setProcessDefenitionVersion(0);
        parameters = new HashMap();
        parameters.put("key2", "val2");
        process.setParameters(parameters);
        processList.add(process);
        
        return processList;
	}

	/**
	 * @return the processList
	 */
	public List<Process> getProcessList() {
		return processList;
	}

}
