package org.flexpay.common.persistence.history;

import org.flexpay.common.persistence.DomainObject;
import org.jetbrains.annotations.NotNull;

/**
 * History generator creates history for the objects type of its parameter
 */
public interface HistoryGenerator<T extends DomainObject> {

	/**
	 * Do generation
	 *
	 * @param obj Object to generate history for
	 */
	void generateFor(@NotNull T obj);
}
