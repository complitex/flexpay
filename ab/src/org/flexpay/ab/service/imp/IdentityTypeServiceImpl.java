package org.flexpay.ab.service.imp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.flexpay.ab.dao.IdentityTypeDao;
import org.flexpay.ab.dao.IdentityTypeTranslationDao;
import org.flexpay.ab.persistence.IdentityType;
import org.flexpay.ab.persistence.IdentityTypeTranslation;
import org.flexpay.ab.persistence.StreetType;
import org.flexpay.ab.service.IdentityTypeService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.util.LanguageUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true, rollbackFor = Exception.class)
public class IdentityTypeServiceImpl implements IdentityTypeService {

	private static Logger log = Logger.getLogger(IdentityTypeServiceImpl.class);

	private IdentityTypeDao identityTypeDao;
	private IdentityTypeTranslationDao identityTypeTranslationDao;

	private List<IdentityType> identityTypes;

	/**
	 * Create IdentityType
	 * 
	 * @param translations
	 *            IdentityType names translations
	 * @return created StreetType object
	 */
	@Transactional(readOnly = false)
	public IdentityType create(Collection<IdentityTypeTranslation> translations)
			throws FlexPayException {
		IdentityType identityType = new IdentityType();

		Set<IdentityTypeTranslation> translationSet = new HashSet<IdentityTypeTranslation>();
		boolean hasDefaultLangTranslation = false;
		for (IdentityTypeTranslation translation : translations) {
			if (StringUtils.isNotBlank(translation.getName())) {
				translationSet.add(translation);
				hasDefaultLangTranslation =
						hasDefaultLangTranslation || translation.getLang().isDefault();
			}
		}
		if (!hasDefaultLangTranslation) {
			throw new FlexPayException("No default language identity type translation");
		}

		identityType.setStatus(StreetType.STATUS_ACTIVE);
		identityType.setTranslations(translationSet);

		identityTypeDao.create(identityType);
		for (IdentityTypeTranslation translation : translationSet) {
			translation.setTranslatable(identityType);
			identityTypeTranslationDao.create(translation);
		}

		if (log.isDebugEnabled()) {
			log.debug("Created IdentityType: " + identityType);
		}

		return identityType;
	}

	/**
	 * Get IdentityType translations for specified locale, if translation is not
	 * found check for translation in default locale
	 * 
	 * @param locale
	 *            Locale to get translations for
	 * @return List of IdentityTypes
	 * @throws org.flexpay.common.exception.FlexPayException
	 *             if failure occurs
	 */
	public List<IdentityTypeTranslation> getTranslations(Locale locale)
			throws FlexPayException {

		log.debug("Getting list of StreetTypes");

		Language language = LanguageUtil.getLanguage(locale);
		Language defaultLang = ApplicationConfig.getInstance()
				.getDefaultLanguage();
		List<IdentityType> identityTypes = identityTypeDao
				.listIdentityTypes(IdentityType.STATUS_ACTIVE);
		List<IdentityTypeTranslation> translations = new ArrayList<IdentityTypeTranslation>(
				identityTypes.size());

		if (log.isDebugEnabled()) {
			log.debug("IdentityTypes: " + identityTypes);
		}

		for (IdentityType identityType : identityTypes) {
			IdentityTypeTranslation translation = getTypeTranslation(identityType,
					language, defaultLang);
			if (translation == null) {
				log.error("No name for identity type: "
						+ language.getLangIsoCode() + " : "
						+ defaultLang.getLangIsoCode() + ", " + identityType);
				continue;
			}
			translations.add(translation);
		}

		return translations;
	}

	private IdentityTypeTranslation getTypeTranslation(IdentityType identityType,
			Language lang, Language defaultLang) {
		IdentityTypeTranslation defaultTranslation = null;

		Collection<IdentityTypeTranslation> names = identityType
				.getTranslations();
		log.debug("Gettting translation: " + lang.getLangIsoCode() + " : "
				+ names);
		for (IdentityTypeTranslation translation : names) {
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
	 * Read IdentityType object by its unique id
	 * 
	 * @param id
	 *            IdentityType key
	 * @return IdentityType object, or <code>null</code> if object not found
	 */
	public IdentityType read(Long id) {
		return identityTypeDao.readFull(id);
	}

	/**
	 * Update identity type translations
	 * 
	 * @param identityType
	 *            Identity Type to update trnaslations for
	 * @param translations
	 *            Translations set
	 * @return Updated IdentityType object
	 */
	@Transactional(readOnly = false)
	public IdentityType update(IdentityType identityType,
			Collection<IdentityTypeTranslation> translations) {
		Set<IdentityTypeTranslation> translationList = new HashSet<IdentityTypeTranslation>(
				translations.size());
		List<IdentityTypeTranslation> translationsToDelete = new ArrayList<IdentityTypeTranslation>(
				translations.size());
		boolean hasDefaultLangTranslation = false;
		for (IdentityTypeTranslation translation : translations) {
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
					"No default language identity type translation");
		}

		identityType.setTranslations(translationList);

		for (IdentityTypeTranslation translation : translationList) {
			if (translation.getId() == null) {
				identityTypeTranslationDao.create(translation);
			} else {
				identityTypeTranslationDao.update(translation);
			}
		}
		for (IdentityTypeTranslation translation : translationsToDelete) {
			identityTypeTranslationDao.delete(translation);
		}

		if (log.isDebugEnabled()) {
			log.debug("Updated IdentityType: " + identityType);
		}

		return identityType;
	}

	/**
	 * Disable IdentityTypes
	 * TODO: check if there are any streets with specified type and reject operation
	 * 
	 * @param identityTypes
	 *            IdentityTypes to disable
	 */
	@Transactional(readOnly = false)
	public void disable(Collection<IdentityType> identityTypes) {
		log.info(identityTypes.size() + " types to disable");
		for (IdentityType identityType : identityTypes) {
			identityType.setStatus(IdentityType.STATUS_DISABLED);
			identityTypeDao.update(identityType);

			if (log.isInfoEnabled()) {
				log.info("Disabled: " + identityType);
			}
		}
	}

	/**
	 * Find identity type by enum id
	 *
	 * @param typeId Type id
	 * @return IdentityType if found, or <code>null</code> otherwise
	 */
	public IdentityType getType(int typeId) {
		if (identityTypes == null) {
			identityTypes = getEntities();
		}
		for (IdentityType type : identityTypes) {
			if (type.getTypeId() == typeId) {
				return type;
			}
		}

		return null;
	}

	/**
	 * Find identity type by name
	 *
	 * @param typeName Type name
	 * @return IdentityType if found, or <code>null</code> otherwise
	 */
	public IdentityType getType(String typeName) {
		if (IdentityType.TYPE_NAME_PASSPORT.equals(typeName))
			return getType(IdentityType.TYPE_PASSPORT);

		if (IdentityType.TYPE_NAME_FOREIGN_PASSPORT.equals(typeName))
			return getType(IdentityType.TYPE_FOREIGN_PASSPORT);

		return getType(IdentityType.TYPE_PASSPORT);
	}

	/**
	 * Get a list of available identity types
	 * 
	 * @return List of IdentityType
	 */
	public List<IdentityType> getEntities() {
		return identityTypeDao.listIdentityTypes(IdentityType.STATUS_ACTIVE);
	}

	public void setIdentityTypeDao(IdentityTypeDao identityTypeDao) {
		this.identityTypeDao = identityTypeDao;
	}

	public void setIdentityTypeTranslationDao(
			IdentityTypeTranslationDao identityTypeTranslationDao) {
		this.identityTypeTranslationDao = identityTypeTranslationDao;
	}
}