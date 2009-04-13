package org.flexpay.payments.service.impl;

import org.flexpay.payments.service.DocumentAdditionTypeService;
import org.flexpay.payments.persistence.DocumentAdditionType;
import org.flexpay.payments.dao.DocumentAdditionTypeDao;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

@Transactional (readOnly = true)
public class DocumentAdditionTypeServiceImpl implements DocumentAdditionTypeService {

	private DocumentAdditionTypeDao typeDao;

	/**
	 * Read DocumentAdditionType object by Stub
	 *
	 * @param stub document addition type stub
	 * @return DocumentAdditionType object
	 */
	public DocumentAdditionType read(@NotNull Stub<DocumentAdditionType> stub) {
		return typeDao.readFull(stub.getId());
	}

	/**
	 * Create type
	 *
	 * @param type DocumentAdditionType Object
	 * @return persisted type back
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@Transactional (readOnly = false)
	@NotNull
	public DocumentAdditionType create(@NotNull DocumentAdditionType type) throws FlexPayExceptionContainer {

		validate(type);
		type.setId(null);
		typeDao.create(type);
		return type;
	}

	/**
	 * Update type
	 *
	 * @param type DocumentAdditionType Object
	 * @return updated type back
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@Transactional (readOnly = false)
	@NotNull
	public DocumentAdditionType update(@NotNull DocumentAdditionType type) throws FlexPayExceptionContainer {

		validate(type);
		typeDao.update(type);
		return type;
	}

	private void validate(DocumentAdditionType type) throws FlexPayExceptionContainer {
		FlexPayExceptionContainer ex = new FlexPayExceptionContainer();

		if (ex.isNotEmpty()) {
			throw ex;
		}
	}

	@Required
	public void setTypeDao(DocumentAdditionTypeDao typeDao) {
		this.typeDao = typeDao;
	}
}
