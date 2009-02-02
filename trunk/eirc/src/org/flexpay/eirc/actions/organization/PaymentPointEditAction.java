package org.flexpay.eirc.actions.organization;

import org.flexpay.common.actions.FPActionSupport;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.eirc.persistence.Organization;
import org.flexpay.eirc.persistence.PaymentPoint;
import org.flexpay.eirc.persistence.PaymentsCollector;
import org.flexpay.eirc.persistence.filters.PaymentsCollectorFilter;
import org.flexpay.eirc.service.PaymentPointService;
import org.flexpay.eirc.service.PaymentsCollectorService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class PaymentPointEditAction extends FPActionSupport {

	private OrganizationHelper organizationHelper;
	private PaymentsCollectorService paymentsCollectorService;
	private PaymentPointService paymentPointService;

	private PaymentsCollectorFilter paymentsCollectorFilter = new PaymentsCollectorFilter();
	private PaymentPoint point = new PaymentPoint();

	public PaymentPointEditAction() {
		paymentsCollectorFilter.setAllowEmpty(false);
		paymentsCollectorFilter.setNeedAutoChange(false);
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

		if (point.getId() == null) {
			addActionError(getText("error.no_id"));
			return REDIRECT_SUCCESS;
		}

		PaymentPoint pnt = point.isNew() ? point : paymentPointService.read(stub(point));
		if (pnt == null) {
			addActionError(getText("error.invalid_id"));
			return REDIRECT_SUCCESS;
		}

		paymentsCollectorService.initFilter(paymentsCollectorFilter);

		if (pnt.isNotNew()) {
			paymentsCollectorFilter.setSelectedId(pnt.getCollector().getId());
			paymentsCollectorFilter.setReadOnly(true);
		}

		// prepare initial setup
		if (!isSubmit()) {
			point = pnt;
			return INPUT;
		}

		if (pnt.isNew()) {
			pnt.setCollector(paymentsCollectorService.read(paymentsCollectorFilter.getSelectedStub()));
		}
		pnt.setAddress(point.getAddress());

		if (pnt.isNew()) {
			paymentPointService.create(pnt);
		} else {
			paymentPointService.update(pnt);
		}

		return REDIRECT_SUCCESS;
	}

	public String getCollectorName(@NotNull PaymentsCollector collectorStub) {

		return organizationHelper.getName(collectorStub, userPreferences.getLocale());
	}

	public String getCollectorName(@NotNull Organization organizationStub) {

		return organizationHelper.getName(organizationStub, userPreferences.getLocale());
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

	public PaymentsCollectorFilter getPaymentsCollectorFilter() {
		return paymentsCollectorFilter;
	}

	public void setPaymentsCollectorFilter(PaymentsCollectorFilter paymentsCollectorFilter) {
		this.paymentsCollectorFilter = paymentsCollectorFilter;
	}

	public PaymentPoint getPoint() {
		return point;
	}

	public void setPoint(PaymentPoint point) {
		this.point = point;
	}

	@Required
	public void setPaymentPointService(PaymentPointService paymentPointService) {
		this.paymentPointService = paymentPointService;
	}

	@Required
	public void setPaymentsCollectorService(PaymentsCollectorService paymentsCollectorService) {
		this.paymentsCollectorService = paymentsCollectorService;
	}

	@Required
	public void setOrganizationHelper(OrganizationHelper organizationHelper) {
		this.organizationHelper = organizationHelper;
	}
}
