package org.flexpay.payments.action.reports;

import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.payments.action.AccountantAWPActionSupport;
import org.flexpay.payments.service.OperationService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import static org.flexpay.common.util.SecurityUtil.isAuthenticationGranted;
import static org.flexpay.payments.service.Roles.PAYMENTS_DEVELOPER;

public class AccBlankOperationsReportAction extends AccountantAWPActionSupport {

	private Long blankOperationsCount;

	private OperationService operationService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		PaymentCollector paymentCollector = getPaymentCollector();
		if (isAuthenticationGranted(PAYMENTS_DEVELOPER)) {
			if (isSubmit()) {
				operationService.deleteAllBlankOperations();
			}
			blankOperationsCount = operationService.getBlankOperationsCount();
		} else if (paymentCollector != null) {
			if (isSubmit()) {
				operationService.deleteBlankOperations(Stub.stub(paymentCollector));
			}
			blankOperationsCount = operationService.getBlankOperationsCount(Stub.stub(paymentCollector));
		} else {
			blankOperationsCount = 0L;
			log.error("Payment collector did not find in user preference. Did not show blank operation.");
			return ERROR;
		}

		return SUCCESS;
	}

	@NotNull
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

	public Long getBlankOperationsCount() {
		return blankOperationsCount;
	}

	public void setBlankOperationsCount(Long blankOperationsCount) {
		this.blankOperationsCount = blankOperationsCount;
	}

	@Required
	public void setOperationService(OperationService operationService) {
		this.operationService = operationService;
	}
}
