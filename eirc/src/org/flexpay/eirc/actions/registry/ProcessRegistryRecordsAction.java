package org.flexpay.eirc.actions.registry;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.persistence.SpRegistryRecord;
import org.flexpay.eirc.service.SpRegistryRecordService;
import org.flexpay.eirc.service.SpRegistryService;
import org.flexpay.eirc.service.exchange.ServiceProviderFileProcessor;
import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ProcessRegistryRecordsAction extends FPActionSupport {

	private Set<Long> objectIds = new HashSet<Long>();
	private SpRegistry registry = new SpRegistry();

	private SpRegistryService registryService;
	private SpRegistryRecordService registryRecordService;
	private ServiceProviderFileProcessor providerFileProcessor;

	public String execute() throws Exception {

		log.debug("About to execute ProcessRegistryRecordsAction");

		registry = registryService.read(registry.getId());
		if (registry == null) {
			addActionError(getText("error.eirc.invalid_registry_id"));
			return ERROR;
		}

		if (objectIds.isEmpty()) {
			return SUCCESS;
		}

		try {
			Collection<SpRegistryRecord> records = registryRecordService.findObjects(registry, objectIds);
			providerFileProcessor.processRecords(registry, records);
		} catch (Exception e) {
			addActionError(e.getMessage());
		}

		return SUCCESS;
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

	public void setRegistryRecordService(SpRegistryRecordService registryRecordService) {
		this.registryRecordService = registryRecordService;
	}

	public void setProviderFileProcessor(ServiceProviderFileProcessor providerFileProcessor) {
		this.providerFileProcessor = providerFileProcessor;
	}

	public void setRegistryService(SpRegistryService registryService) {
		this.registryService = registryService;
	}
}
