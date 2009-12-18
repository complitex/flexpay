package org.flexpay.eirc.process.registry;

import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.process.handler.FlexPayActionHandler;

import java.util.List;
import java.util.Map;

public class HasMoreRecordActionHandler extends FlexPayActionHandler {
	public static final String RANGE = "range";

	public static final String RESULT_CONTINUE = "continue";

	@SuppressWarnings ({"unchecked"})
	@Override
	public String execute2(Map<String, Object> parameters) throws FlexPayException {
		log.debug("start action");

		List<Long> recordsId = (List<Long>)parameters.get(StartRegistryProcessingActionHandler.RECORD_IDS);
		if (recordsId != null) {
			return !recordsId.isEmpty()? RESULT_CONTINUE: RESULT_NEXT;
		}

		FetchRange range = (FetchRange)parameters.get(RANGE);
		if (range == null) {
			processLog.error("Inner error");
			log.error("Can not find '{}' in process parameters", RANGE);
			return RESULT_ERROR;
		}
		range.nextPage();
		return range.hasMore()? RESULT_CONTINUE: RESULT_NEXT;
	}
}
