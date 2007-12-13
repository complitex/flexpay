package org.flexpay.ab.service;

import org.flexpay.ab.persistence.TownType;
import org.flexpay.ab.persistence.TownTypeTranslation;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * Service interface for TownTypes related tasks
 */
public interface TownTypeService {

	/**
	 * Create TownType
	 *
	 * @param translations TownType names translations
	 * @return created Country object
	 * @throws FlexPayException if failure occurs
	 */
	public TownType create(Collection<TownTypeTranslation> translations) throws FlexPayException;

	/**
	 * Read TownType object by its unique id
	 *
	 * @param id TownType key
	 * @return TownType object, or <code>null</code> if object not found
	 */
	public TownType read(Long id);

	/**
	 * Get TownType translations for specified locale, if translation is not found check for
	 * translation in default locale
	 *
	 * @param locale Locale to get translations for
	 * @return List of TownTypes
	 * @throws FlexPayException if failure occurs
	 */
	List<TownTypeTranslation> getTownTypeTranslations(Locale locale) throws FlexPayException;

	/**
	 * Get a list of available town types
	 *
	 * @return List of TownType
	 */
	List<TownType> getTownTypes();

	/**
	 * Update town type translations
	 *
	 * @param townType Town Type to update trnaslations for
	 * @param townTypeTranslations Translations set
	 * @return Updated TownType object
	 * @throws FlexPayException if town type translations specified are invalid
	 */
	TownType update(TownType townType, Collection<TownTypeTranslation> townTypeTranslations)
			throws FlexPayException;

	/**
	 * Disable TownTypes
	 *
	 * @param townTypes TownTypes to disable
	 * @throws FlexPayExceptionContainer if failure occurs
	 */
	void disable(Collection<TownType> townTypes) throws FlexPayExceptionContainer;
}
