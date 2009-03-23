package org.flexpay.eirc.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.eirc.persistence.QuittancePacket;
import org.flexpay.eirc.persistence.QuittancePayment;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface QuittancePaymentDao extends GenericDao<QuittancePayment, Long> {

	@NotNull
	List<QuittancePayment> findPacketPayments(Long packetId, Page<QuittancePayment> pager);

	@NotNull
	List<QuittancePacket> findQuittancePayedPackets(Long quittanceId);

	@NotNull
	List<QuittancePayment> findQuittancePayments(Long quittanceId);
}
