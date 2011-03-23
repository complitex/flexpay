package org.flexpay.eirc.process.registry;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.workflow.RegistryWorkflowManager;
import org.flexpay.common.persistence.registry.workflow.TransitionNotAllowed;
import org.flexpay.common.process.handler.FlexPayActionHandler;
import org.flexpay.common.service.RegistryService;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

public class StartRegistryProcessingActionHandler extends FlexPayActionHandler {

	public static final String REGISTRY_ID = "registryId";
	public static final String RECORD_IDS = "recordIds";

	private RegistryService registryService;
	private RegistryWorkflowManager registryWorkflowManager;

	@Override
	public String execute2(Map<String, Object> parameters) throws FlexPayException {
		log.debug("start action");

		Long registryId = (Long)parameters.get(REGISTRY_ID);
		if (registryId == null) {
			log.error("Can not find '{}' in process parameters", REGISTRY_ID);
			processLog.error("Inner error");
			return RESULT_ERROR;
		}
		Registry registry = registryService.readWithContainers(new Stub<Registry>(registryId));
		if (registry == null) {
			log.error("Can not find registry '{}'", registryId);
			processLog.error("Inner error");
			return RESULT_ERROR;
		}
		try {
			registryWorkflowManager.startProcessing(registry);
		} catch (TransitionNotAllowed transitionNotAllowed) {
			log.error("Inner error", transitionNotAllowed);
			processLog.error("Inner error");
			return RESULT_ERROR;
		}

		return RESULT_NEXT;
	}

	@Required
	public void setRegistryService(RegistryService registryService) {
		this.registryService = registryService;
	}

	@Required
	public void setRegistryWorkflowManager(RegistryWorkflowManager registryWorkflowManager) {
		this.registryWorkflowManager = registryWorkflowManager;
	}
}
