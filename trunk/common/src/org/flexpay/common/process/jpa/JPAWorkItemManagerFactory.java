package org.flexpay.common.process.jpa;

import org.drools.common.InternalKnowledgeRuntime;
import org.drools.process.instance.WorkItemManager;

public class JPAWorkItemManagerFactory extends org.drools.persistence.jpa.processinstance.JPAWorkItemManagerFactory {
	@Override
	public WorkItemManager createWorkItemManager(InternalKnowledgeRuntime kruntime) {
		return new JPAWorkItemManager(kruntime);
	}
}
