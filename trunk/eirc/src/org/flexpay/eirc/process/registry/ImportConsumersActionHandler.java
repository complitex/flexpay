package org.flexpay.eirc.process.registry;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryStatus;
import org.flexpay.common.persistence.registry.workflow.RegistryWorkflowManager;
import org.flexpay.common.persistence.registry.workflow.TransitionNotAllowed;
import org.flexpay.common.process.handler.FlexPayActionHandler;
import org.flexpay.common.service.RegistryService;
import org.flexpay.common.service.RegistryStatusService;
import org.flexpay.eirc.persistence.exchange.ProcessingContext;
import org.flexpay.eirc.process.registry.error.HandleError;
import org.flexpay.eirc.service.exchange.RegistryProcessor;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

public class ImportConsumersActionHandler extends FlexPayActionHandler {
	public final static String RESULT_END = "end";

	public static final String REGISTRY_ID = "registryId";

	private RegistryProcessor processor;
	private RegistryService registryService;
	private RegistryWorkflowManager registryWorkflowManager;
	private HandleError handleError;
	private RegistryStatusService statusService;

	@Override
	public String execute2(Map<String, Object> parameters) throws FlexPayException {
		log.debug("start action");

		Long registryId = (Long)parameters.get(REGISTRY_ID);
		if (registryId == null) {
			log.error("Can not find '{}' in process parameters", REGISTRY_ID);
			processLog.error("Inner error");
			return RESULT_END;
		}
		Registry registry = registryService.readWithContainers(new Stub<Registry>(registryId));
		if (registry == null) {
			log.error("Can not find registry '{}'", registryId);
			processLog.error("Inner error");
			return RESULT_END;
		}

		ProcessingContext context = new ProcessingContext();
		context.setRegistry(registry);

		if (registry.getRegistryStatus() != null &&
				(registry.getRegistryStatus().getCode() == RegistryStatus.PROCESSED_IMPORT_CONSUMER ||
				registry.getRegistryStatus().getCode() == RegistryStatus.PROCESSED_IMPORT_CONSUMER_WITH_ERROR)) {
			log.debug("Consumer was imported from registry {}. Skip this step.", registryId);
			processLog.debug("Consumer was imported. Skip this step.");
			return RESULT_NEXT;
		}

		try {
			registryWorkflowManager.setNextSuccessStatus(registry);
			processor.importConsumers(context);
			registryWorkflowManager.setNextSuccessStatus(registry);

			return RESULT_NEXT;
		} catch (Throwable t) {
			try {
				handleError.handleError(t, context);
			} catch (Exception e) {
				log.error("Inner error", e);
			}
		}
		try {
			registryWorkflowManager.setNextSuccessStatus(registry);
		} catch (TransitionNotAllowed transitionNotAllowed) {
			log.error("Inner error", transitionNotAllowed);
		}
		return RESULT_ERROR;
	}

	@Required
	public void setProcessor(RegistryProcessor processor) {
		this.processor = processor;
	}

	@Required
	public void setRegistryService(RegistryService registryService) {
		this.registryService = registryService;
	}

	@Required
	public void setRegistryWorkflowManager(RegistryWorkflowManager registryWorkflowManager) {
		this.registryWorkflowManager = registryWorkflowManager;
	}

	@Required
	public void setHandleError(HandleError handleError) {
		this.handleError = handleError;
	}

	@Required
	public void setStatusService(RegistryStatusService statusService) {
		this.statusService = statusService;
	}
}
