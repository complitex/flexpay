package org.flexpay.payments.service;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.payments.persistence.OperationType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface OperationTypeService {

	/**
	 * Read full operation type details
	 *
	 * @param stub Operation type stub
	 * @return OperationType if found, or <code>null</code> otherwise
	 */
	@Nullable
	OperationType read(@NotNull Stub<OperationType> stub);

	/**
	 * Read full operation type details by code
	 *
	 * @param code Operation type code
	 * @return OperationType if found, or <code>null</code> otherwise
	 * @throws FlexPayException if lookup by code fails
	 */
	@NotNull
	OperationType read(int code) throws FlexPayException;
}
