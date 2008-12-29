package org.flexpay.common.service.importexport;

import org.flexpay.common.persistence.DomainObject;

public interface ClassToTypeRegistry {

	/**
	 * Get class type id to use in corrections service
	 *
	 * @param clazz Object class
	 * @return Type id
	 */
	int getType(Class<? extends DomainObject> clazz);
}
