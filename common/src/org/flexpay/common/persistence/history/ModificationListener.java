package org.flexpay.common.persistence.history;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.service.JpaSetService;
import org.jetbrains.annotations.NotNull;

public interface ModificationListener<T extends DomainObject> extends JpaSetService {

	/**
	 * Notify of new object created
	 *
	 * @param obj New object
	 */
	void onCreate(@NotNull T obj);

	/**
	 * Notify of object update
	 *
	 * @param objOld object old version
	 * @param obj object new version
	 */
	void onUpdate(@NotNull T objOld, @NotNull T obj);

	/**
	 * Notify of object delete
	 *
	 * @param obj object that was deleted
	 */
	void onDelete(@NotNull T obj);
}
