package org.flexpay.payments.actions.cashbox;

import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.service.CashboxService;
import org.flexpay.payments.actions.AccountantAWPActionSupport;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import static org.flexpay.common.persistence.Stub.stub;

public class CashboxViewAction extends AccountantAWPActionSupport {

	private Cashbox cashbox = new Cashbox();

	private CashboxService cashboxService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		if (cashbox.isNew()) {
			log.error(getText("common.error.invalid_id"));
			addActionError(getText("common.error.invalid_id"));
			return REDIRECT_ERROR;
		}
		cashbox = cashboxService.read(stub(cashbox));

		if (cashbox == null) {
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

	public Cashbox getCashbox() {
		return cashbox;
	}

	public void setCashbox(Cashbox cashbox) {
		this.cashbox = cashbox;
	}

	@Required
	public void setCashboxService(CashboxService cashboxService) {
		this.cashboxService = cashboxService;
	}

}
