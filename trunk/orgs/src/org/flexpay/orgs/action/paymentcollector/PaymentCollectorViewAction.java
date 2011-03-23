package org.flexpay.orgs.action.paymentcollector;

import org.flexpay.common.action.FPActionSupport;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.service.PaymentCollectorService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import static org.flexpay.common.persistence.Stub.stub;

public class PaymentCollectorViewAction extends FPActionSupport {

	private PaymentCollector collector = new PaymentCollector();

	private PaymentCollectorService collectorService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		if (collector.isNew()) {
			log.error(getText("common.error.invalid_id"));
			addActionError(getText("common.error.invalid_id"));
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

	public PaymentCollector getCollector() {
		return collector;
	}

	public void setCollector(PaymentCollector collector) {
		this.collector = collector;
	}

	@Required
	public void setCollectorService(PaymentCollectorService collectorService) {
		this.collectorService = collectorService;
	}

}
