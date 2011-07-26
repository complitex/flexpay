package org.flexpay.common.persistence.history;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.service.JpaSetService;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * History generator creates history for the objects type of its parameter
 */
public interface HistoryGenerator<T extends DomainObject> extends JpaSetService {

	/**
	 * Do generation
	 *
	 * @param obj Object to generate history for
	 */
	void generateFor(@NotNull T obj);

	/**
	 * Do generation of history for several objects
	 *
	 * @param objs Objects to generate history for
	 */
	void generateFor(@NotNull Collection<T> objs);
}
