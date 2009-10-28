package org.flexpay.ab.service;

import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.Translation;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

/**
 * Service interface for multilaguage entity related tasks
 */
public interface MultilangEntityService<Entity extends DomainObject, T extends Translation> {

	/**
	 * Read Entity object by its unique id
	 *
	 * @param stub Entity stub
	 * @return Entity object, or <code>null</code> if object not found
	 */
	Entity read(Stub<Entity> stub);

	/**
	 * Get a list of available street types
	 *
	 * @return List of Entity
	 */
	@NotNull
	List<Entity> getEntities();

	/**
	 * Disable Entity
	 *
	 * @param entity Entity to disable
	 */
	void disable(Collection<Entity> entity);

	/**
	 * Create Entity
	 *
	 * @param entity Entity to save
	 * @return Saved instance
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	Entity create(@NotNull Entity entity) throws FlexPayExceptionContainer;

	/**
	 * Update Entity
	 *
	 * @param entity Entity to save
	 * @return Saved instance
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	Entity update(@NotNull Entity entity) throws FlexPayExceptionContainer;

}
