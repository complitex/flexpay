package org.flexpay.ab.service.impl;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.dao.IdentityTypeDao;
import org.flexpay.ab.persistence.IdentityType;
import org.flexpay.ab.persistence.IdentityTypeTranslation;
import org.flexpay.ab.service.IdentityTypeService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.history.ModificationListener;
import org.flexpay.common.service.internal.SessionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

import static org.flexpay.common.persistence.Stub.stub;

@Transactional (readOnly = true)
public class IdentityTypeServiceImpl implements IdentityTypeService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private IdentityTypeDao identityTypeDao;

	private SessionUtils sessionUtils;
	private ModificationListener<IdentityType> modificationListener;

	/**
	 * Read IdentityType object by its unique id
	 *
	 * @param stub Identity type stub
	 * @return IdentityType object, or <code>null</code> if object not found
	 */
	@Nullable
	@Override
	public IdentityType readFull(@NotNull Stub<IdentityType> stub) {
		return identityTypeDao.readFull(stub.getId());
	}

	/**
	 * Get all identityType objects
	 *
	 * @return List of all identityType objects
	 */
	@Override
	public List<IdentityType> getAll() {
		return getEntities();
	}

	/**
	 * Get a list of available identity types
	 *
	 * @return List of IdentityTypes
	 */
	@NotNull
	@Override
	public List<IdentityType> getEntities() {
		return identityTypeDao.listIdentityTypes(IdentityType.STATUS_ACTIVE);
	}

	/**
	 * Disable identity types
	 *
	 * @param identityTypeIds IDs of identity types to disable
	 */
	//TODO: check if there are any documents with specified type and reject operation
	@Transactional (readOnly = false)
	@Override
	public void disable(@NotNull Collection<Long> identityTypeIds) {
		for (Long id : identityTypeIds) {
			if (id == null) {
				log.warn("Null id in collection of identity type ids for disable");
				continue;
			}
			IdentityType identityType = identityTypeDao.read(id);
			if (identityType == null) {
				log.warn("Can't get identity type with id {} from DB", id);
				continue;
			}
			identityType.disable();
			identityTypeDao.update(identityType);

			modificationListener.onDelete(identityType);
			log.debug("Identity type disabled: {}", identityType);
		}
	}

	/**
	 * Create identity type
	 *
	 * @param identityType Identity type to save
	 * @return Saved instance of identity type
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Transactional (readOnly = false)
	@NotNull
	@Override
	public IdentityType create(@NotNull IdentityType identityType) throws FlexPayExceptionContainer {

		validate(identityType);
		identityType.setId(null);
		identityTypeDao.create(identityType);

		modificationListener.onCreate(identityType);

		return identityType;
	}

	/**
	 * Update or create identity type
	 *
	 * @param identityType Identity type to save
	 * @return Saved instance of identity type
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	@Transactional (readOnly = false)
	@NotNull
	@Override
	public IdentityType update(@NotNull IdentityType identityType) throws FlexPayExceptionContainer {

		validate(identityType);

		IdentityType old = readFull(stub(identityType));
		if (old == null) {
			throw new FlexPayExceptionContainer(
					new FlexPayException("No identity type found to update " + identityType));
		}
		sessionUtils.evict(old);
		modificationListener.onUpdate(old, identityType);

		identityTypeDao.update(identityType);

		return identityType;
	}

	/**
	 * Validate identity type before save
	 *
	 * @param type IdentityType object to validate
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	private void validate(@NotNull IdentityType type) throws FlexPayExceptionContainer {

		FlexPayExceptionContainer container = new FlexPayExceptionContainer();

		boolean defaultLangNameFound = false;

		for (IdentityTypeTranslation translation : type.getTranslations()) {

			Language lang = translation.getLang();
			String name = translation.getName();
			boolean nameNotEmpty = StringUtils.isNotEmpty(name);

			if (lang.isDefault()) {
				defaultLangNameFound = nameNotEmpty;
			}

			if (nameNotEmpty) {
				List<IdentityType> types = identityTypeDao.findByNameAndLanguage(name, lang.getId());
				if (!types.isEmpty() && !types.get(0).getId().equals(type.getId())) {
					container.addException(new FlexPayException(
							"Name \"" + name + "\" is already use", "ab.error.name_is_already_use", name));
				}
			}

		}

		if (!defaultLangNameFound) {
			container.addException(new FlexPayException(
					"No default language translation", "ab.error.identity_type.full_name_is_required"));
		}

		if (container.isNotEmpty()) {
			throw container;
		}

	}

	/**
	 * Find identity type by name
	 *
	 * @param typeName Identity type name
	 * @return IdentityType if found, or <code>null</code> otherwise
	 */
	@Nullable
	@Override
	public IdentityType findTypeByName(@Nullable String typeName) {

		int typeId = IdentityType.TYPE_PASSPORT;

		if (IdentityType.TYPE_NAME_PASSPORT.equals(typeName)) {
			typeId = IdentityType.TYPE_PASSPORT;
		} else if (IdentityType.TYPE_NAME_FOREIGN_PASSPORT.equals(typeName)) {
			typeId = IdentityType.TYPE_FOREIGN_PASSPORT;
		}

		return findTypeById(typeId);
	}

	/**
	 * Find identity type by enum id
	 *
	 * @param typeId Type id
	 * @return IdentityType if found, or <code>null</code> otherwise
	 */
	@Nullable
	@Override
	public IdentityType findTypeById(int typeId) {
		List<IdentityType> types = identityTypeDao.listIdentityTypesByEnumId(typeId);
		if (types == null) {
			log.error("Can't get identity types from DB");
			return null;
		}
		return types.isEmpty() ? null : types.get(0);
	}

	@Required
	public void setIdentityTypeDao(IdentityTypeDao identityTypeDao) {
		this.identityTypeDao = identityTypeDao;
	}

	@Required
	public void setSessionUtils(SessionUtils sessionUtils) {
		this.sessionUtils = sessionUtils;
	}

	@Required
	public void setModificationListener(ModificationListener<IdentityType> modificationListener) {
		this.modificationListener = modificationListener;
	}

}
