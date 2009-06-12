package org.flexpay.payments.actions.cashbox;

import org.flexpay.payments.actions.CashboxCookieWithPagerActionSupport;
import org.flexpay.payments.persistence.Cashbox;
import org.flexpay.payments.service.CashboxService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collections;
import java.util.List;

public class CashboxesListAction extends CashboxCookieWithPagerActionSupport<Cashbox> {

	private List<Cashbox> cashboxes = Collections.emptyList();

	private CashboxService cashboxService;

	@NotNull
	protected String doExecute() throws Exception {

		cashboxes = cashboxService.findObjects(getPager());

		return SUCCESS;
	}

	@NotNull
	protected String getErrorResult() {
		return SUCCESS;
	}

	public List<Cashbox> getCashboxes() {
		return cashboxes;
	}

	@Required
	public void setCashboxService(CashboxService cashboxService) {
		this.cashboxService = cashboxService;
	}

}