package org.flexpay.eirc.persistence.exchange;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.eirc.persistence.exchange.delayed.DelayedUpdateNope;

/**
 * Operation that does nothing useful
 */
public class NoneOperation extends Operation {

	/**
	 * Process operation
	 *
	 * @param registry Registry header
	 * @param record   Registry record
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if failure occurs
	 */
	public DelayedUpdate process(Registry registry, RegistryRecord record) throws FlexPayException {
		return DelayedUpdateNope.INSTANCE;
	}
}
