package org.flexpay.ab.service.impl;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.dao.AddressAttributeTypeDao;
import org.flexpay.ab.persistence.AddressAttributeType;
import org.flexpay.ab.persistence.AddressAttributeTypeTranslation;
import org.flexpay.ab.service.AddressAttributeTypeService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.history.ModificationListener;
import org.flexpay.common.service.internal.SessionUtils;
import org.flexpay.common.util.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.jpa.JpaTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.treeSet;

public class AddressAttributeTypeServiceImpl implements AddressAttributeTypeService {

	private Logger log = LoggerFactory.getLogger(getClass());

	private AddressAttributeTypeDao addressAttributeTypeDao;

	private SessionUtils sessionUtils;
	private ModificationListener<AddressAttributeType> modificationListener;

	/**
	 * Read AddressAttributeType object by its unique id
	 *
	 * @param stub Address attribute type stub
	 * @return AddressAttributeType object, or <code>null</code> if object not found
	 */
	@Nullable
	@Override
	public AddressAttributeType readFull(@NotNull Stub<AddressAttributeType> stub) {
		return addressAttributeTypeDao.readFull(stub.getId());
	}

	/**
	 * Get all objects
	 *
	 * @return List of all objects
	 */
	@Override
	public List<AddressAttributeType> getAll() {
		return getAttributeTypes();
	}

	/**
	 * Get a list of available address attribute types
	 * sorted using AddressAttributeType natural comparator
	 *
	 * @return List of address attribute types
	 */
	@NotNull
	@Override
	public List<AddressAttributeType> getAttributeTypes() {
		List<AddressAttributeType> types = addressAttributeTypeDao.findAttributeTypes();
		return CollectionUtils.list(treeSet(types));
	}

	/**
	 * Disable address attribute types
	 *
	 * @param addressAttributeTypeIds IDs of address attribute types to disable
	 */
	@Override
	public void disable(@NotNull Collection<Long> addressAttributeTypeIds) {

		for (Long id : addressAttributeTypeIds) {
			if (id == null) {
				log.warn("Null id in collection of address attribute type ids for disable");
				continue;
			}
			AddressAttributeType type = addressAttributeTypeDao.read(id);
			if (type == null) {
				log.warn("Can't get address attribute type with id {} from DB", id);
				continue;
			}
			type.disable();
			addressAttributeTypeDao.update(type);

			modificationListener.onDelete(type);
			log.debug("Address attribute type disabled: {}", type);
		}

	}

	/**
	 * Create address attribute type
	 *
	 * @param type Address attribute type to save
	 * @return Saved instance of address attribute type
	 * @throws FlexPayExceptionContainer if validation fails
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
	 * Update or create address attribute type
	 *
	 * @param type Address attribute type to save
	 * @return Saved instance of address attribute type
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	@Transactional (readOnly = false)
	@NotNull
	@Override
	public AddressAttributeType update(@NotNull AddressAttributeType type) throws FlexPayExceptionContainer {

		validate(type);

		AddressAttributeType old = readFull(stub(type));
		if (old == null) {
			throw new FlexPayExceptionContainer(
					new FlexPayException("No address attribute type found to update " + type));
		}
		sessionUtils.evict(old);
		modificationListener.onUpdate(old, type);

		addressAttributeTypeDao.update(type);

		return type;
	}

	/**
	 * Validate address attribute type before save
	 *
	 * @param type AddressAttributeType object to validate
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	private void validate(@NotNull AddressAttributeType type) throws FlexPayExceptionContainer {

		FlexPayExceptionContainer container = new FlexPayExceptionContainer();

		boolean defaultLangNameFound = false;
		boolean defaultLangShortNameFound = false;

		for (AddressAttributeTypeTranslation translation : type.getTranslations()) {

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
				List<AddressAttributeType> types = addressAttributeTypeDao.findByNameAndLanguage(name, lang.getId());
				if (!types.isEmpty() && !types.get(0).getId().equals(type.getId())) {
					container.addException(new FlexPayException(
							"Name \"" + name + "\" is already use", "ab.error.name_is_already_use", name));
				}
			}

			if (shortNameNotEmpty) {
				List<AddressAttributeType> types = addressAttributeTypeDao.findByShortNameAndLanguage(shortName, lang.getId());
				if (!types.isEmpty() && !types.get(0).getId().equals(type.getId())) {
					container.addException(new FlexPayException(
							"Short name \"" + shortName + "\" is already use", "ab.error.short_name_is_already_use", shortName));
				}
			}

		}

		if (!defaultLangNameFound) {
			container.addException(new FlexPayException(
					"No default language translation", "ab.error.building_attribute_type.full_name_is_required"));
		}
		if (!defaultLangShortNameFound) {
			container.addException(new FlexPayException(
					"No default language translation", "ab.error.building_attribute_type.short_name_is_required"));
		}

		if (container.isNotEmpty()) {
			throw container;
		}

	}

    @Override
    public void setJpaTemplate(JpaTemplate jpaTemplate) {
        addressAttributeTypeDao.setJpaTemplate(jpaTemplate);
        sessionUtils.setJpaTemplate(jpaTemplate);
        modificationListener.setJpaTemplate(jpaTemplate);
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
