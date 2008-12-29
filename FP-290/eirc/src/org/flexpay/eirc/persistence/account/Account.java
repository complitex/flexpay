package org.flexpay.eirc.persistence.account;

import org.flexpay.common.persistence.DomainObjectWithStatus;

public class Account extends DomainObjectWithStatus {

	private EircAccountPlan accountPlan;

	public EircAccountPlan getAccountPlan() {
		return accountPlan;
	}

	public void setAccountPlan(EircAccountPlan accountPlan) {
		this.accountPlan = accountPlan;
	}
}
