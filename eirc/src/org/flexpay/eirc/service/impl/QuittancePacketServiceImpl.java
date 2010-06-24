package org.flexpay.eirc.service.impl;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.SecurityUtil;
import org.flexpay.eirc.dao.QuittancePacketDao;
import org.flexpay.eirc.dao.QuittancePacketDaoExt;
import org.flexpay.eirc.dao.QuittancePaymentDao;
import org.flexpay.eirc.persistence.QuittancePacket;
import org.flexpay.eirc.persistence.QuittancePayment;
import org.flexpay.eirc.service.QuittancePacketService;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.flexpay.common.persistence.Stub.stub;

@Transactional (readOnly = true)
public class QuittancePacketServiceImpl implements QuittancePacketService {

	private Logger log = LoggerFactory.getLogger(getClass());

	private QuittancePacketDao quittancePacketDao;
	private QuittancePacketDaoExt quittancePacketDaoExt;
	private QuittancePaymentDao quittancePaymentDao;

	/**
	 * Read full quittance packet details
	 *
	 * @param stub Quittance packet stub
	 * @return packet if found, or <code>null</code> otherwise
	 */
	@Override
	public QuittancePacket read(@NotNull Stub<QuittancePacket> stub) {
		return quittancePacketDao.read(stub.getId());
	}

	/**
	 * List quittance packets
	 *
	 * @param filters stack of filters
	 * @param pager   Pager
	 * @return packets
	 */
	@NotNull
	@Override
	public List<QuittancePacket> listPackets(ArrayStack filters, Page<QuittancePacket> pager) {
		return quittancePacketDaoExt.findPackets(filters, pager);
	}

	/**
	 * Suggest new quittance packet number
	 *
	 * @return new packet number
	 */
	@NotNull
	@Override
	public Long suggestPacketNumber() {
		return quittancePacketDaoExt.nextPacketNumber();
	}

	/**
	 * List registered quittance payments of a packet
	 *
	 * @param stub  Packet stub
	 * @param pager Page
	 * @return list of payments
	 */
	@NotNull
	@Override
	public List<QuittancePayment> listPayments(@NotNull Stub<QuittancePacket> stub, @NotNull Page<QuittancePayment> pager) {
		return quittancePaymentDao.findPacketPayments(stub.getId(), pager);
	}

	/**
	 * Create a new Quittance packet
	 *
	 * @param packet QuittancePacket to create
	 * @return persisted packet
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@Transactional (readOnly = false)
	@NotNull
	@Override
	public QuittancePacket create(@NotNull QuittancePacket packet) throws FlexPayExceptionContainer {
		validate(packet);

		packet.setCreatorUserName(SecurityUtil.getUserName());
		packet.setCloserUserName("");
		log.debug("Creating a new packet {}", packet);

		quittancePacketDao.create(packet);
		return packet;
	}

	/**
	 * Update a Quittance packet
	 *
	 * @param packet QuittancePacket to update
	 * @return packet back
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@Transactional (readOnly = false)
	@NotNull
	@Override
	public QuittancePacket update(@NotNull QuittancePacket packet) throws FlexPayExceptionContainer {
		validate(packet);

		log.debug("Updating a packet {}", packet);

		quittancePacketDao.update(packet);
		return packet;
	}

	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	private void validate(QuittancePacket packet) throws FlexPayExceptionContainer {
		FlexPayExceptionContainer ex = new FlexPayExceptionContainer();

		QuittancePacket persistent = packet.isNotNew() ? read(stub(packet)) : null;

		Date creationDate = packet.getCreationDate();
		if (creationDate == null) {
			ex.addException(new FlexPayException(
					"Invalid creation date", "eirc.error.quittance_packet.invalid_creation_date"));
		}
		if (creationDate != null && packet.isNew() && creationDate.before(DateUtil.now())) {
			ex.addException(new FlexPayException(
					"Invalid creation date", "eirc.error.quittance_packet.invalid_creation_date"));
		}
		if (creationDate != null && packet.isNew() && creationDate.before(DateUtil.now())) {
			ex.addException(new FlexPayException(
					"Invalid creation date", "eirc.error.quittance_packet.invalid_creation_date"));
		}
		if (creationDate != null && persistent != null && creationDate.before(persistent.getCreationDate())) {
			ex.addException(new FlexPayException(
					"Invalid creation date", "eirc.error.quittance_packet.invalid_creation_date"));
		}

		if (packet.getPacketNumber() == null) {
			ex.addException(new FlexPayException(
					"No packet number", "eirc.error.quittance_packet.no_packet_number"));
		}

		PaymentPoint point = packet.getPaymentPoint();
		if (point == null || point.isNew()) {
			ex.addException(new FlexPayException(
					"Invalid payment point", "eirc.error.quittance_packet.invalid_payment_point"));
		}

		if (packet.getControlQuittanciesNumber() == null) {
			ex.addException(new FlexPayException(
					"Invalid control number", "eirc.error.quittance_packet.invalid_control_number"));
		}

		if (packet.getControlOverallSumm() == null) {
			ex.addException(new FlexPayException(
					"Invalid control sum", "eirc.error.quittance_packet.invalid_control_sum"));
		}

		if (ex.isNotEmpty()) {
			throw ex;
		}
	}

	@Required
	public void setQuittancePacketDao(QuittancePacketDao quittancePacketDao) {
		this.quittancePacketDao = quittancePacketDao;
	}

	@Required
	public void setQuittancePacketDaoExt(QuittancePacketDaoExt quittancePacketDaoExt) {
		this.quittancePacketDaoExt = quittancePacketDaoExt;
	}

	@Required
	public void setQuittancePaymentDao(QuittancePaymentDao quittancePaymentDao) {
		this.quittancePaymentDao = quittancePaymentDao;
	}
}
