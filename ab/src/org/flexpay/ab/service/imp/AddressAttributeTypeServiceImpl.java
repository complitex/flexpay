package org.flexpay.ab.service.imp;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.dao.AddressAttributeTypeDao;
import org.flexpay.ab.persistence.AddressAttributeType;
import org.flexpay.ab.persistence.AddressAttributeTypeTranslation;
import org.flexpay.ab.service.AddressAttributeTypeService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class AddressAttributeTypeServiceImpl implements AddressAttributeTypeService {

	private AddressAttributeTypeDao addressAttributeTypeDao;

	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	private void validate(@NotNull AddressAttributeType type) throws FlexPayExceptionContainer {
		FlexPayExceptionContainer container = new FlexPayExceptionContainer();

		boolean defaultLangTranslationFound = false;
		for (AddressAttributeTypeTranslation translation : type.getTranslations()) {
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
	 * Create or update building attribute type
	 *
	 * @param type AttributeType to save
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@Transactional (readOnly = false)
	public void save(@NotNull AddressAttributeType type) throws FlexPayExceptionContainer {
		validate(type);
		if (type.isNew()) {
			type.setId(null);
			addressAttributeTypeDao.create(type);
		} else {
			addressAttributeTypeDao.update(type);
		}
	}

	/**
	 * Get building attribute type
	 *
	 * @param stub BuildingAttributeType stub
	 * @return Attribute type if found, or <code>null</code> otherwise
	 */
	@Nullable
	public AddressAttributeType read(@NotNull Stub<AddressAttributeType> stub) {
		return addressAttributeTypeDao.readFull(stub.getId());
	}

	/**
	 * Get building attribute types
	 *
	 * @return BuildingAttributeType list
	 */
	public List<AddressAttributeType> getAttributeTypes() {

		return addressAttributeTypeDao.findAttributeTypes();
	}

	@Required
	public void setBuildingAttributeTypeDao(AddressAttributeTypeDao addressAttributeTypeDao) {
		this.addressAttributeTypeDao = addressAttributeTypeDao;
	}
}
