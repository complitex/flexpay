package org.flexpay.common.process.dao;

import org.drools.command.SingleSessionCommandService;
import org.drools.command.impl.CommandBasedStatefulKnowledgeSession;
import org.drools.command.impl.KnowledgeCommandContext;
import org.drools.persistence.jpa.processinstance.JPAWorkItemManager;
import org.drools.runtime.EnvironmentName;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemHandler;
import org.drools.runtime.process.WorkItemManager;
import org.flexpay.common.process.audit.WorkItemCompleteLocker;
import org.flexpay.common.process.jpa.LockedManager;
import org.flexpay.common.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

public class WorkItemDaoImpl implements WorkItemDao {

	private Logger log = LoggerFactory.getLogger(getClass());

	private StatefulKnowledgeSession session;

	private EntityManagerFactory emf;

	@SuppressWarnings({"unchecked"})
	@Override
	public List<WorkItem> getWorkItemsWaiting() {

		EntityManager em = (EntityManager)session.getEnvironment().get(EnvironmentName.CMD_SCOPED_ENTITY_MANAGER);

		if (!em.isOpen()) {
			em = emf.createEntityManager();
		}

		Query processInstancesForEvent = em.createNamedQuery("WorkItemsWaiting");
		//processInstancesForEvent.setFlushMode(FlushModeType.COMMIT);

		List<Long> workItemIds = (List<Long>) processInstancesForEvent.getResultList();

		//em.getTransaction().commit();

		List<WorkItem> workItems = CollectionUtils.list();

		session.getEnvironment().set(EnvironmentName.CMD_SCOPED_ENTITY_MANAGER, em);
		WorkItemManager manager = getWorkItemManager();
		for (Long workItemId : workItemIds) {
			WorkItem workItem = ((JPAWorkItemManager) manager).getWorkItem(workItemId);
			workItems.add(workItem);
			log.debug("work item: {}", workItem);
		}

		return workItems;
	}

	private WorkItemManager getWorkItemManager() {
		return ((KnowledgeCommandContext)((SingleSessionCommandService)((CommandBasedStatefulKnowledgeSession)session).getCommandService()).getContext()).getWorkItemManager();
	}

	@Override
	public void completeWorkItem(long workItemId, Map<String, Object> results) {
		WorkItemManager manager = getWorkItemManager();
		log.debug("completeWorkItem class", manager.getClass());
		if (getWorkItemManager() instanceof LockedManager) {
			log.debug("completeWorkItem lock");
			((LockedManager)manager).lock();
			log.debug("completeWorkItem locked");
		}
		try {
			session.getWorkItemManager().completeWorkItem(workItemId, results);
		} finally {
			if (getWorkItemManager() instanceof LockedManager) {
				log.debug("completeWorkItem executed");
				((LockedManager)manager).unlock();
				log.debug("completeWorkItem unlocked");
			}
		}
	}

	@Override
	public void executeWorkItem(WorkItemHandler workItemHandler, WorkItem workItem) {
		workItemHandler.executeWorkItem(workItem, getWorkItemManager());
	}

	@Required
	public void setSession(StatefulKnowledgeSession session) {
		this.session = session;
	}

	@Required
	public void setEmf(EntityManagerFactory emf) {
		this.emf = emf;
	}
}
