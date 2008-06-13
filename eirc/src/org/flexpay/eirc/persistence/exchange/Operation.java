package org.flexpay.eirc.persistence.exchange;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.persistence.SpRegistryRecord;

public abstract class Operation {

	/**
	 * Simbol used escape special simbols
	 */
	public static final char ESCAPE_SIMBOL = '\\';

	/**
	 * Simbol used to split containers
	 */
	public static final char CONTAINER_DELIMITER = '|';

	/**
	 * Simbol used to split fields in containers
	 */
	public static final char CONTAINER_DATA_DELIMITER = ':';

	/**
	 * Simbol used to split fields in records
	 */
	public static final char RECORD_DELIMITER = ';';

	/**
	 * Simbol used to split fields in address group
	 */
	public static final char ADDRESS_DELIMITER = ',';

	/**
	 * Simbol used to split fields in first-middle-last names group
	 */
	public static final char FIO_DELIMITER = ',';

	/**
	 * Process operation
	 * 
	 * @param registry Registry header
	 * @param record Registry record
	 * @throws FlexPayException if failure occurs
	 */
	abstract public void process(SpRegistry registry, SpRegistryRecord record) throws FlexPayException;
}
