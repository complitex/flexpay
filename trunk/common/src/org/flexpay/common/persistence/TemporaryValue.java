package org.flexpay.common.persistence;

import java.io.Serializable;

public interface TemporaryValue<T extends TemporaryValue> extends Serializable {

	/**
	 * Get null value
	 *
	 * @return Null representation of this value
	 */
	public T getEmpty();
}
