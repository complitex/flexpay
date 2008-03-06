package org.flexpay.eirc.persistence.exchange;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.persistence.SpRegistryRecord;

public abstract class Operation {

	public static final char ESCAPE_SIMBOL = '\\';
	public static final char CONTAINER_DELIMITER = '|';
	public static final char CONTAINER_DATA_DELIMITER = ':';

	private int typeId;

	protected Operation(int typeId) {
		this.typeId = typeId;
	}

	/**
	 * Process operation
	 * 
	 * @param registry Registry header
	 * @param record Registry record
	 * @throws FlexPayException if failure occurs
	 */
	abstract public void process(SpRegistry registry, SpRegistryRecord record) throws FlexPayException;

	/**
	 * Get container string representation
	 *
	 * @return container string representation
	 */
	abstract public String getStringFormat();

	/**
	 * Getter for property 'type'.
	 *
	 * @return Value for property 'type'.
	 */
	public int getTypeId() {
		return typeId;
	}
}
