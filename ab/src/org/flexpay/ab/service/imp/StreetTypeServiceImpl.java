package org.flexpay.ab.service.imp;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.flexpay.ab.dao.StreetTypeDao;
import org.flexpay.ab.dao.StreetTypeTranslationDao;
import org.flexpay.ab.persistence.StreetType;
import org.flexpay.ab.persistence.StreetTypeTranslation;
import org.flexpay.ab.persistence.filters.StreetTypeFilter;
import org.flexpay.ab.service.StreetTypeService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Translation;
import org.flexpay.common.util.LanguageUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional (readOnly = true, rollbackFor = Exception.class)
public class StreetTypeServiceImpl implements StreetTypeService {

	@NonNls
	private Logger log = Logger.getLogger(getClass());

	private StreetTypeDao streetTypeDao;
	private StreetTypeTranslationDao streetTypeTranslationDao;

	/**
	 * Create StreetType
	 *
	 * @param translations StreetType names translations
	 * @return created StreetType object
	 */
	@Transactional (readOnly = false)
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

		streetTypeDao.create(streetType);
		for (StreetTypeTranslation translation : translationSet) {
			translation.setTranslatable(streetType);
			streetTypeTranslationDao.create(translation);
		}

		if (log.isDebugEnabled()) {
			log.debug("Created StreetType: " + streetType);
		}

		return streetType;
	}

	/**
	 * Get StreetType translations for specified locale, if translation is not found check for translation in default locale
	 *
	 * @param locale Locale to get translations for
	 * @return List of StreetTypes
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if failure occurs
	 */
	public List<StreetTypeTranslation> getTranslations(Locale locale)
			throws FlexPayException {

		log.debug("Getting list of StreetTypes");

		Language language = LanguageUtil.getLanguage(locale);
		Language defaultLang = ApplicationConfig.getDefaultLanguage();
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
	 * @param id StreetType key
	 * @return StreetType object, or <code>null</code> if object not found
	 */
	public StreetType read(Long id) {
		return streetTypeDao.readFull(id);
	}

	/**
	 * Update street type translations
	 *
	 * @param streetType   Street Type to update trnaslations for
	 * @param translations Translations set
	 * @return Updated StreetType object
	 */
	@Transactional (readOnly = false)
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
	 * Disable StreetTypes TODO: check if there are any streets with specified type and reject operation
	 *
	 * @param streetTypes StreetTypes to disable
	 */
	@Transactional (readOnly = false)
	public void disable(Collection<StreetType> streetTypes) {
		log.info(streetTypes.size() + " types to disable");
		for (StreetType streetType : streetTypes) {
			streetType.setStatus(StreetType.STATUS_DISABLED);
			streetTypeDao.update(streetType);
			log.info("Disabled: " + streetType);
		}
	}

	@Transactional (readOnly = true)
	@Nullable
	public StreetType findTypeByName(@NotNull String typeName) throws FlexPayException {
		for (StreetType type : getEntities()) {
			for (Translation ourType : type.getTranslations()) {
				if (log.isDebugEnabled()) {
					log.debug("Internal street type : " + ourType.getName());
				}

				if (ourType.getName().equalsIgnoreCase(typeName)) {
					return type;
				}
			}
		}

		return null;
	}

	/**
	 * Initialize street type filter
	 *
	 * @param streetTypeFilter Filter to init
	 * @param locale		   Locale to get filter translations in
	 * @throws FlexPayException if failure occurs
	 */
	public void initFilter(StreetTypeFilter streetTypeFilter, Locale locale)
			throws FlexPayException {
		List<StreetTypeTranslation> translations = getTranslations(locale);
		streetTypeFilter.setNames(translations);
	}

	/**
	 * Update or create Entity
	 *
	 * @param streetType Entity to save
	 * @return Saved instance
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	public StreetType save(@NotNull StreetType streetType) throws FlexPayExceptionContainer {
		validate(streetType);
		if (streetType.isNew()) {
			streetType.setId(null);
			streetTypeDao.create(streetType);
		} else {
			streetTypeDao.update(streetType);
		}

		return streetType;
	}

	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	private void validate(StreetType type) throws FlexPayExceptionContainer {
		FlexPayExceptionContainer container = new FlexPayExceptionContainer();

		boolean defaultLangTranslationFound = false;
		for (StreetTypeTranslation translation : type.getTranslations()) {
			if (translation.getLang().isDefault() && StringUtils.isNotEmpty(translation.getName())) {
				defaultLangTranslationFound = true;
			}
		}

		if (!defaultLangTranslationFound) {
			container.addException(new FlexPayException(
					"No default lang translation", "error.no_default_translation"));
		}

		// todo check if there is already a type with a specified name

		if (container.isNotEmpty()) {
			throw container;
		}
	}

	/**
	 * Get a list of available street types
	 *
	 * @return List of StreetType
	 */
	@NotNull
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
