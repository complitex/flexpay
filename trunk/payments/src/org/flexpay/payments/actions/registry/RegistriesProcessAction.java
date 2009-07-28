package org.flexpay.payments.actions.registry;

import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.payments.actions.CashboxCookieActionSupport;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RegistriesProcessAction extends FPActionSupport {

	private Set<Long> objectIds = new HashSet<Long>();

	private ProcessManager processManager;

	@NotNull
	public String doExecute() throws Exception {

		if (objectIds.isEmpty()) {
			// just redirect, no registries to process
			return REDIRECT_SUCCESS;
		}

		log.debug("About to execute RegistriesProcessAction");

		Map<Serializable, Serializable> contextVariables = CollectionUtils.map();
		contextVariables.put("registryIds", (Serializable) objectIds);

		processManager.createProcess("ProcessRegistryWorkflow", contextVariables);
		
		addActionError(getText("eirc.registry.processing_started"));

		return REDIRECT_SUCCESS;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	protected String getErrorResult() {
		return REDIRECT_SUCCESS;
	}

	public Set<Long> getObjectIds() {
		return objectIds;
	}

	public void setObjectIds(Set<Long> objectIds) {
		this.objectIds = objectIds;
	}

	@Required
	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}

}
