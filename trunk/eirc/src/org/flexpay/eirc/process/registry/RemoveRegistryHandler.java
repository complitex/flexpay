package org.flexpay.eirc.process.registry;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.process.handler.TaskHandler;

import java.util.Map;


public class RemoveRegistryHandler extends TaskHandler {
	@Override
	public String execute(Map<String, Object> parameters) throws FlexPayException {
		return RESULT_NEXT;
	}
}
