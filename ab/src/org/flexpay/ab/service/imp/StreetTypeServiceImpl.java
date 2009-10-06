package org.flexpay.ab.service.imp;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.dao.StreetTypeDao;
import org.flexpay.ab.persistence.StreetType;
import org.flexpay.ab.persistence.StreetTypeTranslation;
import org.flexpay.ab.persistence.filters.StreetTypeFilter;
import org.flexpay.ab.service.StreetTypeService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.history.ModificationListener;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.service.internal.SessionUtils;
import static org.flexpay.common.util.CollectionUtils.list;
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

@Transactional (readOnly = true, rollbackFor = Exception.class)
public class StreetTypeServiceImpl implements StreetTypeService {

	private Logger log = LoggerFactory.getLogger(getClass());

	private StreetTypeDao streetTypeDao;

	private CorrectionsService correctionsService;

	private SessionUtils sessionUtils;
	private ModificationListener<StreetType> modificationListener;

	/**
	 * Get StreetType translations for specified locale, if translation is not found check for translation in default
	 * locale
	 *
	 * @param locale Locale to get translations for
	 * @return List of StreetTypes
	 */
	private List<StreetTypeTranslation> getTranslations(Locale locale) {

		log.debug("Getting list of StreetTypes");

		List<StreetType> streetTypes = getEntities();
		List<StreetTypeTranslation> translations = list();

		log.debug("StreetTypes: {}", streetTypes);

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

	/**
	 * Read StreetType object by its unique id
	 *
	 * @param stub Entity stub
	 * @return StreetType object, or <code>null</code> if object not found
	 */
	@Override
	public StreetType read(Stub<StreetType> stub) {
		return streetTypeDao.readFull(stub.getId());
	}

	/**
	 * Read object by its unique id
	 *
	 * @param stub Object stub
	 * @return object, or <code>null</code> if object not found
	 */
	@Override
	public StreetType readFull(@NotNull Stub<StreetType> stub) {
		return streetTypeDao.readFull(stub.getId());
	}

	/**
	 * Disable StreetTypes TODO: check if there are any streets with specified type and reject operation
	 *
	 * @param streetTypes StreetTypes to disable
	 */
	@Transactional (readOnly = false)
	@Override
	public void disable(Collection<StreetType> streetTypes) {
		log.info("{} types to disable", streetTypes.size());
		for (StreetType streetType : streetTypes) {
			streetType.setStatus(StreetType.STATUS_DISABLED);
			streetTypeDao.update(streetType);

			modificationListener.onDelete(streetType);

			log.info("Disabled: {}", streetType);
		}
	}

	@Transactional (readOnly = false)
	@Override
	public void disableByIds(@NotNull Collection<Long> objectIds) {
		for (Long id : objectIds) {
			StreetType streetType = streetTypeDao.read(id);
			if (streetType != null) {
				streetType.disable();
				streetTypeDao.update(streetType);

				modificationListener.onDelete(streetType);
				log.debug("Disabled: {}", streetType);
			}
		}
	}

	@Nullable
	@Override
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
	@Override
	public StreetTypeFilter initFilter(StreetTypeFilter streetTypeFilter, Locale locale)
			throws FlexPayException {

		List<StreetTypeTranslation> translations = getTranslations(locale);

		if (streetTypeFilter == null) {
			streetTypeFilter = new StreetTypeFilter();
		}
		streetTypeFilter.setNames(translations);

		if (streetTypeFilter.getSelectedId() == null) {
			if (translations.isEmpty()) {
				throw new FlexPayException("No street types", "ab.no_street_types");
			}
			streetTypeFilter.setSelectedId(translations.get(0).getId());
		}
		return streetTypeFilter;

	}

	/**
	 * Create Entity
	 *
	 * @param streetType Entity to save
	 * @return Saved instance
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@Transactional (readOnly = false)
	@Override
	public StreetType create(@NotNull StreetType streetType) throws FlexPayExceptionContainer {

		validate(streetType);
		streetType.setId(null);
		streetTypeDao.create(streetType);

		modificationListener.onCreate(streetType);

		return streetType;
	}

	/**
	 * Update or create Entity
	 *
	 * @param streetType Entity to save
	 * @return Saved instance
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	@Transactional (readOnly = false)
	@Override
	public StreetType update(@NotNull StreetType streetType) throws FlexPayExceptionContainer {

		validate(streetType);

		StreetType old = readFull(stub(streetType));
		if (old == null) {
			throw new FlexPayExceptionContainer(
					new FlexPayException("No object found to update " + streetType));
		}
		sessionUtils.evict(old);
		modificationListener.onUpdate(old, streetType);

		streetTypeDao.update(streetType);

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
	@Override
	public List<StreetType> getEntities() {
		return streetTypeDao.listStreetTypes(StreetType.STATUS_ACTIVE);
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
