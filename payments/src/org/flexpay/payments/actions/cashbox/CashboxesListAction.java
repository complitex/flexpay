package org.flexpay.payments.actions.cashbox;

import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.payments.persistence.Cashbox;
import org.flexpay.payments.service.CashboxService;
import org.flexpay.payments.actions.interceptor.CashboxAware;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collections;
import java.util.List;

public class CashboxesListAction extends FPActionWithPagerSupport<Cashbox> implements CashboxAware {

	private Long cashboxId;
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
