package org.flexpay.ab.service.impl;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.dao.AddressAttributeTypeDao;
import org.flexpay.ab.persistence.AddressAttributeType;
import org.flexpay.ab.persistence.AddressAttributeTypeTranslation;
import org.flexpay.ab.service.AddressAttributeTypeService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.history.ModificationListener;
import org.flexpay.common.service.internal.SessionUtils;
import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.common.util.CollectionUtils.treeSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

public class AddressAttributeTypeServiceImpl implements AddressAttributeTypeService {

	private Logger log = LoggerFactory.getLogger(getClass());

	private AddressAttributeTypeDao addressAttributeTypeDao;

	private SessionUtils sessionUtils;
	private ModificationListener<AddressAttributeType> modificationListener;

	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	private void validate(@NotNull AddressAttributeType type) throws FlexPayExceptionContainer {
		FlexPayExceptionContainer container = new FlexPayExceptionContainer();

		boolean defaultLangTranslationFound = false;
		boolean defaultLangShortNameTranslationFound = false;
		for (AddressAttributeTypeTranslation translation : type.getTranslations()) {
			if (translation.getLang().isDefault()) {
				if (StringUtils.isNotEmpty(translation.getName())) {
					defaultLangTranslationFound = true;
				}

				if (StringUtils.isNotEmpty(translation.getShortName())) {
					defaultLangShortNameTranslationFound = true;
				}
			}
		}

		if (!defaultLangTranslationFound) {
			container.addException(new FlexPayException(
					"No default translation", "error.no_default_translation"));
		}

		if (!defaultLangShortNameTranslationFound) {
			container.addException(new FlexPayException(
					"No default short name translation", "error.no_default_short_translation"
			));
		}
		
		// todo check if there is already a type with a specified name

		if (container.isNotEmpty()) {
			throw container;
		}

	}

	/**
	 * Create building address attribute type
	 *
	 * @param type AttributeType to save
	 * @return persisted object back
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@NotNull
	@Override
	public AddressAttributeType create(@NotNull AddressAttributeType type) throws FlexPayExceptionContainer {

		validate(type);

		type.setId(null);
		addressAttributeTypeDao.create(type);

		modificationListener.onCreate(type);

		return type;
	}

	/**
	 * Create or update building attribute type
	 *
	 * @param type AttributeType to save
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	@Transactional (readOnly = false)
	@NotNull
	@Override
	public AddressAttributeType update(@NotNull AddressAttributeType type) throws FlexPayExceptionContainer {

		validate(type);

		AddressAttributeType old = read(stub(type));
		if (old == null) {
			throw new FlexPayExceptionContainer(
					new FlexPayException("No object found to update " + type));
		}
		sessionUtils.evict(old);
		modificationListener.onUpdate(old, type);

		addressAttributeTypeDao.update(type);

		return type;
	}

	/**
	 * Disable Entities
	 *
	 * @param entities Entities to disable
	 */
	@Override
	public void disable(Collection<AddressAttributeType> entities) {

		if (log.isDebugEnabled()) {
			log.debug("{} types to disable", entities.size());
		}
		for (AddressAttributeType type : entities) {

			type.setStatus(AddressAttributeType.STATUS_DISABLED);
			addressAttributeTypeDao.update(type);

			modificationListener.onDelete(type);

			log.info("Disabled: {}", type);
		}
	}

	/**
	 * Get building attribute type
	 *
	 * @param stub BuildingAttributeType stub
	 * @return Attribute type if found, or <code>null</code> otherwise
	 */
	@Nullable
	@Override
	public AddressAttributeType read(@NotNull Stub<AddressAttributeType> stub) {

		return addressAttributeTypeDao.readFull(stub.getId());
	}

	/**
	 * Get building attribute types
	 *
	 * @return BuildingAttributeType list
	 */
	@Override
	public List<AddressAttributeType> getAttributeTypes() {

		List<AddressAttributeType> types = addressAttributeTypeDao.findAttributeTypes();
		return list(treeSet(types));
	}

	@Required
	public void setAddressAttributeTypeDao(AddressAttributeTypeDao addressAttributeTypeDao) {
		this.addressAttributeTypeDao = addressAttributeTypeDao;
	}

	@Required
	public void setSessionUtils(SessionUtils sessionUtils) {
		this.sessionUtils = sessionUtils;
	}

	@Required
	public void setModificationListener(ModificationListener<AddressAttributeType> modificationListener) {
		this.modificationListener = modificationListener;
	}

}
