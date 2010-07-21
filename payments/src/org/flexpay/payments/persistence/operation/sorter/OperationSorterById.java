package org.flexpay.payments.persistence.operation.sorter;

public class OperationSorterById extends OperationSorter {

	/**
	 * {@inheritDoc}
	 */
    @Override
	public void setOrderBy(StringBuilder orderByClause) {
		orderByClause.append(" o.id ").append(getOrder());
	}
}