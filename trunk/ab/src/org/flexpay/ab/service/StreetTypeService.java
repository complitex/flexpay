package org.flexpay.ab.service;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.flexpay.ab.persistence.StreetType;
import org.flexpay.ab.persistence.StreetTypeTranslation;
import org.flexpay.common.exception.FlexPayException;

/**
 * Service interface for StreetTypes related tasks
 */
public interface StreetTypeService {

	/**
	 * Create StreetType
	 *
	 * @param translations StreetType names translations
	 * @return created StreetType object
	 */
	public StreetType create(Collection<StreetTypeTranslation> translations);

	/**
	 * Read StreetType object by its unique id
	 *
	 * @param id StreetType key
	 * @return StreetType object, or <code>null</code> if object not found
	 */
	public StreetType read(Long id);

	/**
	 * Get StreetType translations for specified locale, if translation is not found check for
	 * translation in default locale
	 *
	 * @param locale Locale to get translations for
	 * @return List of StreetTypeTranslations
	 * @throws FlexPayException if failure occurs
	 */
	List<StreetTypeTranslation> getStreetTypeTranslations(Locale locale) throws FlexPayException;

	/**
	 * Get a list of available street types
	 *
	 * @return List of StreetType
	 */
	List<StreetType> getStreetTypes();

	/**
	 * Update street type translations
	 *
	 * @param streetType Street Type to update trnaslations for
	 * @param streetTypeTranslations Translations set
	 * @return Updated StreetType object
	 */
	StreetType update(StreetType streetType, Collection<StreetTypeTranslation> streetTypeTranslations);

	/**
	 * Disable StreetTypes
	 *
	 * @param StreetTypes StreetTypes to disable
	 */
	void disable(Collection<StreetType> streetTypes);
}
