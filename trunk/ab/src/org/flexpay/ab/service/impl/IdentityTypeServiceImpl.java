package org.flexpay.ab.service.impl;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.dao.IdentityTypeDao;
import org.flexpay.ab.persistence.IdentityType;
import org.flexpay.ab.persistence.IdentityTypeTranslation;
import org.flexpay.ab.service.IdentityTypeService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
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

@Transactional (readOnly = true)
public class IdentityTypeServiceImpl implements IdentityTypeService {

	private Logger log = LoggerFactory.getLogger(getClass());

	private IdentityTypeDao identityTypeDao;

	private SessionUtils sessionUtils;
	private ModificationListener<IdentityType> modificationListener;

	/**
	 * Read IdentityType object by its unique id
	 *
	 * @param stub Entity stub
	 * @return IdentityType object, or <code>null</code> if object not found
	 */
	@Nullable
	@Override
	public IdentityType read(@NotNull Stub<IdentityType> stub) {

		return identityTypeDao.readFull(stub.getId());
	}

	/**
	 * Disable IdentityTypes TODO: check if there are any documents with specified type and reject operation
	 *
	 * @param identityTypes IdentityTypes to disable
	 */
	@Transactional (readOnly = false)
	@Override
	public void disable(Collection<IdentityType> identityTypes) {
		log.info("{} types to disable", identityTypes.size());
		for (IdentityType type : identityTypes) {
			type.disable();
			identityTypeDao.update(type);

			modificationListener.onDelete(type);

			log.info("Disabled: {}", type);
		}
	}

	@Transactional (readOnly = false)
	@Override
	public void disableByIds(@NotNull Collection<Long> objectIds) {
		for (Long id : objectIds) {
			IdentityType identityType = identityTypeDao.readFull(id);
			if (identityType != null) {
				identityType.disable();
				identityTypeDao.update(identityType);

				modificationListener.onDelete(identityType);
				log.debug("Disabled: {}", identityType);
			}
		}
	}

	/**
	 * Create Entity
	 *
	 * @param identityType Entity to save
	 * @return Saved instance
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@Transactional (readOnly = false)
	@Override
	public IdentityType create(@NotNull IdentityType identityType) throws FlexPayExceptionContainer {

		validate(identityType);
		identityType.setId(null);
		identityTypeDao.create(identityType);

		modificationListener.onCreate(identityType);

		return identityType;
	}

	/**
	 * Update or create Entity
	 *
	 * @param identityType Entity to save
	 * @return Saved instance
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	@Transactional (readOnly = false)
	@Override
	public IdentityType update(@NotNull IdentityType identityType) throws FlexPayExceptionContainer {

		validate(identityType);

		IdentityType old = read(stub(identityType));
		if (old == null) {
			throw new FlexPayExceptionContainer(
					new FlexPayException("No object found to update " + identityType));
		}
		sessionUtils.evict(old);
		modificationListener.onUpdate(old, identityType);

		identityTypeDao.update(identityType);

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
	@Nullable
	@Override
	public IdentityType getType(int typeId) {

		for (IdentityType type : getEntities()) {
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
	@Override
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
	 * Get a list of available identity types
	 *
	 * @return List of IdentityType
	 */
	@NotNull
	@Override
	public List<IdentityType> getEntities() {
		return identityTypeDao.listIdentityTypes(IdentityType.STATUS_ACTIVE);
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
