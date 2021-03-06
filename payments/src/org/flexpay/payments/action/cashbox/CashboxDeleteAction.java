package org.flexpay.payments.action.cashbox;

import org.flexpay.orgs.service.CashboxService;
import org.flexpay.payments.action.AccountantAWPActionSupport;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Set;

import static org.flexpay.common.util.CollectionUtils.set;

public class CashboxDeleteAction extends AccountantAWPActionSupport {

	private Set<Long> objectIds = set();

	private CashboxService cashboxService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		cashboxService.disable(objectIds);

		return SUCCESS;
	}

	@NotNull
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

	public void setObjectIds(Set<Long> objectIds) {
		this.objectIds = objectIds;
	}

	@Required
	public void setCashboxService(CashboxService cashboxService) {
		this.cashboxService = cashboxService;
	}

}
