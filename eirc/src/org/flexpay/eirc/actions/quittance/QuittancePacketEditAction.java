package org.flexpay.eirc.actions.quittance;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.persistence.filter.CloseDateFilter;
import org.flexpay.common.persistence.filter.CreateDateFilter;
import org.flexpay.common.util.DateUtil;
import org.flexpay.orgs.actions.paymentpoint.PaymentPointHelper;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.persistence.filters.PaymentPointsFilter;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.eirc.persistence.QuittancePacket;
import org.flexpay.eirc.service.QuittancePacketService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class QuittancePacketEditAction extends FPActionSupport {

	private CreateDateFilter createDateFilter = new CreateDateFilter();
	private BeginDateFilter beginDateFilter = new BeginDateFilter();
	private CloseDateFilter closeDateFilter = new CloseDateFilter();
	private PaymentPointsFilter paymentPointsFilter = new PaymentPointsFilter();
	private QuittancePacket packet = new QuittancePacket();

	private QuittancePacketService quittancePacketService;
	private PaymentPointService paymentPointService;
	private PaymentPointHelper paymentPointHelper;

	public QuittancePacketEditAction() {
		beginDateFilter.setReadOnly(true);
		closeDateFilter.setReadOnly(true);

		paymentPointsFilter.setAllowEmpty(false);
	}

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
			addActionError(getText("common.error.invalid_id"));
			return REDIRECT_SUCCESS;
		}

		QuittancePacket pckt = packet.isNew() ? packet : quittancePacketService.read(stub(packet));
		if (pckt == null) {
			addActionError(getText("common.error.invalid_id"));
			return REDIRECT_SUCCESS;
		}

		paymentPointService.initFilter(paymentPointsFilter);
		if (pckt.isNotNew()) {
			paymentPointsFilter.setSelectedId(pckt.getPaymentPoint().getId());
		}

		if (!isSubmit()) {

			if (pckt.isNew()) {
				createDateFilter.setDate(DateUtil.now());
				pckt.setPacketNumber(quittancePacketService.suggestPacketNumber());
			} else {
				createDateFilter.setDate(pckt.getCreationDate());
			}

			beginDateFilter.setDate(pckt.getBeginDate());
			closeDateFilter.setDate(pckt.getCloseDate());
			packet = pckt;
			return INPUT;
		}

		// setup properties
		pckt.setPacketNumber(packet.getPacketNumber());
		pckt.setPaymentPoint(paymentPointService.read(paymentPointsFilter.getSelectedStub()));
		pckt.setCreationDate(createDateFilter.getDate());
		pckt.setControlQuittanciesNumber(packet.getControlQuittanciesNumber());
		pckt.setControlOverallSumm(packet.getControlOverallSumm());

//		pckt.setBeginDate(beginDateFilter.getDate());
//		pckt.setCloseDate(closeDateFilter.getDate());
//		pckt.setQuittanciesNumber(packet.getQuittanciesNumber());
//		pckt.setOverallSumm(packet.getOverallSumm());
//		pckt.setCreatorUserName(packet.getCreatorUserName());
//		pckt.setCloserUserName(packet.getCloserUserName());

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
