package org.flexpay.eirc.actions.registry;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.util.CollectionUtils;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.eirc.persistence.SpRegistry;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public class ProcessRegistryRecordsAction extends FPActionSupport {

	private Set<Long> objectIds = CollectionUtils.set();
	private SpRegistry registry = new SpRegistry();

	private ProcessManager processManager;

	@NotNull
	public String doExecute() throws Exception {

		log.debug("About to execute ProcessRegistryRecordsAction");

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

	public SpRegistry getRegistry() {
		return registry;
	}

	public void setRegistry(SpRegistry registry) {
		this.registry = registry;
	}

	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}
}
