package org.flexpay.eirc.persistence.exchange;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.persistence.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jetbrains.annotations.NotNull;

public abstract class Operation {

	protected Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * Symbol used escape special symbols
	 */
	public static final char ESCAPE_SYMBOL = '\\';

	/**
	 * Symbol used to split containers
	 */
	public static final char CONTAINER_DELIMITER = '|';

	/**
	 * Symbol used to split fields in containers
	 */
	public static final char CONTAINER_DATA_DELIMITER = ':';

	/**
	 * Symbol used to split fields in records
	 */
	public static final char RECORD_DELIMITER = ';';

	/**
	 * Symbol used to split fields in address group
	 */
	public static final char ADDRESS_DELIMITER = ',';

	/**
	 * Symbol used to split fields in first-middle-last names group
	 */
	public static final char FIO_DELIMITER = ',';

	/**
	 * Process operation
	 *
	 * @param context ProcessingContext
	 * @return DelayedUpdate object
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if failure occurs
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if failure occurs
	 */
	abstract public DelayedUpdate process(@NotNull ProcessingContext context) throws FlexPayException, FlexPayExceptionContainer;

}
