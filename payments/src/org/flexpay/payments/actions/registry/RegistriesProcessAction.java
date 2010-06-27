package org.flexpay.payments.actions.registry;

import org.flexpay.common.process.ProcessManager;
import org.flexpay.payments.actions.AccountantAWPActionSupport;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import static org.flexpay.common.util.CollectionUtils.map;
import static org.flexpay.common.util.CollectionUtils.set;

public class RegistriesProcessAction extends AccountantAWPActionSupport {

	private Set<Long> objectIds = set();

	private ProcessManager processManager;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (objectIds == null || objectIds.isEmpty()) {
			return SUCCESS;
		}

		for (Long registryId : objectIds) {
			Map<Serializable, Serializable> contextVariables = map();
			contextVariables.put("registryId", registryId);

			processManager.createProcess("ProcessingDBRegistryProcess2", contextVariables);
		}

		addActionMessage(getText("eirc.registry.processing_started"));

		return SUCCESS;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

	public void setObjectIds(Set<Long> objectIds) {
		this.objectIds = objectIds;
	}

	@Required
	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}

}
