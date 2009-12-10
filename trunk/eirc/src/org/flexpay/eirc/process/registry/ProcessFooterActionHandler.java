package org.flexpay.eirc.process.registry;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.process.handler.FlexPayActionHandler;

import java.util.List;
import java.util.Map;

public class ProcessFooterActionHandler extends FlexPayActionHandler {
	@SuppressWarnings ({"unchecked"})
	@Override
	public String execute2(Map<String, Object> parameters) throws FlexPayException {
		log.debug("start action");
		
		List<String> messageFieldList = (List<String>)parameters.get(ProcessRegistryMessageActionHandler.PARAM_MESSAGE_FIELDS);
		if (messageFieldList == null) {
			processLog.error("Can`t get {} from parameters", ProcessRegistryMessageActionHandler.PARAM_MESSAGE_FIELDS);
			return RESULT_ERROR;
		}
		if (messageFieldList.size() < 2) {
			processLog.error("Message footer error, invalid number of fields");
			return RESULT_ERROR;
		}
		return RESULT_NEXT;
	}
}
