package org.flexpay.common.process.dao;

import org.drools.runtime.process.WorkItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public interface WorkItemHandlerDao {

	/**
	 * Register work item handlers in system
	 *
	 * @param handlers work item handlers
	 */
	void registerWorkItemHandlers(@NotNull Map<String, WorkItemHandler> handlers);

	/**
	 * Register work item handler in system by specific names
	 *
	 * @param workItemHandler Work item handler
	 * @param workItemHandlerNames Names
	 */
	void registerWorkItemHandler(@NotNull WorkItemHandler workItemHandler, @NotNull List<String> workItemHandlerNames);

	/**
	 * Get registered work item handlers
	 *
	 * @return Work item handlers. Collection content  pair <Registered work item handler name, Work item handler>.
	 */
	Map<String, WorkItemHandler> getWorkItemHandlers();
}
