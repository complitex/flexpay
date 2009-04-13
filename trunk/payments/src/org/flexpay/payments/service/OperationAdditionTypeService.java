package org.flexpay.payments.service;

import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.payments.persistence.OperationAdditionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface OperationAdditionTypeService {

	/**
	 * Read OperationAdditionType object by Stub
	 *
	 * @param stub document addition type stub
	 * @return OperationAdditionType object
	 */
	@Nullable
	OperationAdditionType read(@NotNull Stub<OperationAdditionType> stub);

	/**
	 * Create type
	 *
	 * @param type OperationAdditionType Object
	 * @return persisted type back
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@NotNull
	OperationAdditionType create(@NotNull OperationAdditionType type) throws FlexPayExceptionContainer;

	/**
	 * Update type
	 *
	 * @param type OperationAdditionType Object
	 * @return updated type back
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@NotNull
	OperationAdditionType update(@NotNull OperationAdditionType type) throws FlexPayExceptionContainer;
}
