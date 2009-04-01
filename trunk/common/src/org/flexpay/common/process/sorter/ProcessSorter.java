package org.flexpay.common.process.sorter;

import org.flexpay.common.persistence.sorter.ObjectSorter;

/**
 * Base class for different kinds of process sorters
 */
public abstract class ProcessSorter extends ObjectSorter {

	/**
	 * {@inheritDoc}
	 */
	public void setFrom(StringBuilder query) {}
}
