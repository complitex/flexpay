package org.flexpay.eirc.persistence.account;

import org.flexpay.common.persistence.DomainObjectWithStatus;

import java.util.Collections;
import java.util.Set;

import static org.flexpay.common.util.CollectionUtils.set;

public class EircAccountPlan extends DomainObjectWithStatus {

	private Set<Account> accounts = set();

	public Set<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(Set<Account> accounts) {
		this.accounts = accounts;
	}
}
