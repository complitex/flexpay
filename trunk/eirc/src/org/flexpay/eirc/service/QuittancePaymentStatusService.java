package org.flexpay.eirc.service;

import org.flexpay.eirc.persistence.QuittancePaymentStatus;
import org.jetbrains.annotations.NotNull;

public interface QuittancePaymentStatusService {

	/**
	 * Find full payed quittance payment status
	 *
	 * @return QuittancePaymentStatus
	 */
	@NotNull
	QuittancePaymentStatus getPayedFullStatus();

	/**
	 * Find partially payed quittance payment status
	 *
	 * @return QuittancePaymentStatus
	 */
	@NotNull
	QuittancePaymentStatus getPayedPartiallyStatus();
}
