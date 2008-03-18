package org.flexpay.eirc.persistence.exchange;

import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.persistence.SpRegistryRecord;
import org.flexpay.common.exception.FlexPayException;

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
	public void process(SpRegistry registry, SpRegistryRecord record) throws FlexPayException {
	}
}
