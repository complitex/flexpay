package org.flexpay.payments.actions.operations;

import org.flexpay.payments.actions.CashboxCookieActionSupport;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.service.OperationService;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.service.CashboxService;
import org.flexpay.common.util.SecurityUtil;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.math.BigDecimal;

public class CreateBlankOperationAction extends CashboxCookieActionSupport {

	// status codes
	private static final int STATUS_OK = 0;
	private static final int STATUS_ERROR = 1;

	// data
	private int status;
	private Long operationId;

	// required services
	private OperationService operationService;
	private CashboxService cashboxService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		// creating blank operation
		try {
			Cashbox cashbox = getCashbox();
			Organization organization = cashbox.getPaymentPoint().getCollector().getOrganization();
			Operation newOperationBlank = operationService.createBlankOperation(BigDecimal.valueOf(0), SecurityUtil.getUserName(), organization, cashbox.getPaymentPoint(), cashbox);
			operationId = newOperationBlank.getId();
			status = STATUS_OK;
		} catch (Throwable t) {
			log.error("Error creating blank operation", t);
			status = STATUS_ERROR;
			operationId = -1L;
			return SUCCESS;
		}

		return SUCCESS;
	}

	private Cashbox getCashbox() {
		
		Cashbox cashbox = cashboxService.read(new Stub<Cashbox>(cashboxId));

		if (cashbox == null) {
			throw new IllegalArgumentException("Invalid cashbox id: " + cashboxId);
		}

		return cashbox;
	}

	@NotNull
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

	// data
	public int getStatus() {
		return status;
	}

	public Long getOperationId() {
		return operationId;
	}

	// required services
	@Required
	public void setOperationService(OperationService operationService) {
		this.operationService = operationService;
	}

	@Required
	public void setCashboxService(CashboxService cashboxService) {
		this.cashboxService = cashboxService;
	}
}