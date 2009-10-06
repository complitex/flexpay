package org.flexpay.eirc.persistence.exchange.conditions.impl;

import org.flexpay.eirc.persistence.Consumer;
import org.flexpay.eirc.persistence.EircAccount;
import org.flexpay.eirc.persistence.exchange.conditions.AccountPersonChangeCondition;
import org.flexpay.eirc.util.config.ApplicationConfig;

public class MainServiceAccountPersonChangeCondition implements AccountPersonChangeCondition {

	/**
	 * Check if update of responsible person needed
	 *
	 * @param account  EircAccount
	 * @param consumer Consumer that responsible person is to be updated
	 * @return <code>true</code> if update needed, or <code>false</code> otherwise
	 */
	public boolean needUpdatePerson(EircAccount account, Consumer consumer) {
		return account.equals(consumer.getEircAccount()) &&
			   consumer.getService().getServiceType().getCode() == ApplicationConfig.getEircMainServiceCode();
	}
}
