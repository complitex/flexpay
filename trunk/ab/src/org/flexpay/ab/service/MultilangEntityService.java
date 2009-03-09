package org.flexpay.ab.service;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Translation;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * Service interface for multilaguage entity related tasks
 */
public interface MultilangEntityService<Entity, T extends Translation> {

	/**
	 * Read Entity object by its unique id
	 *
	 * @param id Entity key
	 * @return Entity object, or <code>null</code> if object not found
	 */
	public Entity read(Long id);

	/**
	 * Get Entity translations for specified locale, if translation is not found check for
	 * translation in default locale
	 *
	 * @param locale Locale to get translations for
	 * @return List of Translation
	 * @throws FlexPayException if failure occurs
	 */
	List<T> getTranslations(Locale locale) throws FlexPayException;

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
