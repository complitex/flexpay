package org.flexpay.eirc.actions;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import org.flexpay.common.process.Process;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.ProcessStateComparator;

public class ProcessListAction {
	
	private List<Process> processList;
	private Set<Long> objectIds = new HashSet<Long>();
    private String submited = "";

    public String execute() {
        if (objectIds != null && objectIds.size() >0){
            ProcessManager.getInstance().deleteProcessInstanceList(objectIds);
        }
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

    public void setObjectIds(Set<Long> objectIds) {
        this.objectIds = objectIds;
    }

    public void setSubmited(String submited) {
        this.submited = submited;
    }
}
