package org.flexpay.orgs.actions.paymentcollector;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.orgs.actions.organization.OrganizationHelper;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.persistence.filters.PaymentCollectorFilter;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.orgs.service.PaymentCollectorService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class PaymentCollectorDetailsAction extends FPActionWithPagerSupport<PaymentPoint> {

	protected PaymentCollectorFilter paymentCollectorFilter = new PaymentCollectorFilter();
	protected List<PaymentPoint> points = CollectionUtils.list();

	private PaymentCollector paymentCollector;

	protected OrganizationHelper organizationHelper;
	protected PaymentPointService paymentPointService;
	protected PaymentCollectorService collectorService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		paymentCollector = paymentCollectorFilter.needFilter() ? collectorService.read(paymentCollectorFilter.getSelectedStub()) : null;

		collectorService.initFilter(paymentCollectorFilter);
		ArrayStack filters = CollectionUtils.arrayStack(paymentCollectorFilter);
		points = paymentPointService.listPoints(filters, getPager());

		return SUCCESS;
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
		return SUCCESS;
	}

	public boolean isPaymentCollectorLoaded() {
		return paymentCollector != null;
	}

	public String getPaymentCollectorDescription() {
		return getTranslation(paymentCollector.getDescriptions()).getName();
	}

	public String getOrganizationName() {
		return organizationHelper.getName(paymentCollector.getOrganization(), getUserPreferences().getLocale());
	}

	public PaymentCollector getPaymentCollector() {
		return paymentCollector;
	}

	public PaymentCollectorFilter getPaymentCollectorFilter() {
		return paymentCollectorFilter;
	}

	public void setPaymentCollectorFilter(PaymentCollectorFilter paymentCollectorFilter) {
		this.paymentCollectorFilter = paymentCollectorFilter;
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
	public void setCollectorService(PaymentCollectorService collectorService) {
		this.collectorService = collectorService;
	}

}
