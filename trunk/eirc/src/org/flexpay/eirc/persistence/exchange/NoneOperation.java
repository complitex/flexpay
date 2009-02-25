package org.flexpay.eirc.persistence.exchange;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.eirc.persistence.RegistryRecord;
import org.flexpay.eirc.persistence.SpRegistry;

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
	public void process(SpRegistry registry, RegistryRecord record) throws FlexPayException {
	}
}
