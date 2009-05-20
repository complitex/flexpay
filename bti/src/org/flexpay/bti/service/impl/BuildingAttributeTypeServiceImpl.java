package org.flexpay.bti.service.impl;

import org.apache.commons.lang.StringUtils;
import org.flexpay.bti.dao.BuildingAttributeTypeDao;
import org.flexpay.bti.dao.BuildingAttributeTypeDaoExt;
import org.flexpay.bti.persistence.building.*;
import org.flexpay.bti.service.BuildingAttributeTypeService;
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
public class BuildingAttributeTypeServiceImpl implements BuildingAttributeTypeService {

	private Logger log = LoggerFactory.getLogger(getClass());

	private BuildingAttributeTypeDao attributeTypeDao;
	private BuildingAttributeTypeDaoExt attributeTypeDaoExt;

	/**
	 * Read full type info
	 *
	 * @param stub Attribute type stub
	 * @return type if found, or <code>null</code> otherwise
	 */
	public BuildingAttributeType readFull(@NotNull Stub<BuildingAttributeType> stub) {
		return attributeTypeDaoExt.readFull(stub.getId());
	}

	/**
	 * Create a new attribute type
	 *
	 * @param type Attribute type to create
	 * @return persisted type back
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Transactional (readOnly = false)
	public BuildingAttributeType create(@NotNull BuildingAttributeType type) throws FlexPayExceptionContainer {
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
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Transactional (readOnly = false)
	public BuildingAttributeType update(@NotNull BuildingAttributeType type) throws FlexPayExceptionContainer {
		validate(type);

		attributeTypeDao.update(type);
		return type;
	}

	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	private void validate(@NotNull BuildingAttributeType type) throws FlexPayExceptionContainer {
		FlexPayExceptionContainer ex = new FlexPayExceptionContainer();

		boolean defaultNameFound = false;
		for (BuildingAttributeTypeName name : type.getTranslations()) {
			// check if default language name specified
			if (name.getLang().isDefault() && StringUtils.isNotBlank(name.getName())) {
				defaultNameFound = true;
			}

			// check if the name is unique
			boolean isUnique = attributeTypeDaoExt.isUniqueTypeName(name.getName(), type.getId());
			if (!isUnique) {
				ex.addException(new FlexPayException(
						"Not unique name", "bti.error.building.attribute.type.not_unique", name.getName()));
			}
		}
		if (!defaultNameFound) {
			ex.addException(new FlexPayException("No default translation", "error.no_default_translation"));
		}

		if (type.getUniqueCode() != null) {
			// check if the name is unique
			boolean isUnique = attributeTypeDaoExt.isUniqueTypeName(type.getUniqueCode(), type.getId());
			if (!isUnique) {
				ex.addException(new FlexPayException(
						"Not unique name", "bti.error.building.attribute.type.not_unique_code", type.getUniqueCode()));
			}

		}

		// check enum type
		if (type instanceof BuildingAttributeTypeEnum) {
			// check if there is any values
			BuildingAttributeTypeEnum enumType = (BuildingAttributeTypeEnum) type;
			if (enumType.getValues().isEmpty()) {
				ex.addException(new FlexPayException(
						"No enum values", "bti.error.building.attribute.type.enum.no_values"));
			}

			// check value duplicates
			Set<String> values = CollectionUtils.set();
			for (BuildingAttributeTypeEnumValue value : enumType.getValues()) {
				if (values.contains(value.getValue())) {
					ex.addException(new FlexPayException("Duplicate enum value",
							"bti.error.building.attribute.type.enum.duplicate", value.getValue()));
				} else {
					values.add(value.getValue());
				}
			}
		}

		BuildingAttributeGroup group = type.getGroup();
		if (group == null || group.isNew()) {
			ex.addException(new FlexPayException("no group", "bti.error.building.attribute.type.no_group"));
		}

		if (ex.isNotEmpty()) {
			throw ex;
		}
	}

	/**
	 * Find building attribute types
	 *
	 * @param pager Page
	 * @return list of building attributes
	 */
	public List<BuildingAttributeType> listTypes(@NotNull Page<BuildingAttributeType> pager) {
		return attributeTypeDao.findTypes(pager);
	}

    public List<BuildingAttributeType> listTypes() {
        return attributeTypeDao.findAllTypes();
    }

	/**
	 * Find attribute type by name
	 *
	 * @param typeName Type name to look up
	 * @return type if found, or <code>null</code> otherwise
	 */
	public BuildingAttributeType findTypeByName(String typeName) {
		List<BuildingAttributeType> types = attributeTypeDao.findTypesByName(typeName, typeName);
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
	public void setAttributeTypeDao(BuildingAttributeTypeDao attributeTypeDao) {
		this.attributeTypeDao = attributeTypeDao;
	}

	@Required
	public void setAttributeTypeDaoExt(BuildingAttributeTypeDaoExt attributeTypeDaoExt) {
		this.attributeTypeDaoExt = attributeTypeDaoExt;
	}

}
