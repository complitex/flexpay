package org.flexpay.eirc.process.registry;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.process.handler.FlexPayActionHandler;

import java.util.Map;


public class RemoveRegistryActionHandler extends FlexPayActionHandler {
	@Override
	public String execute2(Map<String, Object> parameters) throws FlexPayException {
		return RESULT_NEXT;
	}
}
