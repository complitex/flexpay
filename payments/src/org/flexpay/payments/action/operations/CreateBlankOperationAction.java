package org.flexpay.payments.action.operations;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.SecurityUtil;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.payments.action.OperatorAWPActionSupport;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.persistence.OperationActionLog;
import org.flexpay.payments.service.OperationService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class CreateBlankOperationAction extends OperatorAWPActionSupport {

	// status codes
	private static final int STATUS_OK = 0;
	private static final int STATUS_ERROR = 1;

	private int status;
	private Long operationId;

	private OperationService operationService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		// creating blank operation
		try {
			Operation newOperationBlank = operationService.createBlankOperation(SecurityUtil.getUserName(), getCashboxStub());
			operationId = newOperationBlank.getId();

            createActionLog(getCashbox());

			status = STATUS_OK;
		} catch (Throwable t) {
			log.error("Error creating blank operation", t);
			status = STATUS_ERROR;
			operationId = -1L;
			return SUCCESS;
		}

		return SUCCESS;
	}

    private OperationActionLog createActionLog(Cashbox cashbox) throws Exception {
        OperationActionLog actionLog = new OperationActionLog();

        actionLog.setUserName(getUserPreferences().getUsername());
        actionLog.setAction(OperationActionLog.PRINT_QUITTANCE);
        actionLog.setActionString("Квитанция №" + operationId);
        actionLog.setCashbox(cashbox);

        return actionLog;
    }


	private Cashbox getCashbox() {

		Cashbox cashbox = cashboxService.read(new Stub<Cashbox>(getCashboxId()));

		if (cashbox == null) {
			throw new IllegalArgumentException("Invalid cashbox id: " + getCashboxId());
		}

		return cashbox;
	}

	private Stub<Cashbox> getCashboxStub() {
		return new Stub<Cashbox>(getCashboxId());
	}

	@NotNull
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

	public int getStatus() {
		return status;
	}

	public Long getOperationId() {
		return operationId;
	}

	@Required
	public void setOperationService(OperationService operationService) {
		this.operationService = operationService;
	}

}
