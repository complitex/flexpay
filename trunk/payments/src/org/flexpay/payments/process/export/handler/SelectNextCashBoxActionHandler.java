package org.flexpay.payments.process.export.handler;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.process.handler.FlexPayActionHandler;

import java.util.Map;

import static org.flexpay.payments.process.export.ExportJobParameterNames.CURRENT_INDEX_CASH_BOX;
import static org.flexpay.payments.process.export.ExportJobParameterNames.CASH_BOXES;

public class SelectNextCashBoxActionHandler extends FlexPayActionHandler {

	public final static String RESULT_END = "end";

	@Override
	public String execute2(Map<String, Object> parameters) throws FlexPayException {
		Long[] cashPoints = (Long[])parameters.get(CASH_BOXES);
		if (cashPoints == null || cashPoints.length == 0) {
			return RESULT_END;
		}

		Integer index = (Integer)parameters.get(CURRENT_INDEX_CASH_BOX);

		if (index != null && ++index >= cashPoints.length) {
			return RESULT_END;
		} else if (index == null) {
			index = 0;
		}

		parameters.put(CURRENT_INDEX_CASH_BOX, index);
		log.debug("Current index cash box: {}, process id: {}",
				new Object[]{index, getProcessId()});

		return RESULT_NEXT;
	}
}
