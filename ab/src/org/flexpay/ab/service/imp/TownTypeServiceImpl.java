package org.flexpay.ab.service.imp;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.flexpay.ab.dao.TownTypeDao;
import org.flexpay.ab.dao.TownTypeTranslationDao;
import org.flexpay.ab.persistence.TownType;
import org.flexpay.ab.persistence.TownTypeTranslation;
import org.flexpay.ab.service.TownTypeService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.util.LanguageUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional (readOnly = true, rollbackFor = Exception.class)
public class TownTypeServiceImpl implements TownTypeService {

	private static Logger log = Logger.getLogger(TownTypeServiceImpl.class);

	private TownTypeDao townTypeDao;
	private TownTypeTranslationDao townTypeTranslationDao;

	/**
	 * Create TownType
	 *
	 * @param translations TownType names translations
	 * @return created Country object
	 */
	@Transactional (readOnly = false)
	public TownType create(Collection<TownTypeTranslation> translations) {
		TownType townType = new TownType();

		Set<TownTypeTranslation> translationList = new HashSet<TownTypeTranslation>();
		boolean hasDefaultLangTranslation = false;
		for (TownTypeTranslation translation : translations) {
			if (StringUtils.isNotBlank(translation.getName())) {
				translationList.add(translation);
				hasDefaultLangTranslation =
						hasDefaultLangTranslation || translation.getLang().isDefault();
			}
		}
		if (translationList.isEmpty()) {
			throw new IllegalArgumentException("No town type translations specified");
		}
		if (!hasDefaultLangTranslation) {
			throw new IllegalArgumentException("No default language town type translation");
		}

		townType.setStatus(TownType.STATUS_ACTIVE);
		townType.setTypeTranslations(translationList);

		townTypeDao.create(townType);
		for (TownTypeTranslation translation : translationList) {
			translation.setTownType(townType);
			townTypeTranslationDao.create(translation);
		}

		if (log.isDebugEnabled()) {
			log.debug("Created TownType: " + townType);
		}

		return townType;
	}

	/**
	 * Get TownType translations for specified locale, if translation is not found check for
	 * translation in default locale
	 *
	 * @param locale Locale to get translations for
	 * @return List of TownTypes
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if failure occurs
	 */
	public List<TownTypeTranslation> getTownTypeTranslations(Locale locale)
			throws FlexPayException {

		log.debug("Getting list of TownTypes");

		Language language = LanguageUtil.getLanguage(locale);
		Language defaultLang = ApplicationConfig.getInstance().getDefaultLanguage();
		List<TownType> townTypes = townTypeDao.listTownTypes(TownType.STATUS_ACTIVE);
		List<TownTypeTranslation> translations =
				new ArrayList<TownTypeTranslation>(townTypes.size());

		if (log.isDebugEnabled()) {
			log.debug("TownTypes: " + townTypes);
		}

		for (TownType townType : townTypes) {
			TownTypeTranslation translation = getTypeTranslation(townType, language, defaultLang);
			if (translation == null) {
				log.error("No name for town type: " + language.getLangIsoCode() + " : " +
						  defaultLang.getLangIsoCode() + ", " + townType);
				continue;
			}
			translation.setTranslation(
					LanguageUtil.getLanguageName(translation.getLang(), locale));
			translations.add(translation);
		}

		return translations;
	}

	private TownTypeTranslation getTypeTranslation(
			TownType townType, Language lang, Language defaultLang) {
		TownTypeTranslation defaultTranslation = null;

		Collection<TownTypeTranslation> names = townType.getTypeTranslations();
		log.debug("Gettting translation: " + lang.getLangIsoCode() + " : " + names);
		for (TownTypeTranslation translation : names) {
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
	 * Read TownType object by its unique id
	 *
	 * @param id TownType key
	 * @return TownType object, or <code>null</code> if object not found
	 */
	public TownType read(Long id) {
		return townTypeDao.read(id);
	}

	/**
	 * Update town type translations
	 *
	 * @param townType	 Town Type to update trnaslations for
	 * @param translations Translations set
	 * @return Updated TownType object
	 */
	@Transactional (readOnly = false)
	public TownType update(TownType townType, Collection<TownTypeTranslation> translations) {
		Set<TownTypeTranslation> translationList =
				new HashSet<TownTypeTranslation>(translations.size());
		List<TownTypeTranslation> translationsToDelete =
				new ArrayList<TownTypeTranslation>(translations.size());
		boolean hasDefaultLangTranslation = false;
		for (TownTypeTranslation translation : translations) {
			if (StringUtils.isNotBlank(translation.getName())) {
				translationList.add(translation);
				hasDefaultLangTranslation =
						hasDefaultLangTranslation || translation.getLang().isDefault();
			} else if (translation.getId() != null) {
				translationsToDelete.add(translation);
			}
		}
		if (!hasDefaultLangTranslation) {
			throw new IllegalArgumentException("No default language town type translation");
		}

		townType.setTypeTranslations(translationList);

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
			log.debug("Updated TownType: " + townType);
		}

		return townType;
	}

	/**
	 * Disable TownTypes TODO: check if there are any towns with specified type and reject
	 * operation
	 *
	 * @param townTypes TownTypes to disable
	 */
	@Transactional (readOnly = false)
	public void disable(Collection<TownType> townTypes) {
		log.info(townTypes.size() + " types to disable");
		for (TownType townType : townTypes) {
			townType.setStatus(TownType.STATUS_DISABLED);
			townTypeDao.update(townType);
			log.info("Diasabled: " + townType);
		}
	}

	/**
	 * Get a list of available town types
	 *
	 * @return List of TownType
	 */
	public List<TownType> getTownTypes() {
		return townTypeDao.listTownTypes(TownType.STATUS_ACTIVE);
	}

	public void setTownTypeDao(TownTypeDao townTypeDao) {
		this.townTypeDao = townTypeDao;
	}

	public void setTownTypeTranslationDao(TownTypeTranslationDao townTypeTranslationDao) {
		this.townTypeTranslationDao = townTypeTranslationDao;
	}
}
