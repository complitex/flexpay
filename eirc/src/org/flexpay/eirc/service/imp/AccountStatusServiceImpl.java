package org.flexpay.eirc.service.imp;

import org.flexpay.eirc.service.AccountStatusService;
import org.flexpay.eirc.persistence.AccountStatus;
import org.flexpay.eirc.dao.AccountStatusDao;

import java.util.List;
import java.util.Collection;

public class AccountStatusServiceImpl implements AccountStatusService {

	private AccountStatusDao accountStatusDao;

	private Collection<AccountStatus> accountStatuses;

	/**
	 * Get account status by status enum id
	 *
	 * @param status status enum
	 * @return AccountStatus if found, or <code>null</code> otherwise
	 */
	public AccountStatus getStatus(int status) {
		if (accountStatuses == null) {
			accountStatuses = accountStatusDao.getAccountStatuses();
		}

		for (AccountStatus accountStatus : accountStatuses) {
			if (accountStatus.getStatus() == status) {
				return accountStatus;
			}
		}

		return null;
	}

	public void setAccountStatusDao(AccountStatusDao accountStatusDao) {
		this.accountStatusDao = accountStatusDao;
	}
}
