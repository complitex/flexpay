package org.flexpay.ab.service.imp;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.flexpay.ab.dao.IdentityTypeDao;
import org.flexpay.ab.dao.IdentityTypeTranslationDao;
import org.flexpay.ab.persistence.IdentityType;
import org.flexpay.ab.persistence.IdentityTypeTranslation;
import org.flexpay.ab.service.IdentityTypeService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Language;
import static org.flexpay.common.util.CollectionUtils.list;
import org.flexpay.common.util.LanguageUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

@Transactional (readOnly = true, rollbackFor = Exception.class)
public class IdentityTypeServiceImpl implements IdentityTypeService {

	@NonNls
	private Logger log = Logger.getLogger(getClass());

	private IdentityTypeDao identityTypeDao;
	private IdentityTypeTranslationDao identityTypeTranslationDao;

	private List<IdentityType> identityTypes;

	/**
	 * Get IdentityType translations for specified locale, if translation is not found check for translation in default
	 * locale
	 *
	 * @param locale Locale to get translations for
	 * @return List of IdentityTypes
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if failure occurs
	 */
	public List<IdentityTypeTranslation> getTranslations(Locale locale)
			throws FlexPayException {

		log.debug("Getting list of IdentityTypes");

		Language language = LanguageUtil.getLanguage(locale);
		Language defaultLang = ApplicationConfig.getDefaultLanguage();
		List<IdentityType> types = identityTypeDao.listIdentityTypes(IdentityType.STATUS_ACTIVE);
		List<IdentityTypeTranslation> translations = list();

		if (log.isDebugEnabled()) {
			log.debug("IdentityTypes: " + types);
		}

		for (IdentityType identityType : types) {
			IdentityTypeTranslation translation = getTypeTranslation(identityType, language, defaultLang);
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

		Collection<IdentityTypeTranslation> names = identityType.getTranslations();
		for (IdentityTypeTranslation translation : names) {
			if (lang.equals(translation.getLang())) {
				return translation;
			}
			if (defaultLang.equals(translation.getLang())) {
				defaultTranslation = translation;
			}
		}

		return defaultTranslation;
	}

	/**
	 * Read IdentityType object by its unique id
	 *
	 * @param id IdentityType key
	 * @return IdentityType object, or <code>null</code> if object not found
	 */
	@Nullable
	public IdentityType read(@NotNull Long id) {
		if (identityTypes != null) {
			for (IdentityType type : identityTypes) {
				if (id.equals(type.getId())) {
					return type;
				}
			}
		}
		return identityTypeDao.readFull(id);
	}

	/**
	 * Disable IdentityTypes TODO: check if there are any documents with specified type and reject operation
	 *
	 * @param identityTypes IdentityTypes to disable
	 */
	@Transactional (readOnly = false)
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
	 * Update or create Entity
	 *
	 * @param identityType Entity to save
	 * @return Saved instance
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Transactional (readOnly = false)
	public IdentityType save(@NotNull IdentityType identityType) throws FlexPayExceptionContainer {
		validate(identityType);
		if (identityType.isNew()) {
			identityType.setId(null);
			identityTypeDao.create(identityType);
		} else {
			identityTypeDao.update(identityType);
		}

		return identityType;
	}

	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	private void validate(IdentityType type) throws FlexPayExceptionContainer {
		FlexPayExceptionContainer container = new FlexPayExceptionContainer();

		boolean defaultLangTranslationFound = false;
		for (IdentityTypeTranslation translation : type.getTranslations()) {
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
	 * Find identity type by enum id
	 *
	 * @param typeId Type id
	 * @return IdentityType if found, or <code>null</code> otherwise
	 */
	public IdentityType getType(int typeId) {
		initTypesCache();
		for (IdentityType type : identityTypes) {
			if (type.getTypeId() == typeId) {
				return type;
			}
		}

		return null;
	}

	/**
	 * @deprecated replace with external caching
	 */
	private void initTypesCache() {
		if (identityTypes == null) {
			identityTypes = getEntities();
		}
	}

	/**
	 * Find identity type by name
	 *
	 * @param typeName Type name
	 * @return IdentityType if found, or <code>null</code> otherwise
	 */
	public IdentityType getType(String typeName) {
		if (IdentityType.TYPE_NAME_PASSPORT.equals(typeName)) {
			return getType(IdentityType.TYPE_PASSPORT);
		}

		if (IdentityType.TYPE_NAME_FOREIGN_PASSPORT.equals(typeName)) {
			return getType(IdentityType.TYPE_FOREIGN_PASSPORT);
		}

		return getType(IdentityType.TYPE_PASSPORT);
	}

	/**
	 * Get available identity types
	 *
	 * @return Identity types
	 */
	public Collection<IdentityType> getIdentityTypes() {
		initTypesCache();
		return identityTypes;
	}

	/**
	 * Get a list of available identity types
	 *
	 * @return List of IdentityType
	 */
	@NotNull
	public List<IdentityType> getEntities() {
		identityTypes = identityTypeDao.listIdentityTypes(IdentityType.STATUS_ACTIVE);
		return identityTypes;
	}

	public void setIdentityTypeDao(IdentityTypeDao identityTypeDao) {
		this.identityTypeDao = identityTypeDao;
	}

	public void setIdentityTypeTranslationDao(
			IdentityTypeTranslationDao identityTypeTranslationDao) {
		this.identityTypeTranslationDao = identityTypeTranslationDao;
	}
}