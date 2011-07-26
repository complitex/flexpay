package org.flexpay.common.persistence.history;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.service.JpaSetService;
import org.jetbrains.annotations.NotNull;

/**
 * History generator that ensures history exists for referenced objects
 *
 * @param <T> Object type
 */
public interface ReferencesHistoryGenerator<T extends DomainObject> extends JpaSetService {

	void generateReferencesHistory(@NotNull T obj);
}
