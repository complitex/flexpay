package org.flexpay.ab.service.impl;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.dao.StreetTypeDao;
import org.flexpay.ab.persistence.StreetType;
import org.flexpay.ab.persistence.StreetTypeTranslation;
import org.flexpay.ab.persistence.filters.StreetTypeFilter;
import org.flexpay.ab.service.StreetTypeService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.history.ModificationListener;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.service.internal.SessionUtils;
import org.flexpay.common.util.TranslationUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.jpa.JpaTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.list;

@Transactional (readOnly = true)
public class StreetTypeServiceImpl implements StreetTypeService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private StreetTypeDao streetTypeDao;

	private CorrectionsService correctionsService;

	private SessionUtils sessionUtils;
	private ModificationListener<StreetType> modificationListener;

	/**
	 * Read StreetType object by its unique id
	 *
	 * @param streetTypeStub Street type stub
	 * @return StreetType object, or <code>null</code> if object not found
	 */
	@Nullable
	@Override
	public StreetType readFull(@NotNull Stub<StreetType> streetTypeStub) {
		return streetTypeDao.readFull(streetTypeStub.getId());
	}

	/**
	 * Get a list of available street types
	 *
	 * @return List of StreetTypes
	 */
	@NotNull
	@Override
	public List<StreetType> getEntities() {
		return streetTypeDao.listStreetTypes(StreetType.STATUS_ACTIVE);
	}

	/**
	 * Get all streetType objects
	 *
	 * @return List of all streetType objects
	 */
	@Override
	public List<StreetType> getAll() {
		return getEntities();
	}

	/**
	 * Disable StreetTypes
	 *
	 * @param streetTypeIds IDs of street types to disable
	 */
	//TODO: check if there are any streets with specified type and reject operation
	@Transactional (readOnly = false)
	@Override
	public void disable(@NotNull Collection<Long> streetTypeIds) {
		for (Long id : streetTypeIds) {
			if (id == null) {
				log.warn("Null id in collection of street type ids for disable");
				continue;
			}
			StreetType streetType = streetTypeDao.read(id);
			if (streetType == null) {
				log.warn("Can't get street type with id {} from DB", id);
				continue;
			}
			streetType.disable();
			streetTypeDao.update(streetType);

			modificationListener.onDelete(streetType);
			log.debug("Street type disabled: {}", streetType);
		}
	}

	/**
	 * Create street type
	 *
	 * @param streetType Street type to save
	 * @return Saved instance of street type
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Transactional (readOnly = false)
	@NotNull
	@Override
	public StreetType create(@NotNull StreetType streetType) throws FlexPayExceptionContainer {

		validate(streetType);
		streetType.setId(null);
		streetTypeDao.create(streetType);

		modificationListener.onCreate(streetType);

		return streetType;
	}

	/**
	 * Update or create street type
	 *
	 * @param streetType Street type to save
	 * @return Saved instance of street type
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	@Transactional (readOnly = false)
	@NotNull
	@Override
	public StreetType update(@NotNull StreetType streetType) throws FlexPayExceptionContainer {

		validate(streetType);

		StreetType old = readFull(stub(streetType));
		if (old == null) {
			throw new FlexPayExceptionContainer(
					new FlexPayException("No street type found to update " + streetType));
		}
		sessionUtils.evict(old);
		modificationListener.onUpdate(old, streetType);

		streetTypeDao.update(streetType);

		return streetType;
	}

	/**
	 * Validate street type before save
	 *
	 * @param type StreetType object to validate
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	private void validate(@NotNull StreetType type) throws FlexPayExceptionContainer {

		FlexPayExceptionContainer container = new FlexPayExceptionContainer();

		boolean defaultLangNameFound = false;
//		boolean defaultLangShortNameFound = false;

		for (StreetTypeTranslation translation : type.getTranslations()) {

			Language lang = translation.getLang();
			String name = translation.getName();
			String shortName = translation.getShortName();
			boolean nameNotEmpty = StringUtils.isNotEmpty(name);
			boolean shortNameNotEmpty = StringUtils.isNotEmpty(shortName);

			if (lang.isDefault()) {
				defaultLangNameFound = nameNotEmpty;
//				defaultLangShortNameFound = shortNameNotEmpty;
			}

			if (nameNotEmpty) {
				List<StreetType> types = streetTypeDao.findByNameAndLanguage(name, lang.getId());
				if (!types.isEmpty() && !types.get(0).getId().equals(type.getId())) {
					container.addException(new FlexPayException(
							"Name \"" + name + "\" is already use", "ab.error.name_is_already_use", name));
				}
			}

			if (shortNameNotEmpty) {
				List<StreetType> types = streetTypeDao.findByShortNameAndLanguage(shortName, lang.getId());
				if (!types.isEmpty() && !types.get(0).getId().equals(type.getId())) {
					container.addException(new FlexPayException(
							"Short name \"" + shortName + "\" is already use", "ab.error.short_name_is_already_use", shortName));
				}
			}

		}

		if (!defaultLangNameFound) {
			container.addException(new FlexPayException(
					"No default language translation", "ab.error.street_type.full_name_is_required"));
		}
//		if (!defaultLangShortNameFound) {
//			container.addException(new FlexPayException(
//					"No default language translation", "ab.error.street_type.short_name_is_required"));
//		}

		if (container.isNotEmpty()) {
			throw container;
		}

	}

	@Nullable
	@Override
	public Stub<StreetType> findTypeByName(@NotNull String typeName) throws FlexPayException {
		for (StreetType type : getEntities()) {
			for (StreetTypeTranslation ourType : type.getTranslations()) {
				if (ourType.getName().equalsIgnoreCase(typeName)
					|| ourType.getShortName().equalsIgnoreCase(typeName)) {

					return stub(type);
				}
			}
		}

		// Try to find general correction by type name
		Stub<StreetType> correction = correctionsService.findCorrection(
				typeName.toUpperCase(), StreetType.class, null);
		if (correction != null) {
			return new Stub<StreetType>(correction.getId());
		}
		correction = correctionsService.findCorrection(
				typeName.toLowerCase(), StreetType.class, null);
		if (correction != null) {
			return new Stub<StreetType>(correction.getId());
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
	@NotNull
	@Override
	public StreetTypeFilter initFilter(@Nullable StreetTypeFilter streetTypeFilter, @NotNull Locale locale) throws FlexPayException {

		List<StreetTypeTranslation> translations = getTranslations(locale);

		if (streetTypeFilter == null) {
			streetTypeFilter = new StreetTypeFilter();
		}
		streetTypeFilter.setNames(translations);

		if (streetTypeFilter.getSelectedId() == null) {
			if (translations.isEmpty()) {
				throw new FlexPayException("No street types", "ab.error.street_type.no_street_types");
			}
			streetTypeFilter.setSelectedId(translations.get(0).getId());
		}

		return streetTypeFilter;
	}

	/**
	 * Get StreetType translations for specified locale,
	 * if translation is not found check for translation in default locale
	 *
	 * @param locale Locale to get translations for
	 * @return List of name translations for all street types
	 */
	@NotNull
	private List<StreetTypeTranslation> getTranslations(@NotNull Locale locale) {

		List<StreetType> streetTypes = getEntities();
		List<StreetTypeTranslation> translations = list();

		for (StreetType type : streetTypes) {
			StreetTypeTranslation translation = TranslationUtil.getTranslation(type.getTranslations(), locale);
			if (translation == null) {
				log.error("No name for street type: {}", type);
				continue;
			}
			translations.add(translation);
		}

		return translations;
	}

    @Override
    public void setJpaTemplate(JpaTemplate jpaTemplate) {
        streetTypeDao.setJpaTemplate(jpaTemplate);
        sessionUtils.setJpaTemplate(jpaTemplate);
        correctionsService.setJpaTemplate(jpaTemplate);
        modificationListener.setJpaTemplate(jpaTemplate);
    }

	@Required
	public void setStreetTypeDao(StreetTypeDao streetTypeDao) {
		this.streetTypeDao = streetTypeDao;
	}

	@Required
	public void setCorrectionsService(CorrectionsService correctionsService) {
		this.correctionsService = correctionsService;
	}

	@Required
	public void setSessionUtils(SessionUtils sessionUtils) {
		this.sessionUtils = sessionUtils;
	}

	@Required
	public void setModificationListener(ModificationListener<StreetType> modificationListener) {
		this.modificationListener = modificationListener;
	}

}
