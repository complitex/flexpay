package org.flexpay.payments.service;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.payments.persistence.DocumentStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DocumentStatusService {

	/**
	 * Read DocumentStatus object by Stub
	 *
	 * @param stub document status stub
	 * @return DocumentStatus object if found, or <code>null</code> otherwise
	 */
	@Nullable
	DocumentStatus read(@NotNull Stub<DocumentStatus> stub);

	/**
	 * Read DocumentStatus object by Stub
	 *
	 * @param code document status code
	 * @return DocumentStatus object
	 * @throws FlexPayException if code is invalid
	 */
	@NotNull
	DocumentStatus read(int code) throws FlexPayException;
}
