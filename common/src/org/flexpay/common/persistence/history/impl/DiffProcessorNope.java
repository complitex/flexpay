package org.flexpay.common.persistence.history.impl;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.DiffProcessor;

public class DiffProcessorNope<T extends DomainObject> implements DiffProcessor<T> {

	/**
	 * ProcessInstance creation diff
	 *
	 * @param obj  Created object
	 * @param diff Diff
	 */
	@Override
	public void onCreate(T obj, Diff diff) {
	}

	/**
	 * ProcessInstance update diff
	 *
	 * @param obj1 Old object version
	 * @param obj2 new object version
	 * @param diff Diff of two versions
	 */
	@Override
	public void onUpdate(T obj1, T obj2, Diff diff) {
	}

	/**
	 * ProcessInstance deletion diff
	 *
	 * @param obj  Deleted object
	 * @param diff Diff
	 */
	@Override
	public void onDelete(T obj, Diff diff) {
	}
}
