package org.flexpay.common.persistence.registry.sorter;

public class RegistrySorterByCreationDate extends RegistrySorter {

	/**
	 * {@inheritDoc}
	 */
    @Override
	public void setOrderBy(StringBuilder orderByClause) {
		orderByClause.append(" r.creationDate ").append(getOrder());
	}
}
