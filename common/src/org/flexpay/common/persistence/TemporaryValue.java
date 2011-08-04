package org.flexpay.common.persistence;

public abstract class TemporaryValue<T extends TemporaryValue> extends EsbXmlSyncObject {

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

	/**
	 * Check if this value is empty
	 *
	 * @return <code>true</code> if this value is empty, or <code>false</code> otherwise
	 */
	public abstract boolean isEmpty();
}
