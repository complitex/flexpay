package org.flexpay.payments.action.registry;

import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.payments.action.AccountantAWPActionSupport;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import static org.flexpay.common.util.CollectionUtils.map;
import static org.flexpay.common.util.CollectionUtils.set;

public class RegistryRecordsProcessAction extends AccountantAWPActionSupport {

	private Set<Long> objectIds = set();
	private Registry registry = new Registry();

	private ProcessManager processManager;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		Map<Serializable, Serializable> contextVariables = map();
		contextVariables.put("recordIds", (Serializable) CollectionUtils.list(objectIds));
		contextVariables.put("registryId", registry.getId());

		processManager.createProcess("ProcessingDBRegistryProcess2", contextVariables);

		addActionMessage(getText("payments.registry.records.processing_started"));

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

	public void setRegistry(Registry registry) {
		this.registry = registry;
	}

	@Required
	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}

}
