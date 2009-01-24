package org.flexpay.bti.service.impl;

import org.flexpay.bti.service.BuildingAttributeTypeService;
import org.flexpay.bti.persistence.BuildingAttributeType;
import org.flexpay.bti.dao.BuildingAttributeTypeDao;
import org.flexpay.bti.dao.BuildingAttributeTypeDaoExt;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

@Transactional (readOnly = true)
public class BuildingAttributeTypeServiceImpl implements BuildingAttributeTypeService {

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
	public BuildingAttributeType create(@NotNull BuildingAttributeType type) throws FlexPayExceptionContainer {
		validate(type);

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
	public BuildingAttributeType update(@NotNull BuildingAttributeType type) throws FlexPayExceptionContainer {
		validate(type);

		attributeTypeDao.update(type);
		return type;
	}

	private void validate(@NotNull BuildingAttributeType type) throws FlexPayExceptionContainer {
		FlexPayExceptionContainer ex = new FlexPayExceptionContainer();

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

	@Required
	public void setAttributeTypeDao(BuildingAttributeTypeDao attributeTypeDao) {
		this.attributeTypeDao = attributeTypeDao;
	}

	@Required
	public void setAttributeTypeDaoExt(BuildingAttributeTypeDaoExt attributeTypeDaoExt) {
		this.attributeTypeDaoExt = attributeTypeDaoExt;
	}
}
