package org.flexpay.eirc.persistence.account;

import org.flexpay.common.persistence.DomainObjectWithStatus;

import java.util.Set;
import java.util.Collections;

public class EircAccountPlan extends DomainObjectWithStatus {

	private Set<Account> accounts = Collections.emptySet();

	public Set<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(Set<Account> accounts) {
		this.accounts = accounts;
	}
}
