package org.flexpay.eirc.persistence.exchange.conditions;

import org.flexpay.eirc.persistence.Consumer;
import org.flexpay.eirc.persistence.EircAccount;

/**
 * Condition checks if eirc account responsible person should be changed when recieved
 * update on some consumer
 */
public interface AccountPersonChangeCondition {

	/**
	 * Check if update of responsible person needed
	 *
	 * @param account EircAccount
	 * @param consumer Consumer that responsible person is to be updated
	 * @return <code>true</code> if update needed, or <code>false</code> otherwise
	 */
	boolean needUpdatePerson(EircAccount account, Consumer consumer);
}
