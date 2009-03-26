package org.flexpay.eirc.service.imp;

import org.flexpay.eirc.dao.QuittancePaymentStatusDao;
import org.flexpay.eirc.persistence.QuittancePaymentStatus;
import org.flexpay.eirc.service.QuittancePaymentStatusService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional (readOnly = true)
public class QuittancePaymentStatusServiceImpl implements QuittancePaymentStatusService {

	private QuittancePaymentStatusDao statusDao;

	/**
	 * Find full payed quittance payment status
	 *
	 * @return QuittancePaymentStatus
	 */
	@NotNull
	public QuittancePaymentStatus getPayedFullStatus() {

		List<QuittancePaymentStatus> statuses = statusDao.findStatus(QuittancePaymentStatus.PAYED_FULL);
		if (statuses.isEmpty()) {
			throw new RuntimeException("PAYED_FULL status not found in DB");
		}
		if (statuses.size() > 1) {
			throw new RuntimeException("Found several PAYED_FULL statuses in DB");
		}

		return statuses.get(0);
	}

	/**
	 * Find partially payed quittance payment status
	 *
	 * @return QuittancePaymentStatus
	 */
	@NotNull
	public QuittancePaymentStatus getPayedPartiallyStatus() {

		List<QuittancePaymentStatus> statuses = statusDao.findStatus(QuittancePaymentStatus.PAYED_PARTIALLY);
		if (statuses.isEmpty()) {
			throw new RuntimeException("PAYED_PARTIALLY status not found in DB");
		}
		if (statuses.size() > 1) {
			throw new RuntimeException("Found several PAYED_PARTIALLY statuses in DB");
		}

		return statuses.get(0);
	}

	@Required
	public void setStatusDao(QuittancePaymentStatusDao statusDao) {
		this.statusDao = statusDao;
	}
}
