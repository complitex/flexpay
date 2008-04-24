package org.flexpay.ab.service.imp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.flexpay.ab.dao.TownTypeDao;
import org.flexpay.ab.dao.TownTypeTranslationDao;
import org.flexpay.ab.persistence.TownType;
import org.flexpay.ab.persistence.TownTypeTranslation;
import org.flexpay.ab.persistence.filters.TownTypeFilter;
import org.flexpay.ab.service.TownTypeService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.util.TranslationUtil;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true, rollbackFor = Exception.class)
public class TownTypeServiceImpl implements TownTypeService {

	private static Logger log = Logger.getLogger(TownTypeServiceImpl.class);

	private TownTypeDao townTypeDao;
	private TownTypeTranslationDao townTypeTranslationDao;

	/**
	 * Create TownType
	 * 
	 * @param translations
	 *            TownType names translations
	 * @return created TownType object
	 */
	@Transactional(readOnly = false)
	public TownType create(Collection<TownTypeTranslation> translations)
			throws FlexPayException {
		TownType townType = new TownType();

		Set<TownTypeTranslation> translationSet = new HashSet<TownTypeTranslation>();
		boolean hasDefaultLangTranslation = false;
		for (TownTypeTranslation translation : translations) {
			if (StringUtils.isNotBlank(translation.getName())) {
				translationSet.add(translation);
				hasDefaultLangTranslation = hasDefaultLangTranslation
						|| translation.getLang().isDefault();
			}
		}
		if (!hasDefaultLangTranslation) {
			throw new FlexPayException(
					"No default language town type translation",
					"error.town_type_no_default_translation");
		}

		townType.setStatus(TownType.STATUS_ACTIVE);

		townTypeDao.create(townType);
		for (TownTypeTranslation translation : translationSet) {
			translation.setTranslatable(townType);
			townTypeTranslationDao.create(translation);
		}

		if (log.isDebugEnabled()) {
			log.debug("Created TownType: " + townType);
		}

		return townType;
	}

	/**
	 * Get TownType translations for specified locale, if translation is not
	 * found check for translation in default locale
	 * 
	 * @param locale
	 *            Locale to get translations for
	 * @return List of TownTypes
	 * @throws FlexPayException
	 *             if failure occurs
	 */
	public List<TownTypeTranslation> getTranslations(Locale locale)
			throws FlexPayException {

		log.debug("Getting list of TownTypes");

		List<TownType> townTypes = townTypeDao
				.listTownTypes(TownType.STATUS_ACTIVE);
		List<TownTypeTranslation> translations = new ArrayList<TownTypeTranslation>(
				townTypes.size());

		if (log.isDebugEnabled()) {
			log.debug("TownTypes: " + townTypes);
		}

		for (TownType townType : townTypes) {
			TownTypeTranslation translation = TranslationUtil.getTranslation(
					townType.getTranslations(), locale);
			if (translation == null) {
				log.error("No name for town type: " + townType);
				continue;
			}
			translations.add(translation);
		}

		return translations;
	}

	/**
	 * Read TownType object by its unique id
	 * 
	 * @param id
	 *            TownType key
	 * @return TownType object, or <code>null</code> if object not found
	 */
	public TownType read(Long id) {
		return townTypeDao.readFull(id);
	}

	/**
	 * Update town type translations
	 * 
	 * @param townType
	 *            Town Type to update trnaslations for
	 * @param translations
	 *            Translations set
	 * @return Updated TownType object
	 */
	@Transactional(readOnly = false)
	public TownType update(TownType townType,
			Collection<TownTypeTranslation> translations) {
		Set<TownTypeTranslation> translationList = new HashSet<TownTypeTranslation>(
				translations.size());
		List<TownTypeTranslation> translationsToDelete = new ArrayList<TownTypeTranslation>(
				translations.size());
		boolean hasDefaultLangTranslation = false;
		for (TownTypeTranslation translation : translations) {
			if (StringUtils.isNotBlank(translation.getName())) {
				translationList.add(translation);
				hasDefaultLangTranslation = hasDefaultLangTranslation
						|| translation.getLang().isDefault();
			} else if (translation.getId() != null) {
				translationsToDelete.add(translation);
			}
		}
		if (!hasDefaultLangTranslation) {
			throw new IllegalArgumentException(
					"No default language town type translation");
		}

		townType.setTranslations(translationList);

		for (TownTypeTranslation translation : translationList) {
			if (translation.getId() == null) {
				townTypeTranslationDao.create(translation);
			} else {
				townTypeTranslationDao.update(translation);
			}
		}
		for (TownTypeTranslation translation : translationsToDelete) {
			townTypeTranslationDao.delete(translation);
		}

		if (log.isDebugEnabled()) {
			log.debug("Updated StreetType: " + townType);
		}

		return townType;
	}

	/**
	 * Disable TownTypes TODO: check if there are any towns with specified type
	 * and reject operation
	 * 
	 * @param townTypes
	 *            TownTypes to disable
	 */
	@Transactional(readOnly = false)
	public void disable(Collection<TownType> townTypes) {
		if (log.isDebugEnabled()) {
			log.info(townTypes.size() + " types to disable");
		}
		for (TownType townType : townTypes) {
			townType.setStatus(TownType.STATUS_DISABLED);
			townTypeDao.update(townType);
			if (log.isDebugEnabled()) {
				log.info("Disabled: " + townType);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public TownTypeFilter initFilter(TownTypeFilter townTypeFilter,
			Locale locale) throws FlexPayException {
		List<TownTypeTranslation> translations = getTranslations(locale);

		if (townTypeFilter == null) {
			townTypeFilter = new TownTypeFilter();
		}
		townTypeFilter.setNames(translations);

		if (townTypeFilter.getSelectedId() == null) {
			if (translations.size() == 0) {
				throw new FlexPayException("No town types", "ab.no_town_types");
			}
			townTypeFilter.setSelectedId(translations.get(0).getId());
		}
		return townTypeFilter;
	}

	/**
	 * Get a list of available town types
	 * 
	 * @return List of TownType
	 */
	public List<TownType> getTownTypes() {
		return townTypeDao.listTownTypes(TownType.STATUS_ACTIVE);
	}

	/**
	 * Get a list of available town types
	 * 
	 * @return List of TownType
	 */
	public List<TownType> getEntities() {
		return townTypeDao.listTownTypes(TownType.STATUS_ACTIVE);
	}

	/**
	 * Setter for property 'townTypeDao'.
	 * 
	 * @param townTypeDao
	 *            Value to set for property 'townTypeDao'.
	 */
	public void setTownTypeDao(TownTypeDao townTypeDao) {
		this.townTypeDao = townTypeDao;
	}

	/**
	 * Setter for property 'townTypeTranslationDao'.
	 * 
	 * @param townTypeTranslationDao
	 *            Value to set for property 'townTypeTranslationDao'.
	 */
	public void setTownTypeTranslationDao(
			TownTypeTranslationDao townTypeTranslationDao) {
		this.townTypeTranslationDao = townTypeTranslationDao;
	}
}
