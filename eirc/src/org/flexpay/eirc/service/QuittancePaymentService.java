package org.flexpay.eirc.service;

import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.persistence.QuittancePacket;
import org.flexpay.eirc.persistence.QuittancePayment;
import org.flexpay.eirc.persistence.account.Quittance;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.annotation.Secured;

import java.util.List;

public interface QuittancePaymentService {

	/**
	 * Get list of packets where quittance was payed
	 *
	 * @param stub Quittance stub
	 * @return List of quittance packets possibly empty
	 */
	@Secured(Roles.QUITTANCE_PAYMENT_READ)
	@NotNull
	List<QuittancePacket> getPacketsWhereQuittancePayed(@NotNull Stub<Quittance> stub);

	/**
	 * Find all quittance payments
	 *
	 * @param stub Quittance stub to get quittance of
	 * @return List of registered quittance payments
	 */
	@Secured(Roles.QUITTANCE_PAYMENT_READ)
	@NotNull
	List<QuittancePayment> getQuittancePayments(@NotNull Stub<Quittance> stub);

	/**
	 * Create quittance payment
	 * 
	 * @param payment QuittancePayement to persist
	 */
	@Secured(Roles.QUITTANCE_PAY)
	void createPayment(@NotNull QuittancePayment payment);
}
