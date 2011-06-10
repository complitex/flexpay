package org.flexpay.common.process;

import org.drools.runtime.StatefulKnowledgeSession;

public interface StatefulKnowledgeSessionFactory {
	StatefulKnowledgeSession getSession() throws InterruptedException;
}
