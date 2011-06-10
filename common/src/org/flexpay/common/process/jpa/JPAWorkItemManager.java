package org.flexpay.common.process.jpa;

import org.apache.commons.lang.StringUtils;
import org.drools.common.InternalKnowledgeRuntime;
import org.drools.persistence.PersistenceContext;
import org.drools.persistence.PersistenceContextManager;
import org.drools.process.instance.WorkItem;
import org.drools.runtime.Environment;
import org.drools.runtime.EnvironmentName;
import org.drools.runtime.process.WorkItemHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

public class JPAWorkItemManager extends org.drools.persistence.jpa.processinstance.JPAWorkItemManager implements LockedManager {

	private static final Logger log = LoggerFactory.getLogger(JPAWorkItemManager.class);

	private InternalKnowledgeRuntime kruntime;
	//private EntityManagerFactory emf;

	public JPAWorkItemManager(InternalKnowledgeRuntime kruntime) {
		super(kruntime);
		this.kruntime = kruntime;
		log.debug("Init JPAWorkItemManager");
	}

	@Override
	public void internalAddWorkItem(WorkItem workItem) {
		log.debug("Add work item '{}' (process instance id '{}')", workItem.getId(), workItem.getProcessInstanceId());
		super.internalAddWorkItem(workItem);
	}

	@SuppressWarnings({"unchecked"})
	@Override
	public void registerWorkItemHandler(String workItemName, WorkItemHandler handler) {
		super.registerWorkItemHandler(workItemName, handler);

		Environment env = this.kruntime.getEnvironment();
		EntityManager em = (EntityManager) env.get(EnvironmentName.CMD_SCOPED_ENTITY_MANAGER);
		if (!em.isOpen()) {
			EntityManagerFactory emf = (EntityManagerFactory) env.get(EnvironmentName.ENTITY_MANAGER_FACTORY);
			em = emf.createEntityManager();
			env.set(EnvironmentName.CMD_SCOPED_ENTITY_MANAGER, em);
		}
		Query workItemsQuery = em.createNamedQuery("WorkItemsWaiting");

		List<Long> workItemIds = (List<Long>) workItemsQuery.getResultList();

		for (Long workItemId : workItemIds) {
			WorkItem workItem = getWorkItem(workItemId);
			if (StringUtils.equals(workItem.getName(), workItemName)) {
				handler.executeWorkItem(workItem, this);
				log.debug("execute work item: {}", workItem);
			}
		}
	}

	@Override
	public void completeWorkItem(long id, Map<String, Object> results) {
		log.debug("completeWorkItem({}, {}):", id, results);

		Environment env = this.kruntime.getEnvironment();
		log.debug("drools.workItemManagerFactory={}", env.get("drools.workItemManagerFactory"));
//        EntityManager em = (EntityManager) env.get(EnvironmentName.CMD_SCOPED_ENTITY_MANAGER);
        PersistenceContext context = ((PersistenceContextManager) env.get( EnvironmentName.PERSISTENCE_CONTEXT_MANAGER )).getCommandScopedPersistenceContext();
		log.debug("drools.persistence.PersistenceContextManager: {}, context: {}", env.get( EnvironmentName.PERSISTENCE_CONTEXT_MANAGER ), context);

		try {
//			WorkItem workItemBefore = getWorkItem(id);
//			ProcessInstance processInstanceBefore = kruntime.getProcessInstance(workItemBefore.getProcessInstanceId());

			super.completeWorkItem(id, results);

//			WorkItem workItemAfter = getWorkItem(id);
//			ProcessInstance processInstanceAfter = kruntime.getProcessInstance(workItemAfter.getProcessInstanceId());

//			log.debug("workItemBefore={}\n processInstanceBefore={}\n workItemAfter={}\n processInstanceAfter={}",
//					new Object[]{workItemBefore, processInstanceBefore, workItemAfter, processInstanceAfter});
		} catch (RuntimeException ex) {
			log.error("Error completed work item", ex);
			throw ex;
		}


	}

	@Override
	public void lock() {

	}

	@Override
	public void unlock() {

	}
}
