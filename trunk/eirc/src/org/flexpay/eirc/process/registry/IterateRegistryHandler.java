package org.flexpay.eirc.process.registry;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.process.handler.TaskHandler;
import org.flexpay.eirc.process.registry.helper.ParseRegistryFacade;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Transactional (readOnly = true)
public class IterateRegistryHandler extends TaskHandler {

	private ParseRegistryFacade facade;

	@Override
	public String execute(Map<String, Object> parameters) throws FlexPayException {

		return facade.parse(parameters);

	}

	@Required
	public void setFacade(ParseRegistryFacade facade) {
		this.facade = facade;
	}
}
