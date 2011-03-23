package org.flexpay.ab.service.impl;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.dao.TownTypeDao;
import org.flexpay.ab.persistence.TownType;
import org.flexpay.ab.persistence.TownTypeTranslation;
import org.flexpay.ab.persistence.filters.TownTypeFilter;
import org.flexpay.ab.service.TownTypeService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.history.ModificationListener;
import org.flexpay.common.service.internal.SessionUtils;
import org.flexpay.common.util.TranslationUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.list;

@Transactional (readOnly = true)
public class TownTypeServiceImpl implements TownTypeService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private TownTypeDao townTypeDao;

	private SessionUtils sessionUtils;
	private ModificationListener<TownType> modificationListener;


	/**
	 * Read TownType object by its unique id
	 *
	 * @param stub Town type stub
	 * @return TownType object, or <code>null</code> if object not found
	 */
	@Nullable
	@Override
	public TownType readFull(@NotNull Stub<TownType> stub) {
		return townTypeDao.readFull(stub.getId());
	}

	/**
	 * Get a list of available town types
	 *
	 * @return List of TownTypes
	 */
	@NotNull
	@Override
	public List<TownType> getEntities() {
		return townTypeDao.listTownTypes(TownType.STATUS_ACTIVE);
	}

	/**
	 * Disable town types
	 *
	 * @param townTypeIds IDs of town types to disable
	 */
	@Transactional (readOnly = false)
	@Override
	public void disable(@NotNull Collection<Long> townTypeIds) {

		for (Long id : townTypeIds) {
			if (id == null) {
				log.warn("Null id in collection of town type ids for disable");
				continue;
			}
			TownType townType = townTypeDao.read(id);
			if (townType == null) {
				log.warn("Can't get town type with id {} from DB", id);
				continue;
			}
			townType.disable();
			townTypeDao.update(townType);

			modificationListener.onDelete(townType);
			log.debug("Town type disabled: {}", townType);
		}

	}

	/**
	 * Create town type
	 *
	 * @param townType Town type to save
	 * @return Saved instance of town type
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Transactional (readOnly = false)
	@NotNull
	@Override
	public TownType create(@NotNull TownType townType) throws FlexPayExceptionContainer {

		validate(townType);
		townType.setId(null);
		townTypeDao.create(townType);

		modificationListener.onCreate(townType);

		return townType;
	}

	/**
	 * Update or create town type
	 *
	 * @param townType Town type to save
	 * @return Saved instance of town type
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	@Transactional (readOnly = false)
	@NotNull
	@Override
	public TownType update(@NotNull TownType townType) throws FlexPayExceptionContainer {

		validate(townType);

		TownType oldType = readFull(stub(townType));
		if (oldType == null) {
			throw new FlexPayExceptionContainer(
					new FlexPayException("No town type found to update " + townType));
		}
		sessionUtils.evict(oldType);
		modificationListener.onUpdate(oldType, townType);

		townTypeDao.update(townType);

		return townType;
	}

	/**
	 * Validate town type before save
	 *
	 * @param type TownType object to validate
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	private void validate(@NotNull TownType type) throws FlexPayExceptionContainer {

		FlexPayExceptionContainer container = new FlexPayExceptionContainer();

		boolean defaultLangNameFound = false;
		boolean defaultLangShortNameFound = false;

		for (TownTypeTranslation translation : type.getTranslations()) {

			Language lang = translation.getLang();
			String name = translation.getName();
			String shortName = translation.getShortName();
			boolean nameNotEmpty = StringUtils.isNotEmpty(name);
			boolean shortNameNotEmpty = StringUtils.isNotEmpty(shortName);

			if (lang.isDefault()) {
				defaultLangNameFound = nameNotEmpty;
				defaultLangShortNameFound = shortNameNotEmpty;
			}

			if (nameNotEmpty) {
				List<TownType> types = townTypeDao.findByNameAndLanguage(name, lang.getId());
				if (!types.isEmpty() && !types.get(0).getId().equals(type.getId())) {
					container.addException(new FlexPayException(
							"Name \"" + name + "\" is already use", "ab.error.name_is_already_use", name));
				}
			}
			
			if (shortNameNotEmpty) {
				List<TownType> types = townTypeDao.findByShortNameAndLanguage(shortName, lang.getId());
				if (!types.isEmpty() && !types.get(0).getId().equals(type.getId())) {
					container.addException(new FlexPayException(
							"Short name \"" + shortName + "\" is already use", "ab.error.short_name_is_already_use", shortName));
				}
			}

		}

		if (!defaultLangNameFound) {
			container.addException(new FlexPayException(
					"No default language translation", "ab.error.town_type.full_name_is_required"));
		}
		if (!defaultLangShortNameFound) {
			container.addException(new FlexPayException(
					"No default language translation", "ab.error.town_type.short_name_is_required"));
		}

		if (container.isNotEmpty()) {
			throw container;
		}

	}

	/**
	 * Initialize filter
	 *
	 * @param townTypeFilter filter to init
	 * @param locale Locale to get names in
	 * @return initialized filter
	 * @throws FlexPayException if failure occurs
	 */
	@NotNull
	@Override
	public TownTypeFilter initFilter(@Nullable TownTypeFilter townTypeFilter, @NotNull Locale locale) throws FlexPayException {

		List<TownTypeTranslation> translations = getTranslations(locale);
		if (translations.isEmpty()) {
			throw new FlexPayException("No town types", "ab.error.town_type.no_town_types");
		}

		if (townTypeFilter == null) {
			townTypeFilter = new TownTypeFilter();
		}
		townTypeFilter.setNames(translations);

		if (townTypeFilter.getSelectedId() == null) {
			townTypeFilter.setSelectedId(translations.get(0).getId());
		}
		return townTypeFilter;
	}


	/**
	 * Get TownType translations for specified locale,
	 * if translation is not found check for translation in default locale
	 *
	 * @param locale Locale to get translations for
	 * @return List of name translations for all town types
	 */
	@NotNull
	private List<TownTypeTranslation> getTranslations(@NotNull Locale locale) {

		List<TownType> townTypes = getEntities();
		List<TownTypeTranslation> translations = list();

		for (TownType townType : townTypes) {
			TownTypeTranslation translation = TranslationUtil.getTranslation(townType.getTranslations(), locale);
			if (translation == null) {
				log.warn("No name for town type: {}", townType);
				continue;
			}
			translations.add(translation);
		}

		return translations;
	}

	/**
	 * Get all objects
	 *
	 * @return List of all objects
	 */
	@Override
	public List<TownType> getAll() {
		return getEntities();
	}

	@Required
	public void setTownTypeDao(TownTypeDao townTypeDao) {
		this.townTypeDao = townTypeDao;
	}

	@Required
	public void setModificationListener(ModificationListener<TownType> modificationListener) {
		this.modificationListener = modificationListener;
	}

	@Required
	public void setSessionUtils(SessionUtils sessionUtils) {
		this.sessionUtils = sessionUtils;
	}

}
