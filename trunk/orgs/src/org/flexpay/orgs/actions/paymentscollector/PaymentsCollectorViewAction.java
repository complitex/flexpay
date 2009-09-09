package org.flexpay.orgs.actions.paymentscollector;

import org.flexpay.common.actions.FPActionSupport;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.orgs.persistence.PaymentsCollector;
import org.flexpay.orgs.service.PaymentsCollectorService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class PaymentsCollectorViewAction extends FPActionSupport {

	private PaymentsCollector collector = new PaymentsCollector();

	private PaymentsCollectorService collectorService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		if (collector.isNew()) {
			log.error(getText("error.invalid_id"));
			addActionError(getText("error.invalid_id"));
			return REDIRECT_ERROR;
		}
		collector = collectorService.read(stub(collector));

		if (collector == null) {
			log.error(getText("common.object_not_selected"));
			addActionError(getText("common.object_not_selected"));
			return REDIRECT_ERROR;
		}

		return SUCCESS;
	}

	@NotNull
	@Override
	protected String getErrorResult() {
		return REDIRECT_ERROR;
	}

	public PaymentsCollector getCollector() {
		return collector;
	}

	public void setCollector(PaymentsCollector collector) {
		this.collector = collector;
	}

	@Required
	public void setCollectorService(PaymentsCollectorService collectorService) {
		this.collectorService = collectorService;
	}

}
