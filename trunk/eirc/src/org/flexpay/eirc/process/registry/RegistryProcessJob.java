package org.flexpay.eirc.process.registry;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.process.job.Job;
import org.flexpay.common.service.RegistryService;
import org.flexpay.eirc.service.exchange.RegistryProcessor;
import org.springframework.beans.factory.annotation.Required;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class RegistryProcessJob extends Job {

	private RegistryService registryService;
	private RegistryProcessor registryProcessor;

	@SuppressWarnings ({"unchecked"})
	public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {
		Set<Long> objectIds = (Set<Long>) parameters.get("registryIds");

		try {
			Collection<Registry> registries = registryService.findObjects(objectIds);
			registryProcessor.registriesProcess(registries);
		} catch (Exception e) {
			log.warn("Processing exception", e);
			return RESULT_ERROR;
		}

		return RESULT_NEXT;
	}

	@Required
	public void setRegistryService(RegistryService registryService) {
		this.registryService = registryService;
	}

	@Required
	public void setRegistryProcessor(RegistryProcessor registryProcessor) {
		this.registryProcessor = registryProcessor;
	}

}
