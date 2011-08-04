package org.flexpay.common.drools.utils;

import org.drools.common.InternalKnowledgeRuntime;
import org.drools.process.instance.WorkItemManager;
import org.flexpay.common.process.jpa.JPAWorkItemManager;
import org.flexpay.common.process.jpa.JPAWorkItemManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class TestJPAWorkItemManagerFactory extends JPAWorkItemManagerFactory {

	private static final Logger log = LoggerFactory.getLogger(TestJPAWorkItemManagerFactory.class);

	@Override
	public WorkItemManager createWorkItemManager(InternalKnowledgeRuntime kruntime) {
		return new JPAWorkItemManager(kruntime) {
			@Override
			public void completeWorkItem(long id, Map<String, Object> results) {
				log.debug("complete test work item");
				System.out.println("complete test work item");
				super.completeWorkItem(id, results);
				log.debug("completed test work item");
			}

			@Override
			public void lock() {
				WorkItemCompleteLocker.lock();
			}

			@Override
			public void unlock() {
				WorkItemCompleteLocker.unlock();
			}
		};
	}
}
