package org.flexpay.payments.process.export.job;

import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.service.CashboxService;
import org.springframework.beans.factory.annotation.Required;

import java.io.Serializable;
import java.util.Map;

import static org.flexpay.payments.process.export.ExportJobParameterNames.CASHBOXES;
import static org.flexpay.payments.process.export.ExportJobParameterNames.CURRENT_INDEX_CASHBOX;

public class SendSignalToCashboxParentJob extends SendSignalToProcessJob {

	private CashboxService cashboxService;

	@Override
	public Long getProcessId(Map<Serializable, Serializable> parameters) {
		Integer index = (Integer) parameters.get(CURRENT_INDEX_CASHBOX);
		Long[] cashboxes = (Long[]) parameters.get(CASHBOXES);

		Cashbox cashbox = cashboxService.read(new Stub<Cashbox>(cashboxes[index]));

		if (cashbox == null) {
			log.error("Cashbox {} did not find", cashboxes[index]);
			return null;
		}

		return cashbox.getPaymentPoint().getTradingDayProcessInstanceId();

	}

	@Required
	public void setCashboxService(CashboxService cashboxService) {
		this.cashboxService = cashboxService;
	}
}
