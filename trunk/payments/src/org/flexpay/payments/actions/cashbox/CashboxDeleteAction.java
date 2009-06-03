package org.flexpay.payments.actions.cashbox;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.payments.actions.interceptor.CashboxAware;
import org.flexpay.payments.service.CashboxService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.HashSet;
import java.util.Set;

public class CashboxDeleteAction extends FPActionSupport implements CashboxAware {

	private Long cashboxId;
	private Set<Long> objectIds = new HashSet<Long>();

	private CashboxService cashboxService;

	@NotNull
	protected String doExecute() throws Exception {

		cashboxService.disable(objectIds);

		return REDIRECT_SUCCESS;
	}

	@NotNull
	protected String getErrorResult() {
		return REDIRECT_SUCCESS;
	}

	@Override
	protected void setBreadCrumbs() {

	}

	public Set<Long> getObjectIds() {
		return objectIds;
	}

	public void setObjectIds(Set<Long> objectIds) {
		this.objectIds = objectIds;
	}

	public Long getCashboxId() {
		return cashboxId;
	}

	public void setCashboxId(Long cashboxId) {
		this.cashboxId = cashboxId;
	}

	@Required
	public void setCashboxService(CashboxService cashboxService) {
		this.cashboxService = cashboxService;
	}

}
