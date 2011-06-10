package org.flexpay.common.persistence.history;

import org.flexpay.common.persistence.DomainObject;

/**
 * ProcessInstance new diff object
 *
 * @param <T> Objects type diff was created for
 */
public interface DiffProcessor<T extends DomainObject> {

	/**
	 * ProcessInstance creation diff
	 *
	 * @param obj Created object
	 * @param diff Diff
	 */
	void onCreate(T obj, Diff diff);

	/**
	 * ProcessInstance update diff
	 *
	 * @param obj1 Old object version
	 * @param obj2 new object version
	 * @param diff Diff of two versions
	 */
	void onUpdate(T obj1, T obj2, Diff diff);

	/**
	 * ProcessInstance deletion diff
	 *
	 * @param obj Deleted object
	 * @param diff Diff
	 */
	void onDelete(T obj, Diff diff);
}
