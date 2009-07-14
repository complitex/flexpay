package org.flexpay.payments.service;

import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.payments.persistence.DocumentAdditionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DocumentAdditionTypeService {

	/**
	 * Read DocumentAdditionType object by Stub
	 *
	 * @param stub document addition type stub
	 * @return DocumentAdditionType object
	 */
	@Nullable
	DocumentAdditionType read(@NotNull Stub<DocumentAdditionType> stub);

	/**
	 * Create type
	 *
	 * @param type DocumentAdditionType Object
	 * @return persisted type back
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@NotNull
	DocumentAdditionType create(@NotNull DocumentAdditionType type) throws FlexPayExceptionContainer;

	/**
	 * Update type
	 *
	 * @param type DocumentAdditionType Object
	 * @return updated type back
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@NotNull
	DocumentAdditionType update(@NotNull DocumentAdditionType type) throws FlexPayExceptionContainer;

	/**
	 * Returms document addition type code instance by code
	 * @param typeCode code
	 * @throws FlexPayException if no type found for code
	 * @return document addition type code instance
	 */
	DocumentAdditionType findTypeByCode(int typeCode) throws FlexPayException;
}
