package org.flexpay.payments.service.impl;

import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.payments.dao.OperationAdditionTypeDao;
import org.flexpay.payments.persistence.OperationAdditionType;
import org.flexpay.payments.service.OperationAdditionTypeService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

@Transactional (readOnly = true)
public class OperationAdditionTypeServiceImpl implements OperationAdditionTypeService {

	private OperationAdditionTypeDao typeDao;

	/**
	 * Read OperationAdditionType object by Stub
	 *
	 * @param stub operation addition type stub
	 * @return OperationAdditionType object
	 */
	public OperationAdditionType read(@NotNull Stub<OperationAdditionType> stub) {
		return typeDao.readFull(stub.getId());
	}

	/**
	 * Create type
	 *
	 * @param type OperationAdditionType Object
	 * @return persisted type back
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@Transactional (readOnly = false)
	@NotNull
	public OperationAdditionType create(@NotNull OperationAdditionType type) throws FlexPayExceptionContainer {

		validate(type);
		type.setId(null);
		typeDao.create(type);
		return type;
	}

	/**
	 * Update type
	 *
	 * @param type OperationAdditionType Object
	 * @return updated type back
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@Transactional (readOnly = false)
	@NotNull
	public OperationAdditionType update(@NotNull OperationAdditionType type) throws FlexPayExceptionContainer {

		validate(type);
		typeDao.update(type);
		return type;
	}

	private void validate(OperationAdditionType type) throws FlexPayExceptionContainer {
		FlexPayExceptionContainer ex = new FlexPayExceptionContainer();

		if (ex.isNotEmpty()) {
			throw ex;
		}
	}

	@Required
	public void setTypeDao(OperationAdditionTypeDao typeDao) {
		this.typeDao = typeDao;
	}
}
