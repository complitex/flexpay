package org.flexpay.common.process.dao;

import org.drools.SessionConfiguration;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.WorkItemHandler;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;
import java.util.Map;

import static org.flexpay.common.util.CollectionUtils.list;

public class WorkItemHandlerDaoImpl implements WorkItemHandlerDao {

	private static final Logger log = LoggerFactory.getLogger(WorkItemHandlerDaoImpl.class);

	private StatefulKnowledgeSession session;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, WorkItemHandler> getWorkItemHandlers() {
		return ((SessionConfiguration)session.getSessionConfiguration()).getWorkItemHandlers();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerWorkItemHandlers(@NotNull Map<String, WorkItemHandler> handlers) {
		for (Map.Entry<String, WorkItemHandler> workItemHandlerEntry : handlers.entrySet()) {
			registerWorkItemHandler(workItemHandlerEntry.getValue(), list(workItemHandlerEntry.getKey()));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerWorkItemHandler(@NotNull WorkItemHandler workItemHandler, @NotNull List<String> workItemHandlerNames) {
		for (String workItemHandlerName : workItemHandlerNames) {
			log.debug("Register work item by name '{}'", workItemHandlerName);
			session.getWorkItemManager().registerWorkItemHandler(workItemHandlerName, workItemHandler);
			((SessionConfiguration)session.getSessionConfiguration()).getWorkItemHandlers().put(workItemHandlerName, workItemHandler);
		}
	}

	@Required
	public void setSession(StatefulKnowledgeSession session) {
		this.session = session;
	}
}
