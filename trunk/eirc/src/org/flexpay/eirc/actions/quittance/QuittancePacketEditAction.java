package org.flexpay.eirc.actions.quittance;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.persistence.filter.CloseDateFilter;
import org.flexpay.common.persistence.filter.CreateDateFilter;
import org.flexpay.eirc.actions.organization.PaymentPointHelper;
import org.flexpay.eirc.persistence.PaymentPoint;
import org.flexpay.eirc.persistence.QuittancePacket;
import org.flexpay.eirc.persistence.filters.PaymentPointsFilter;
import org.flexpay.eirc.service.PaymentPointService;
import org.flexpay.eirc.service.QuittancePacketService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class QuittancePacketEditAction extends FPActionSupport {

	private QuittancePacketService quittancePacketService;
	private PaymentPointService paymentPointService;
	private PaymentPointHelper paymentPointHelper;

	private CreateDateFilter createDateFilter = new CreateDateFilter();
	private BeginDateFilter beginDateFilter = new BeginDateFilter();
	private CloseDateFilter closeDateFilter = new CloseDateFilter();
	private PaymentPointsFilter paymentPointsFilter = new PaymentPointsFilter();
	private QuittancePacket packet = new QuittancePacket();

	/**
	 * Perform action execution.
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return execution result code
	 * @throws Exception if failure occurs
	 */
	@NotNull
	protected String doExecute() throws Exception {

		if (packet.getId() == null) {
			addActionError(getText("error.no_id"));
			return REDIRECT_SUCCESS;
		}

		QuittancePacket pckt = packet.isNew() ? packet : quittancePacketService.read(stub(packet));
		if (pckt == null) {
			addActionError(getText("error.invalid_id"));
			return REDIRECT_SUCCESS;
		}

		paymentPointService.initFilter(paymentPointsFilter);
		paymentPointsFilter.setAllowEmpty(false);
		if (pckt.isNotNew()) {
			paymentPointsFilter.setSelectedId(pckt.getPaymentPoint().getId());
		}

		createDateFilter.setDate(pckt.getCreationDate());
		beginDateFilter.setDate(pckt.getBeginDate());
		closeDateFilter.setDate(pckt.getCloseDate());

		if (!isSubmit()) {
			packet = pckt;
			return INPUT;
		}

		if (pckt.isNew()) {
			quittancePacketService.create(pckt);
		} else {
			quittancePacketService.update(pckt);
		}

		return REDIRECT_SUCCESS;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	protected String getErrorResult() {
		return INPUT;
	}

	public String getPaymentPointDescription(@NotNull Long pointId) {
		return paymentPointHelper.getDescription(new Stub<PaymentPoint>(pointId), getLocale());
	}

	public CreateDateFilter getCreateDateFilter() {
		return createDateFilter;
	}

	public void setCreateDateFilter(CreateDateFilter createDateFilter) {
		this.createDateFilter = createDateFilter;
	}

	public BeginDateFilter getBeginDateFilter() {
		return beginDateFilter;
	}

	public void setBeginDateFilter(BeginDateFilter beginDateFilter) {
		this.beginDateFilter = beginDateFilter;
	}

	public CloseDateFilter getCloseDateFilter() {
		return closeDateFilter;
	}

	public void setCloseDateFilter(CloseDateFilter closeDateFilter) {
		this.closeDateFilter = closeDateFilter;
	}

	public PaymentPointsFilter getPaymentPointsFilter() {
		return paymentPointsFilter;
	}

	public void setPaymentPointsFilter(PaymentPointsFilter paymentPointsFilter) {
		this.paymentPointsFilter = paymentPointsFilter;
	}

	public QuittancePacket getPacket() {
		return packet;
	}

	public void setPacket(QuittancePacket packet) {
		this.packet = packet;
	}

	@Required
	public void setQuittancePacketService(QuittancePacketService quittancePacketService) {
		this.quittancePacketService = quittancePacketService;
	}

	@Required
	public void setPaymentPointService(PaymentPointService paymentPointService) {
		this.paymentPointService = paymentPointService;
	}

	@Required
	public void setPaymentPointHelper(PaymentPointHelper paymentPointHelper) {
		this.paymentPointHelper = paymentPointHelper;
	}
}
