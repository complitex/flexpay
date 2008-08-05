package org.flexpay.eirc.util.selftest;

import org.flexpay.common.process.ProcessManager;
import static org.flexpay.common.util.CollectionUtils.ar;


// TODO add to spring configuration
public class ProcessDefinitionsChecker {

	private ProcessManager processManager;
	private String[] definitionNames = ar("ParseRegistryProcess", "ProcessRegistryWorkflow");

	public void doSelfTesting() throws Exception {

		for (String name : definitionNames) {
			if (!processManager.hasProcessDefinition(name)) {
				processManager.deployProcessDefinition(name, true);
			}
		}
	}

	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}
}
