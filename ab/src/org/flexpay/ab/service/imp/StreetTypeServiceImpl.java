package org.flexpay.ab.service.imp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.flexpay.ab.dao.StreetTypeDao;
import org.flexpay.ab.dao.StreetTypeTranslationDao;
import org.flexpay.ab.persistence.StreetType;
import org.flexpay.ab.persistence.StreetTypeTranslation;
import org.flexpay.ab.service.StreetTypeService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.util.LanguageUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true, rollbackFor = Exception.class)
public class StreetTypeServiceImpl implements
		StreetTypeService {

	private static Logger log = Logger.getLogger(StreetTypeServiceImpl.class);

	private StreetTypeDao streetTypeDao;
	private StreetTypeTranslationDao streetTypeTranslationDao;

	/**
	 * Create StreetType
	 * 
	 * @param translations
	 *            StreetType names translations
	 * @return created StreetType object
	 */
	@Transactional(readOnly = false)
	public StreetType create(Collection<StreetTypeTranslation> translations)
			throws FlexPayException {
		StreetType streetType = new StreetType();

		Set<StreetTypeTranslation> translationSet = new HashSet<StreetTypeTranslation>();
		boolean hasDefaultLangTranslation = false;
		for (StreetTypeTranslation translation : translations) {
			if (StringUtils.isNotBlank(translation.getName())) {
				translationSet.add(translation);
				hasDefaultLangTranslation =
						hasDefaultLangTranslation || translation.getLang().isDefault();
			}
		}
		if (!hasDefaultLangTranslation) {
			throw new FlexPayException("No default language street type translation");
		}

		streetType.setStatus(StreetType.STATUS_ACTIVE);
		streetType.setTranslations(translationSet);

		streetTypeDao.create(streetType);
		for (StreetTypeTranslation translation : translationSet) {
			translation.setStreetType(streetType);
			streetTypeTranslationDao.create(translation);
		}

		if (log.isDebugEnabled()) {
			log.debug("Created StreetType: " + streetType);
		}

		return streetType;
	}

	/**
	 * Get StreetType translations for specified locale, if translation is not
	 * found check for translation in default locale
	 * 
	 * @param locale
	 *            Locale to get translations for
	 * @return List of StreetTypes
	 * @throws org.flexpay.common.exception.FlexPayException
	 *             if failure occurs
	 */
	public List<StreetTypeTranslation> getTranslations(Locale locale)
			throws FlexPayException {

		log.debug("Getting list of StreetTypes");

		Language language = LanguageUtil.getLanguage(locale);
		Language defaultLang = ApplicationConfig.getInstance()
				.getDefaultLanguage();
		List<StreetType> streetTypes = streetTypeDao
				.listStreetTypes(StreetType.STATUS_ACTIVE);
		List<StreetTypeTranslation> translations = new ArrayList<StreetTypeTranslation>(
				streetTypes.size());

		if (log.isDebugEnabled()) {
			log.debug("StreetTypes: " + streetTypes);
		}

		for (StreetType streetType : streetTypes) {
			StreetTypeTranslation translation = getTypeTranslation(streetType,
					language, defaultLang);
			if (translation == null) {
				log.error("No name for street type: "
						+ language.getLangIsoCode() + " : "
						+ defaultLang.getLangIsoCode() + ", " + streetType);
				continue;
			}
			translations.add(translation);
		}

		return translations;
	}

	private StreetTypeTranslation getTypeTranslation(StreetType streetType,
			Language lang, Language defaultLang) {
		StreetTypeTranslation defaultTranslation = null;

		Collection<StreetTypeTranslation> names = streetType
				.getTranslations();
		log.debug("Gettting translation: " + lang.getLangIsoCode() + " : "
				+ names);
		for (StreetTypeTranslation translation : names) {
			if (lang.equals(translation.getLang())) {
				log.debug("Found translation: " + translation);
				return translation;
			}
			if (defaultLang.equals(translation.getLang())) {
				log.debug("Found default translation: " + translation);
				defaultTranslation = translation;
			}

			log.debug("Translation is invalid: " + translation);
		}

		return defaultTranslation;
	}

	/**
	 * Read StreetType object by its unique id
	 * 
	 * @param id
	 *            StreetType key
	 * @return StreetType object, or <code>null</code> if object not found
	 */
	public StreetType read(Long id) {
		return streetTypeDao.read(id);
	}

	/**
	 * Update street type translations
	 * 
	 * @param streetType
	 *            Street Type to update trnaslations for
	 * @param translations
	 *            Translations set
	 * @return Updated StreetType object
	 */
	@Transactional(readOnly = false)
	public StreetType update(StreetType streetType,
			Collection<StreetTypeTranslation> translations) {
		Set<StreetTypeTranslation> translationList = new HashSet<StreetTypeTranslation>(
				translations.size());
		List<StreetTypeTranslation> translationsToDelete = new ArrayList<StreetTypeTranslation>(
				translations.size());
		boolean hasDefaultLangTranslation = false;
		for (StreetTypeTranslation translation : translations) {
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
					"No default language street type translation");
		}

		streetType.setTranslations(translationList);

		for (StreetTypeTranslation translation : translationList) {
			if (translation.getId() == null) {
				streetTypeTranslationDao.create(translation);
			} else {
				streetTypeTranslationDao.update(translation);
			}
		}
		for (StreetTypeTranslation translation : translationsToDelete) {
			streetTypeTranslationDao.delete(translation);
		}

		if (log.isDebugEnabled()) {
			log.debug("Updated StreetType: " + streetType);
		}

		return streetType;
	}

	/**
	 * Disable StreetTypes TODO: check if there are any streets with specified
	 * type and reject operation
	 * 
	 * @param streetTypes
	 *            StreetTypes to disable
	 */
	@Transactional(readOnly = false)
	public void disable(Collection<StreetType> streetTypes) {
		log.info(streetTypes.size() + " types to disable");
		for (StreetType streetType : streetTypes) {
			streetType.setStatus(StreetType.STATUS_DISABLED);
			streetTypeDao.update(streetType);
			log.info("Disabled: " + streetType);
		}
	}

	/**
	 * Get a list of available street types
	 * 
	 * @return List of StreetType
	 */
	public List<StreetType> getEntities() {
		return streetTypeDao.listStreetTypes(StreetType.STATUS_ACTIVE);
	}

	public void setStreetTypeDao(StreetTypeDao streetTypeDao) {
		this.streetTypeDao = streetTypeDao;
	}

	public void setStreetTypeTranslationDao(
			StreetTypeTranslationDao streetTypeTranslationDao) {
		this.streetTypeTranslationDao = streetTypeTranslationDao;
	}
}
