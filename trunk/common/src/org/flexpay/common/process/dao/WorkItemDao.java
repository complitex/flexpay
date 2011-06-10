package org.flexpay.common.process.dao;

import org.drools.process.instance.WorkItem;

import java.util.List;
import java.util.Map;

public interface WorkItemDao {

	List<WorkItem> getWorkItems();

	void completeWorkItem(long workItemId, Map<String, Object> results);
}
