package org.flexpay.eirc.dao;

import org.flexpay.eirc.persistence.AccountStatus;

import java.util.Collection;

public interface AccountStatusDao {

	Collection<AccountStatus> getAccountStatuses();
}
