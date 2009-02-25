package org.flexpay.eirc.service.imp;

import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.dao.QuittancePaymentDao;
import org.flexpay.eirc.persistence.QuittancePacket;
import org.flexpay.eirc.persistence.account.Quittance;
import org.flexpay.eirc.service.QuittancePaymentService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional (readOnly = true)
public class QuittancePaymentServiceImpl implements QuittancePaymentService {

	private QuittancePaymentDao quittancePaymentDao;

	/**
	 * Get list of packets where quittance was payed
	 *
	 * @param stub Quittance stub
	 * @return List of quittance packets possibly empty
	 */
	@NotNull
	public List<QuittancePacket> getPacketsWhereQuittancePayed(@NotNull Stub<Quittance> stub) {
		return quittancePaymentDao.findQuittancePayedPackets(stub.getId());
	}

	@Required
	public void setQuittancePaymentDao(QuittancePaymentDao quittancePaymentDao) {
		this.quittancePaymentDao = quittancePaymentDao;
	}
}
