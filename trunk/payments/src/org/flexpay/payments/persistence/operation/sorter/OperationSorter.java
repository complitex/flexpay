package org.flexpay.payments.persistence.operation.sorter;

import org.flexpay.common.persistence.sorter.ObjectSorter;

public abstract class OperationSorter extends ObjectSorter {

	/**
	 * {@inheritDoc}
	 */
    @Override
	public void setFrom(StringBuilder query) {}
}