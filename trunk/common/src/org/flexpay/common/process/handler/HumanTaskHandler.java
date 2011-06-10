package org.flexpay.common.process.handler;

import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemHandler;
import org.drools.runtime.process.WorkItemManager;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HumanTaskHandler implements WorkItemHandler {

    private Map<Long, WorkItem> workItems = Collections.synchronizedMap(new HashMap<Long, WorkItem>());

    public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
        workItems.remove(workItem.getProcessInstanceId());
    }

    public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
        workItems.put(workItem.getProcessInstanceId(), workItem);
    }

	/**
	 * Get item of processes
	 *
	 * @return Collection content pair <process Id,work item>
	 */
	@NotNull
	public Map<Long, WorkItem> getWorkItems() {
		return workItems;
	}

}

