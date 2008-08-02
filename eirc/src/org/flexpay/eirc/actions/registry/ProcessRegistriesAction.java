package org.flexpay.eirc.actions.registry;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.util.CollectionUtils;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ProcessRegistriesAction extends FPActionSupport {

	private Set<Long> objectIds = new HashSet<Long>();

	private ProcessManager processManager;

	public String doExecute() throws Exception {

		if (objectIds.isEmpty()) {
			// just redirect, no registries to process
			return SUCCESS;
		}

		log.debug("About to execute ProcessRegistriesAction");

		Map<Serializable, Serializable> contextVariables = CollectionUtils.map();
		contextVariables.put("registryIds", (Serializable) objectIds);

		processManager.createProcess("ProcessRegistryWorkflow", contextVariables);

		return SUCCESS;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	protected String getErrorResult() {
		return SUCCESS;
	}

	public Set<Long> getObjectIds() {
		return objectIds;
	}

	public void setObjectIds(Set<Long> objectIds) {
		this.objectIds = objectIds;
	}

	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}
}
