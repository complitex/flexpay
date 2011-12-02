package org.flexpay.common.process.dao;

import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemHandler;

import java.util.List;
import java.util.Map;

public interface WorkItemDao {

	List<WorkItem> getWorkItemsWaiting();

	void completeWorkItem(long workItemId, Map<String, Object> results);

	void executeWorkItem(WorkItemHandler workItemHandler, WorkItem workItem);
}
