package org.flexpay.ab.service.imp;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.flexpay.ab.dao.StreetTypeDao;
import org.flexpay.ab.dao.StreetTypeTranslationDao;
import org.flexpay.ab.persistence.StreetType;
import org.flexpay.ab.persistence.StreetTypeTranslation;
import org.flexpay.ab.persistence.filters.StreetTypeFilter;
import org.flexpay.ab.service.StreetTypeService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.util.LanguageUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional (readOnly = true, rollbackFor = Exception.class)
public class StreetTypeServiceImpl implements StreetTypeService {

	@NonNls
	private Logger log = LoggerFactory.getLogger(getClass());

	private StreetTypeDao streetTypeDao;
	private StreetTypeTranslationDao streetTypeTranslationDao;

	private CorrectionsService correctionsService;

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

		log.debug("StreetTypes: {}", streetTypes);

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
		log.debug("Gettting translation: {} : {}", lang.getLangIsoCode(), names);
		for (StreetTypeTranslation translation : names) {
			if (lang.equals(translation.getLang())) {
				log.debug("Found translation: {}", translation);
				return translation;
			}
			if (defaultLang.equals(translation.getLang())) {
				log.debug("Found default translation: {}", translation);
				defaultTranslation = translation;
			}

			log.debug("Translation is invalid: {}", translation);
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
	 * Disable StreetTypes TODO: check if there are any streets with specified type and reject operation
	 *
	 * @param streetTypes StreetTypes to disable
	 */
	@Transactional (readOnly = false)
	public void disable(Collection<StreetType> streetTypes) {
		log.info("{} types to disable", streetTypes.size());
		for (StreetType streetType : streetTypes) {
			streetType.setStatus(StreetType.STATUS_DISABLED);
			streetTypeDao.update(streetType);
			log.info("Disabled: {}", streetType);
		}
	}

	@Transactional (readOnly = true)
	@Nullable
	public StreetType findTypeByName(@NotNull String typeName) throws FlexPayException {
		for (StreetType type : getEntities()) {
			for (StreetTypeTranslation ourType : type.getTranslations()) {

				String fullName = ourType.getName();
				String shortName = ourType.getShortName();
				if (fullName.equalsIgnoreCase(typeName) || shortName.equalsIgnoreCase(typeName)) {
					return type;
				}
			}
		}

		// Try to find general correction by type name
		Stub<StreetType> correction = correctionsService.findCorrection(
				typeName.toUpperCase(), StreetType.class, null);
		if (correction != null) {
			return new StreetType(correction.getId());
		}
		correction = correctionsService.findCorrection(
				typeName.toLowerCase(), StreetType.class, null);
		if (correction != null) {
			return new StreetType(correction.getId());
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
	@Transactional (readOnly = false)
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

	public void setCorrectionsService(CorrectionsService correctionsService) {
		this.correctionsService = correctionsService;
	}
}
