package org.flexpay.common.process.processinstance;

import org.drools.common.InternalKnowledgeRuntime;
import org.flexpay.common.process.ProcessDefinitionManagerImpl;
import org.jbpm.persistence.processinstance.JPASignalManager;
import org.jbpm.process.instance.event.SignalManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JPASignalManagerFactory extends org.jbpm.persistence.processinstance.JPASignalManagerFactory {

	private static final Logger log = LoggerFactory.getLogger(JPASignalManagerFactory.class);

	@Override
	public SignalManager createSignalManager(InternalKnowledgeRuntime kruntime) {
		log.debug("createSignalManager");
		return new JPASignalManager(kruntime) {
			@Override
			public void signalEvent(String type, Object event) {
				log.debug("signalEvent by type");
				waitWhileProcessDefinitionWillLoad();
				super.signalEvent(type, event);	//To change body of overridden methods use File | Settings | File Templates.
			}

			@Override
			public void signalEvent(long processInstanceId, String type, Object event) {
				log.debug("signalEvent by processInstanceId");
				waitWhileProcessDefinitionWillLoad();
				super.signalEvent(processInstanceId, type, event);	//To change body of overridden methods use File | Settings | File Templates.
			}

			@Override
			public void internalSignalEvent(String type, Object event) {
				log.debug("internalSignalEvent");
				waitWhileProcessDefinitionWillLoad();
				super.internalSignalEvent(type, event);	//To change body of overridden methods use File | Settings | File Templates.
			}
		};
	}

	private void waitWhileProcessDefinitionWillLoad() {
		while (!ProcessDefinitionManagerImpl.PROCESS_DEFINITIONS_LOADED) {
			log.debug("Wait while process definition manager will load process definitions. Sleep 5 sec.");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				log.debug("Interrupted signal to event");
			}
		}
	}
}
