package org.flexpay.common.service;

import org.flexpay.common.persistence.DomainObject;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Helper that can initialize additional object properties
 *
 * @param <T> Object type this interface supports
 */
public interface PropertiesInitializer<T extends DomainObject> {

	/**
	 * initialize properties of a single object
	 *
	 * @param obj Object that's properties to initialize
	 */
	void init(@NotNull T obj);

	/**
	 * initialize properties of a group of objects
	 *
	 * @param objs Objects that's properties to initialize
	 */
	void init(@NotNull Collection<T> objs);
}
