package org.flexpay.eirc.process.registry;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.process.handler.FlexPayActionHandler;
import org.flexpay.common.service.RegistryService;
import org.flexpay.eirc.persistence.exchange.ProcessingContext;
import org.flexpay.eirc.process.registry.error.HandleError;
import org.flexpay.eirc.service.exchange.RegistryProcessor;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

public class ProcessRegistryHeaderActionHandler extends FlexPayActionHandler {

	private RegistryProcessor processor;
	private RegistryService registryService;
	private HandleError handleError;

	@Override
	public String execute2(Map<String, Object> parameters) throws FlexPayException {
		log.debug("start action");

		Long registryId = (Long)parameters.get(StartRegistryProcessingActionHandler.REGISTRY_ID);
		if (registryId == null) {
			log.error("Can not find '{}' in process parameters", StartRegistryProcessingActionHandler.REGISTRY_ID);
			processLog.error("Inner error");
			return RESULT_ERROR;
		}
		Registry registry = registryService.read(new Stub<Registry>(registryId));
		if (registry == null) {
			log.error("Can not find registry '{}'", registryId);
			processLog.error("Inner error");
			return RESULT_ERROR;
		}

		ProcessingContext context = new ProcessingContext();
		context.setRegistry(registry);

		try {
//			registryWorkflowManager.setNextSuccessStatus(registry);
			processor.processHeader(context);

			return RESULT_NEXT;
		} catch (Throwable t) {
			try {
				handleError.handleError(t, context);
			} catch (Exception e) {
				log.error("Inner error", e);
			}
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
	public void setHandleError(HandleError handleError) {
		this.handleError = handleError;
	}
}
