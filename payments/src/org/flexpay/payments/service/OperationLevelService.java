package org.flexpay.payments.service;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.payments.persistence.OperationLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface OperationLevelService {

	/**
	 * Read full operation type details
	 *
	 * @param stub Operation level stub
	 * @return OperationLevel if found, or <code>null</code> otherwise
	 */
	@Nullable
	OperationLevel read(@NotNull Stub<OperationLevel> stub);

	/**
	 * Read full operation level details by code
	 *
	 * @param code Operation level code
	 * @return OperationLevel if found, or <code>null</code> otherwise
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if lookup by code fails
	 */
	@NotNull
	OperationLevel read(int code) throws FlexPayException;
}
