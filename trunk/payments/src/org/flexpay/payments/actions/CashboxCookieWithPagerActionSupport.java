package org.flexpay.payments.actions;

import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.payments.actions.interceptor.CashboxAware;

public abstract class CashboxCookieWithPagerActionSupport<T> extends FPActionWithPagerSupport<T> implements CashboxAware {

	protected Long cashboxId;

	@Override
	public Long getCashboxId() {
		return cashboxId;
	}

	@Override
	public void setCashboxId(Long cashboxId) {
		this.cashboxId = cashboxId;
	}

}
