package org.flexpay.common.persistence.history.impl;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.history.ModificationListener;
import org.jetbrains.annotations.NotNull;

/**
 * Midificatation listener that does nothing
 * @param <T>
 */
public class NopeModificationListener<T extends DomainObject> implements ModificationListener<T> {

	/**
	 * Notify of new object created
	 *
	 * @param obj New object
	 */
	public void onCreate(@NotNull T obj) {
	}

	/**
	 * Notify of object update
	 *
	 * @param objOld object old version
	 * @param obj	object new version
	 */
	public void onUpdate(@NotNull T objOld, @NotNull T obj) {
	}

	/**
	 * Notify of object delete
	 *
	 * @param obj object that was deleted
	 */
	public void onDelete(@NotNull T obj) {
	}
}
