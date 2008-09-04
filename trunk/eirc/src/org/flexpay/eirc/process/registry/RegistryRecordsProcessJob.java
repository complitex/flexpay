package org.flexpay.eirc.process.registry;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.process.job.Job;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.service.SpRegistryService;
import org.flexpay.eirc.service.exchange.RegistryProcessor;

import java.util.Map;
import java.util.Set;

public class RegistryRecordsProcessJob extends Job {

	private SpRegistryService registryService;
	private RegistryProcessor registryProcessor;

	@SuppressWarnings ({"unchecked"})
	public String execute(Map parameters) throws FlexPayException {
		Set<Long> recordIds = (Set<Long>) parameters.get("recordIds");
		Stub<SpRegistry> stub = (Stub<SpRegistry>) parameters.get("registryStub");

		try {
			SpRegistry registry = registryService.read(stub);
			if (registry == null) {
				log.warn("Invalid registry stub: " + stub);
				return RESULT_ERROR;
			}

			if (recordIds.isEmpty()) {
				return RESULT_NEXT;
			}

			registryProcessor.processRecords(registry, recordIds);
		} catch (Exception e) {
			log.error("Processing exception", e);
			return RESULT_ERROR;
		}

		return RESULT_NEXT;
	}

	public void setRegistryService(SpRegistryService registryService) {
		this.registryService = registryService;
	}

	public void setRegistryProcessor(RegistryProcessor registryProcessor) {
		this.registryProcessor = registryProcessor;
	}
}
