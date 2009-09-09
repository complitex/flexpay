package org.flexpay.orgs.actions.cashbox;

import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.service.CashboxService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class CashboxesListAction extends FPActionWithPagerSupport<Cashbox> {

	protected List<Cashbox> cashboxes = CollectionUtils.list();

	protected CashboxService cashboxService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		cashboxes = cashboxService.findObjects(getPager());

		return SUCCESS;
	}

	@NotNull
	@Override
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
