package org.flexpay.orgs.actions.organization;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.persistence.PaymentsCollector;
import org.flexpay.orgs.persistence.filters.PaymentsCollectorFilter;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.orgs.service.PaymentsCollectorService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collections;
import java.util.List;

public class PaymentPointsListAction extends FPActionWithPagerSupport<PaymentPoint> {

	protected PaymentsCollectorFilter paymentsCollectorFilter = new PaymentsCollectorFilter();
	protected List<PaymentPoint> points = Collections.emptyList();

	protected OrganizationHelper organizationHelper;
	protected PaymentsCollectorService collectorService;
	protected PaymentPointService paymentPointService;

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

		loadPaymentPoints();

		return SUCCESS;
	}

	protected void loadPaymentPoints() {
		collectorService.initFilter(paymentsCollectorFilter);
		ArrayStack filters = CollectionUtils.arrayStack(paymentsCollectorFilter);
		points = paymentPointService.listPoints(filters, getPager());
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
		return SUCCESS;
	}

	public PaymentsCollectorFilter getPaymentsCollectorFilter() {
		return paymentsCollectorFilter;
	}

	public Long getPaymentsCollectorId(Long paymentPointId) {
		PaymentPoint paymentPoint = paymentPointService.read(new Stub<PaymentPoint>(paymentPointId));
		return paymentPoint.getCollector().getId();
	}

	public void setPaymentsCollectorFilter(PaymentsCollectorFilter paymentsCollectorFilter) {
		this.paymentsCollectorFilter = paymentsCollectorFilter;
	}

	public List<PaymentPoint> getPoints() {
		return points;
	}

	@Required
	public void setPaymentPointService(PaymentPointService paymentPointService) {
		this.paymentPointService = paymentPointService;
	}

	@Required
	public void setOrganizationHelper(OrganizationHelper organizationHelper) {
		this.organizationHelper = organizationHelper;
	}

	@Required
	public void setCollectorService(PaymentsCollectorService collectorService) {
		this.collectorService = collectorService;
	}

}
