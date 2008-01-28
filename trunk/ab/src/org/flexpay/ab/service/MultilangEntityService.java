package org.flexpay.ab.service;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Translation;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * Service interface for multilaguage entity related tasks
 */
public interface MultilangEntityService<Entity, T extends Translation> {

	/**
	 * Create Entity
	 *
	 * @param translations Entity names translations
	 * @return created Entity object
	 * @throws FlexPayException if failure occurs
	 */
	public Entity create(Collection<T> translations) throws FlexPayException;

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
	List<Entity> getEntities();

	/**
	 * Update street type translations
	 *
	 * @param entity	   Entity to update trnaslations for
	 * @param translations Translations set
	 * @return Updated Entity object
	 */
	Entity update(Entity entity, Collection<T> translations);

	/**
	 * Disable Entity
	 *
	 * @param entity Entity to disable
	 */
	void disable(Collection<Entity> entity);

}
