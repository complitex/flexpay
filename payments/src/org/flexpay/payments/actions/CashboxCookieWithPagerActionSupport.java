package org.flexpay.payments.actions;

import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.payments.actions.interceptor.CashboxAware;

public abstract class CashboxCookieWithPagerActionSupport<T> extends FPActionWithPagerSupport<T> implements CashboxAware {

	protected Long cashboxId;

	public Long getCashboxId() {
		return cashboxId;
	}

	public void setCashboxId(Long cashboxId) {
		this.cashboxId = cashboxId;
	}

}
