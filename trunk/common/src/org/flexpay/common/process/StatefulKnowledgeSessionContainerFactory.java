package org.flexpay.common.process;

import org.drools.runtime.StatefulKnowledgeSession;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class StatefulKnowledgeSessionContainerFactory implements StatefulKnowledgeSessionFactory {

	private static final Logger log = LoggerFactory.getLogger(StatefulKnowledgeSessionContainerFactory.class);

	private static StatefulKnowledgeSession ksession = null;

	@Override
	@NotNull
	public StatefulKnowledgeSession getSession() throws InterruptedException {
		return ksession;
	}

	@Required
	public void setSession(StatefulKnowledgeSession ksession) {
		StatefulKnowledgeSessionContainerFactory.ksession = ksession;
	}
}
