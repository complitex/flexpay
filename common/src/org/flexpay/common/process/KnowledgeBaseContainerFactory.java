package org.flexpay.common.process;

import org.drools.KnowledgeBase;
import org.flexpay.common.process.exception.ProcessDefinitionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class KnowledgeBaseContainerFactory implements KnowledgeBaseFactory {

	private static final Logger log = LoggerFactory.getLogger(KnowledgeBaseContainerFactory.class);

	private KnowledgeBase knowledgeBase;

	private ProcessDefinitionManager processDefinitionManager;

	public void deployProcessDefitions() throws ProcessDefinitionException {
		log.debug("Deploy process definitions");
		processDefinitionManager.deployProcessDefinitions();
	}

	@Override
	public KnowledgeBase getKnowledgeBase() {
		log.debug("get knowledge base");
		return knowledgeBase;
	}

	@Required
	public void setKnowledgeBase(KnowledgeBase knowledgeBase) {
		this.knowledgeBase = knowledgeBase;
	}

	@Required
	public void setProcessDefinitionManager(ProcessDefinitionManager processDefinitionManager) {
		this.processDefinitionManager = processDefinitionManager;
	}
}
