package org.flexpay.payments.service;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.payments.persistence.OperationStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface OperationStatusService {

	/**
	 * Read full operation status details
	 *
	 * @param stub Operation status stub
	 * @return OperationStatus if found, or <code>null</code> otherwise
	 */
	@Nullable
	OperationStatus read(@NotNull Stub<OperationStatus> stub);

	/**
	 * Read full operation status details by code
	 *
	 * @param code Operation status code
	 * @return OperationStatus if found, or <code>null</code> otherwise
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if lookup by code fails
	 */
	@NotNull
	OperationStatus read(int code) throws FlexPayException;	
}
