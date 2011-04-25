package org.flexpay.bti.service.impl;

import org.apache.commons.lang.StringUtils;
import org.flexpay.bti.dao.ApartmentAttributeTypeDao;
import org.flexpay.bti.dao.ApartmentAttributeTypeDaoExt;
import org.flexpay.bti.persistence.apartment.*;
import org.flexpay.bti.service.ApartmentAttributeTypeService;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Transactional (readOnly = true)
public class ApartmentAttributeTypeServiceImpl implements ApartmentAttributeTypeService {

	private Logger log = LoggerFactory.getLogger(getClass());

	private ApartmentAttributeTypeDao attributeTypeDao;
	private ApartmentAttributeTypeDaoExt attributeTypeDaoExt;

	/**
	 * Read full type info
	 *
	 * @param stub Attribute type stub
	 * @return type if found, or <code>null</code> otherwise
	 */
	public ApartmentAttributeType readFull(@NotNull Stub<ApartmentAttributeType> stub) {
		return attributeTypeDaoExt.readFull(stub.getId());
	}

	/**
	 * Create a new attribute type
	 *
	 * @param type Attribute type to create
	 * @return persisted type back
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer if validation fails
	 */
	@Transactional (readOnly = false)
	public ApartmentAttributeType create(@NotNull ApartmentAttributeType type) throws FlexPayExceptionContainer {
		validate(type);

		type.setId(null);
		attributeTypeDao.create(type);
		return type;
	}

	/**
	 * Update existing attribute type
	 *
	 * @param type Attribute type to update
	 * @return type back
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer if validation fails
	 */
	@Transactional (readOnly = false)
	public ApartmentAttributeType update(@NotNull ApartmentAttributeType type) throws FlexPayExceptionContainer {
		validate(type);

		attributeTypeDao.update(type);
		return type;
	}

	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	private void validate(@NotNull ApartmentAttributeType type) throws FlexPayExceptionContainer {
		FlexPayExceptionContainer ex = new FlexPayExceptionContainer();

		boolean defaultNameFound = false;
		for (ApartmentAttributeTypeName name : type.getTranslations()) {
			// check if default language name specified
			if (name.getLang().isDefault() && StringUtils.isNotBlank(name.getName())) {
				defaultNameFound = true;
			}

			// check if the name is unique
			boolean isUnique = attributeTypeDaoExt.isUniqueTypeName(name.getName(), type.getId());
			if (!isUnique) {
				ex.addException(new FlexPayException(
						"Not unique name", "bti.error.apartment.attribute.type.not_unique", name.getName()));
			}
		}
		if (!defaultNameFound) {
			ex.addException(new FlexPayException("No default translation", "common.error.no_default_translation"));
		}

		if (type.getUniqueCode() != null) {
			// check if the name is unique
			boolean isUnique = attributeTypeDaoExt.isUniqueTypeName(type.getUniqueCode(), type.getId());
			if (!isUnique) {
				ex.addException(new FlexPayException(
						"Not unique name", "bti.error.apartment.attribute.type.not_unique_code", type.getUniqueCode()));
			}

		}

		// check enum type
		if (type instanceof ApartmentAttributeTypeEnum) {
			// check if there is any values
			ApartmentAttributeTypeEnum enumType = (ApartmentAttributeTypeEnum) type;
			if (enumType.getValues().isEmpty()) {
				ex.addException(new FlexPayException(
						"No enum values", "bti.error.apartment.attribute.type.enum.no_values"));
			}

			// check value duplicates
			Set<String> values = CollectionUtils.set();
			for (ApartmentAttributeTypeEnumValue value : enumType.getValues()) {
				if (values.contains(value.getValue())) {
					ex.addException(new FlexPayException("Duplicate enum value",
							"bti.error.apartment.attribute.type.enum.duplicate", value.getValue()));
				} else {
					values.add(value.getValue());
				}
			}
		}

		ApartmentAttributeGroup group = type.getGroup();
		if (group == null || group.isNew()) {
			ex.addException(new FlexPayException("no group", "bti.error.apartment.attribute.type.no_group"));
		}

		if (ex.isNotEmpty()) {
			throw ex;
		}
	}

	/**
	 * Find apartment attribute types
	 *
	 * @param pager Page
	 * @return list of apartment attributes
	 */
	public List<ApartmentAttributeType> listTypes(@NotNull Page<ApartmentAttributeType> pager) {
		return attributeTypeDao.findTypes(pager);
	}

    public List<ApartmentAttributeType> findTypes() {
        return attributeTypeDao.findAllTypes();
    }

	/**
	 * Find attribute type by name
	 *
	 * @param typeName Type name to look up
	 * @return type if found, or <code>null</code> otherwise
	 */
	public ApartmentAttributeType findTypeByName(String typeName) {
		List<ApartmentAttributeType> types = attributeTypeDao.findTypesByName(typeName, typeName);
		if (types.isEmpty()) {
			return null;
		}
		if (types.size() > 1) {
			log.error("Internal error, several attribute types found for name '{}'", typeName);
			throw new IllegalStateException("Internal error, several attribute types found for name " + typeName);
		}

		return types.get(0);
	}

	@Required
	public void setAttributeTypeDao(ApartmentAttributeTypeDao attributeTypeDao) {
		this.attributeTypeDao = attributeTypeDao;
	}

	@Required
	public void setAttributeTypeDaoExt(ApartmentAttributeTypeDaoExt attributeTypeDaoExt) {
		this.attributeTypeDaoExt = attributeTypeDaoExt;
	}

}
