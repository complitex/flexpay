package org.flexpay.eirc.actions.organization;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.eirc.persistence.Organization;
import org.flexpay.eirc.persistence.PaymentPoint;
import org.flexpay.eirc.persistence.PaymentsCollector;
import org.flexpay.eirc.persistence.filters.PaymentsCollectorFilter;
import org.flexpay.eirc.service.PaymentPointService;
import org.flexpay.eirc.service.PaymentsCollectorService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collections;
import java.util.List;

public class PaymentPointsListAction extends FPActionWithPagerSupport<PaymentPoint> {

	private PaymentsCollectorFilter paymentsCollectorFilter = new PaymentsCollectorFilter();
	private List<PaymentPoint> points = Collections.emptyList();

	private OrganizationHelper organizationHelper;
	private PaymentsCollectorService collectorService;
	private PaymentPointService paymentPointService;

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

		collectorService.initFilter(paymentsCollectorFilter);

		ArrayStack filters = CollectionUtils.arrayStack(paymentsCollectorFilter);
		points = paymentPointService.listPoints(filters, getPager());

		return SUCCESS;
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
