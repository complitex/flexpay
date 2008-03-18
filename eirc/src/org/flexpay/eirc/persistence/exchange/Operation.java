package org.flexpay.eirc.persistence.exchange;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.persistence.SpRegistryRecord;

public abstract class Operation {

	public static final char ESCAPE_SIMBOL = '\\';
	public static final char CONTAINER_DELIMITER = '|';
	public static final char CONTAINER_DATA_DELIMITER = ':';

	/**
	 * Process operation
	 * 
	 * @param registry Registry header
	 * @param record Registry record
	 * @throws FlexPayException if failure occurs
	 */
	abstract public void process(SpRegistry registry, SpRegistryRecord record) throws FlexPayException;
}
