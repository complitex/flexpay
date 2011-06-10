package org.flexpay.eirc.persistence.exchange.delayed;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.eirc.persistence.EircAccount;
import org.flexpay.eirc.persistence.exchange.DelayedUpdate;
import org.flexpay.eirc.service.EircAccountService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class DelayedUpdateEircAccount implements DelayedUpdate {

	private EircAccount account;
	private EircAccountService service;

	public DelayedUpdateEircAccount(EircAccount account, EircAccountService service) {
		this.account = account;
		this.service = service;
	}

	/**
	 * Perform storage update
	 *
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if operation fails
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if operation fails
	 */
	@Transactional(readOnly = false, propagation = Propagation.MANDATORY)
	@Override
	public void doUpdate() throws FlexPayException, FlexPayExceptionContainer {
		if (account.isNew()) {
			service.create(account);
		} else {
			service.update(account);
		}
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof DelayedUpdateEircAccount &&
			   ((DelayedUpdateEircAccount) o).account.equals(account);
	}

	@Override
	public int hashCode() {
		return account.hashCode();
	}
}
