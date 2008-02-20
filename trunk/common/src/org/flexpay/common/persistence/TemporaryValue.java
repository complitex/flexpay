package org.flexpay.common.persistence;

public abstract class TemporaryValue<T extends TemporaryValue> extends DomainObject {

	protected TemporaryValue() {
	}

	protected TemporaryValue(Long id) {
		super(id);
	}

	/**
	 * Get null value
	 *
	 * @return Null representation of this value
	 */
	public abstract T getEmpty();
}
