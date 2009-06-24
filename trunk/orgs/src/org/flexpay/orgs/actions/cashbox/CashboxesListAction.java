package org.flexpay.orgs.actions.cashbox;

import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.service.CashboxService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collections;
import java.util.List;

public class CashboxesListAction extends FPActionWithPagerSupport<Cashbox> {

	protected List<Cashbox> cashboxes = Collections.emptyList();

	protected CashboxService cashboxService;

	@NotNull
	protected String doExecute() throws Exception {

		loadCashboxes();

		return SUCCESS;
	}

	protected void loadCashboxes() {

		cashboxes = cashboxService.findObjects(getPager());
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
