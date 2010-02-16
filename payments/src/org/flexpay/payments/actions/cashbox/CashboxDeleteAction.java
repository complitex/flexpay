package org.flexpay.payments.actions.cashbox;

import static org.flexpay.common.util.CollectionUtils.set;
import org.flexpay.orgs.service.CashboxService;
import org.flexpay.payments.actions.AccountantAWPActionSupport;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Set;

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