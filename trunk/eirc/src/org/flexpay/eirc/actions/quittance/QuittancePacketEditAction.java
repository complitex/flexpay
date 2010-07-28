package org.flexpay.eirc.actions.quittance;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.DateUtil.now;

import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.persistence.filter.CloseDateFilter;
import org.flexpay.common.persistence.filter.CreateDateFilter;
import org.flexpay.orgs.actions.paymentpoint.PaymentPointHelper;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.persistence.filters.PaymentPointFilter;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.eirc.persistence.QuittancePacket;
import org.flexpay.eirc.service.QuittancePacketService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class QuittancePacketEditAction extends FPActionSupport {

	private QuittancePacket packet = new QuittancePacket();
	private CreateDateFilter createDateFilter = new CreateDateFilter();
	private BeginDateFilter beginDateFilter = new BeginDateFilter();
	private CloseDateFilter closeDateFilter = new CloseDateFilter();
	private PaymentPointFilter paymentPointFilter = new PaymentPointFilter();

	private String crumbCreateKey;
	private QuittancePacketService quittancePacketService;
	private PaymentPointService paymentPointService;
	private PaymentPointHelper paymentPointHelper;

	/**
	 * Perform action execution.
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return execution result code
	 * @throws Exception if failure occurs
	 */
	@NotNull
	@Override
	protected String doExecute() throws Exception {

		if (packet == null || packet.getId() == null) {
			log.warn("Incorrect quittance packet id");
			addActionError(getText("eirc.error.quittance_packet.incorrect_quittance_packet_id"));
			return REDIRECT_ERROR;
		}

		if (packet.isNotNew()) {
			Stub<QuittancePacket> stub = stub(packet);
			packet = quittancePacketService.read(stub);

			if (packet == null) {
				log.warn("Can't get quittance packet with id {} from DB", stub.getId());
				addActionError(getText("eirc.error.quittance_packet.cant_get_quittance_packet"));
				return REDIRECT_ERROR;
			} else if (packet.isNotActive()) {
				log.warn("Quittance packet with id {} is disabled", stub.getId());
				addActionError(getText("eirc.error.quittance_packet.cant_get_quittance_packet"));
				return REDIRECT_ERROR;
			}

		} else {
			packet.setPacketNumber(quittancePacketService.suggestPacketNumber());
		}

		initFilters();

		if (isSubmit()) {

			if (!doValidate()) {
				return INPUT;
			}

			updatePacket();

			addActionMessage(getText("eirc.quittance_packet.saved"));

			return REDIRECT_SUCCESS;
		}

		return INPUT;

	}

	private void initFilters() throws Exception {

		paymentPointService.initFilter(paymentPointFilter);
		paymentPointFilter.setAllowEmpty(false);

		if (packet.isNew()) {
			createDateFilter.setDate(now());
		} else {
			createDateFilter.setDate(packet.getCreationDate());
			paymentPointFilter.setSelectedId(packet.getPaymentPoint().getId());
		}

		beginDateFilter.setDate(packet.getBeginDate());
		beginDateFilter.setReadOnly(true);
		closeDateFilter.setDate(packet.getCloseDate());
		closeDateFilter.setReadOnly(true);

	}

	private void updatePacket() throws FlexPayExceptionContainer {

		packet.setPaymentPoint(paymentPointService.read(paymentPointFilter.getSelectedStub()));
		packet.setCreationDate(createDateFilter.getDate());

		if (packet.isNew()) {
			quittancePacketService.create(packet);
		} else {
			quittancePacketService.update(packet);
		}

	}

	private boolean doValidate() {
		
		if (paymentPointFilter == null || !paymentPointFilter.needFilter()) {
			log.warn("Incorrect paymentPointFilter value {}", paymentPointFilter);
			addActionError(getText("eirc.error.quittance_packet.invalid_payment_point"));
		} else {
			PaymentPoint paymentPoint = paymentPointService.read(new Stub<PaymentPoint>(paymentPointFilter.getSelectedId()));
			if (paymentPoint == null) {
				log.warn("Can't get payment point with id {} from DB", paymentPointFilter.getSelectedId());
				addActionError(getText("eirc.error.quittance_packet.cant_get_payment_point"));
				paymentPointFilter.setSelectedId(0L);
			} else if (paymentPoint.isNotActive()) {
				log.warn("Payments point with id {} is disabled", paymentPointFilter.getSelectedId());
				addActionError(getText("eirc.error.quittance_packet.cant_get_payment_point"));
				paymentPointFilter.setSelectedId(0L);
			}
		}

		if (createDateFilter == null) {
			log.warn("Incorrect create date in filter ({})", createDateFilter);
			addActionError(getText("eirc.error.quittance_packet.invalid_creation_date"));
		}

		if (packet.getControlQuittanciesNumber() == null || packet.getControlQuittanciesNumber() <= 0) {
			log.warn("Incorrect control quittances number ({})", packet.getControlQuittanciesNumber());
			addActionError(getText("eirc.error.quittance_packet.invalid_control_number"));
		}

		if (packet.getControlOverallSum() == null) {
			log.warn("Incorrect control overall sum ({})", packet.getControlOverallSum());
			addActionError(getText("eirc.error.quittance_packet.invalid_control_sum"));
		}

		return !hasActionErrors();
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	@Override
	protected String getErrorResult() {
		return INPUT;
	}

	@Override
	protected void setBreadCrumbs() {
		if (packet != null && packet.isNew()) {
			crumbNameKey = crumbCreateKey;
		}
		super.setBreadCrumbs();
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

	public PaymentPointFilter getPaymentPointFilter() {
		return paymentPointFilter;
	}

	public void setPaymentPointFilter(PaymentPointFilter paymentPointFilter) {
		this.paymentPointFilter = paymentPointFilter;
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

	@Required
	public void setCrumbCreateKey(String crumbCreateKey) {
		this.crumbCreateKey = crumbCreateKey;
	}
}
