package org.flexpay.common.persistence.history.builder;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.Nullable;

/**
 * Callback interface for {@link HistoryBuilderHelper#patchReference(org.flexpay.common.persistence.DomainObject,
 * org.flexpay.common.persistence.history.HistoryRecord, ReferencePatcher)}
 *
 * @param <Ref> Reference type
 * @param <DO> Object type
 */
public interface ReferencePatcher<Ref extends DomainObject, DO extends DomainObject> {

	/**
	 * Get reference type
	 *
	 * @return Reference class
	 */
	Class<Ref> getType();

	/**
	 * Set reference
	 *
	 * @param obj Object to setup reference to
	 * @param ref Reference object
	 */
	void setReference(DO obj, @Nullable Stub<Ref> ref);
}
