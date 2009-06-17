package org.flexpay.common.service.importexport;

import org.flexpay.common.persistence.DomainObject;

public interface ClassToTypeRegistry {

	/**
	 * Get class type id to use in corrections service
	 *
	 * @param clazz Object class
	 * @return Type id
	 * @throws IllegalArgumentException if <code>clazz</code> has no assigned type
	 */
	int getType(Class<? extends DomainObject> clazz) throws IllegalArgumentException;
}
