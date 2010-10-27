package org.flexpay.payments.process.export.handler;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.process.handler.FlexPayActionHandler;

import java.util.Map;

import static org.flexpay.payments.process.export.ExportJobParameterNames.PAYMENT_POINTS;
import static org.flexpay.payments.process.export.ExportJobParameterNames.CURRENT_INDEX_PAYMENT_POINT;

public class SelectNextPaymentPointActionHandler extends FlexPayActionHandler {

	public final static String RESULT_END = "end";

	@Override
	public String execute2(Map<String, Object> parameters) throws FlexPayException {
		int[][] paymentPoints = (int[][])parameters.get(PAYMENT_POINTS);
		if (paymentPoints == null || paymentPoints.length == 0) {
			return RESULT_END;
		}

		Integer index = (Integer)parameters.get(CURRENT_INDEX_PAYMENT_POINT);

		if (index != null && ++index >= paymentPoints.length) {
			return RESULT_END;
		} else if (index == null) {
			index = 0;
		}

		parameters.put(CURRENT_INDEX_PAYMENT_POINT, index);
		log.debug("Current index payment point: {}, process id: {}",
				new Object[]{index, getProcessId()});

		return RESULT_NEXT;
	}
}
