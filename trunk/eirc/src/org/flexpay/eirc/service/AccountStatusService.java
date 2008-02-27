package org.flexpay.eirc.service;

import org.flexpay.eirc.persistence.AccountStatus;

public interface AccountStatusService {

	/**
	 * Get account status by status enum id
	 *
	 * @param status status enum
	 * @return AccountStatus if found, or <code>null</code> otherwise
	 */
	AccountStatus getStatus(int status);
}
