package org.flexpay.eirc.process.registry;

import org.apache.log4j.Logger;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.process.job.Job;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.service.SpRegistryService;
import org.flexpay.eirc.service.exchange.ServiceProviderFileProcessor;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class RegistryProcessEvent extends Job {

	private Logger log = Logger.getLogger(getClass());

	private SpRegistryService registryService;
	private ServiceProviderFileProcessor providerFileProcessor;

	@SuppressWarnings ({"unchecked"})
	public String execute(Map parameters) throws FlexPayException {
		Set<Long> objectIds = (Set<Long>) parameters.get("registryIds");

		try {
			Collection<SpRegistry> registries = registryService.findObjects(objectIds);
			providerFileProcessor.processRegistries(registries);
		} catch (Exception e) {
			log.warn("Parser exception", e);
			return RESULT_ERROR;
		}

		return RESULT_NEXT;
	}

	public void setRegistryService(SpRegistryService registryService) {
		this.registryService = registryService;
	}

	public void setProviderFileProcessor(ServiceProviderFileProcessor providerFileProcessor) {
		this.providerFileProcessor = providerFileProcessor;
	}
}
