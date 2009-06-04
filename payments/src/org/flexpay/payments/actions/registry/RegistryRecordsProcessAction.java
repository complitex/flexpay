package org.flexpay.payments.actions.registry;

import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.payments.actions.CashboxCookieActionSupport;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public class RegistryRecordsProcessAction extends CashboxCookieActionSupport {

	private Set<Long> objectIds = CollectionUtils.set();
	private Registry registry = new Registry();

	private ProcessManager processManager;

	@NotNull
	public String doExecute() throws Exception {

		log.debug("About to execute RegistryRecordsProcessAction");

		Map<Serializable, Serializable> contextVariables = CollectionUtils.map();
		contextVariables.put("recordIds", (Serializable) objectIds);
		contextVariables.put("registryStub", stub(registry));

		processManager.createProcess("ProcessRegistryRecordsWorkflow", contextVariables);

		addActionError(getText("eirc.registry.records.processing_started"));

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
		return REDIRECT_ERROR;
	}

	public Set<Long> getObjectIds() {
		return objectIds;
	}

	public void setObjectIds(Set<Long> objectIds) {
		this.objectIds = objectIds;
	}

	public Registry getRegistry() {
		return registry;
	}

	public void setRegistry(Registry registry) {
		this.registry = registry;
	}

	@Required
	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}

}
