package org.flexpay.common.persistence;

public abstract class TemporaryValue<T extends TemporaryValue> extends DomainObject {

	/**
	 * Get null value
	 *
	 * @return Null representation of this value
	 */
	public abstract T getEmpty();
}
