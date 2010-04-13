package org.flexpay.payments.actions.reports;

import org.flexpay.payments.actions.AccountantAWPActionSupport;
import org.flexpay.payments.service.OperationService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class AccBlankOperationsReportAction extends AccountantAWPActionSupport {

	private Long blankOperationsCount;

	private OperationService operationService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		if (isSubmit()) {
			operationService.deleteAllBlankOperations();
		}

		blankOperationsCount = operationService.getBlankOperationsCount(); 

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
