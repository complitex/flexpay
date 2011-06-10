package org.flexpay.eirc.process.registry;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.process.handler.TaskHandler;
import org.flexpay.eirc.process.registry.helper.ProcessingRegistryFacade;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

public class ProcessingRegistryHandler extends TaskHandler {

	private ProcessingRegistryFacade facade;

	@Override
	public String execute(Map<String, Object> parameters) throws FlexPayException {

		return facade.processing(parameters);

	}

	@Required
	public void setFacade(ProcessingRegistryFacade facade) {
		this.facade = facade;
	}

}
