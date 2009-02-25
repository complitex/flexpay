package org.flexpay.ab.service.imp;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.dao.TownTypeDao;
import org.flexpay.ab.persistence.TownType;
import org.flexpay.ab.persistence.TownTypeTranslation;
import org.flexpay.ab.persistence.filters.TownTypeFilter;
import org.flexpay.ab.service.TownTypeService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.util.TranslationUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

@Transactional (readOnly = true)
public class TownTypeServiceImpl implements TownTypeService {

	@NonNls
	private final Logger log = LoggerFactory.getLogger(getClass());

	private TownTypeDao townTypeDao;

	/**
	 * Get TownType translations for specified locale, if translation is not found check for translation in default locale
	 *
	 * @param locale Locale to get translations for
	 * @return List of TownTypes
	 * @throws FlexPayException if failure occurs
	 */
	public List<TownTypeTranslation> getTranslations(Locale locale)
			throws FlexPayException {

		log.debug("Getting list of TownTypes");
		List<TownType> townTypes = townTypeDao
				.listTownTypes(TownType.STATUS_ACTIVE);
		List<TownTypeTranslation> translations = new ArrayList<TownTypeTranslation>(
				townTypes.size());

		log.debug("TownTypes: {}", townTypes);


		for (TownType townType : townTypes) {
			TownTypeTranslation translation = TranslationUtil.getTranslation(
					townType.getTranslations(), locale);
			if (translation == null) {
				log.error("No name for town type: {}", townType);
				continue;
			}
			translations.add(translation);
		}

		return translations;
	}

	/**
	 * Update or create Entity
	 *
	 * @param type Entity to save
	 * @return Saved instance
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Transactional (readOnly = false)
	public TownType save(@NotNull TownType type) throws FlexPayExceptionContainer {
		validate(type);
		if (type.isNew()) {
			type.setId(null);
			townTypeDao.create(type);
		} else {
			townTypeDao.update(type);
		}

		return type;

	}

	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	private void validate(TownType type) throws FlexPayExceptionContainer {
		FlexPayExceptionContainer container = new FlexPayExceptionContainer();

		boolean defaultLangTranslationFound = false;
		for (TownTypeTranslation translation : type.getTranslations()) {
			if (translation.getLang().isDefault() && StringUtils.isNotEmpty(translation.getName())) {
				defaultLangTranslationFound = true;
			}
		}

		if (!defaultLangTranslationFound) {
			container.addException(new FlexPayException(
					"No default translation", "error.no_default_translation"));
		}

		// todo check if there is already a type with a specified name

		if (container.isNotEmpty()) {
			throw container;
		}
	}

	/**
	 * Read TownType object by its unique id
	 *
	 * @param id TownType key
	 * @return TownType object, or <code>null</code> if object not found
	 */
	public TownType read(Long id) {
		return townTypeDao.readFull(id);
	}

	/**
	 * Disable TownTypes TODO: check if there are any towns with specified type and reject operation
	 *
	 * @param townTypes TownTypes to disable
	 */
	@Transactional (readOnly = false)
	public void disable(Collection<TownType> townTypes) {
		log.info("{} types to disable", townTypes.size());
		for (TownType townType : townTypes) {
			townType.setStatus(TownType.STATUS_DISABLED);
			townTypeDao.update(townType);
			log.info("Disabled: {}", townType);
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
	@NotNull
	public List<TownType> getEntities() {
		return townTypeDao.listTownTypes(TownType.STATUS_ACTIVE);
	}

	@Required
	public void setTownTypeDao(TownTypeDao townTypeDao) {
		this.townTypeDao = townTypeDao;
	}

}
