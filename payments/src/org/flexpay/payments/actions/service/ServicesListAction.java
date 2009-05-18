package org.flexpay.payments.actions.service;

import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.persistence.filter.EndDateFilter;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.service.SPService;
import org.flexpay.payments.actions.PaymentPointAwareAction;
import org.flexpay.orgs.persistence.filters.ServiceProviderFilter;
import org.flexpay.orgs.service.ServiceProviderService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class ServicesListAction extends FPActionWithPagerSupport<Service> implements PaymentPointAwareAction {

	private BeginDateFilter beginDateFilter = new BeginDateFilter();
	private EndDateFilter endDateFilter = new EndDateFilter();
	private ServiceProviderFilter serviceProviderFilter = new ServiceProviderFilter();

	private List<Service> services;

	private ServiceProviderService providerService;
	private SPService spService;

	@NotNull
	public String doExecute() throws Exception {

		serviceProviderFilter = providerService.initServiceProvidersFilter(serviceProviderFilter);

		List<ObjectFilter> filters = CollectionUtils.list(beginDateFilter, endDateFilter, serviceProviderFilter);

		services = spService.listServices(filters, getPager());

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
	protected String getErrorResult() {
		return SUCCESS;
	}

	public List<Service> getServices() {
		return services;
	}

	public BeginDateFilter getBeginDateFilter() {
		return beginDateFilter;
	}

	public void setBeginDateFilter(BeginDateFilter beginDateFilter) {
		this.beginDateFilter = beginDateFilter;
	}

	public EndDateFilter getEndDateFilter() {
		return endDateFilter;
	}

	public void setEndDateFilter(EndDateFilter endDateFilter) {
		this.endDateFilter = endDateFilter;
	}

	public ServiceProviderFilter getServiceProviderFilter() {
		return serviceProviderFilter;
	}

	public void setServiceProviderFilter(ServiceProviderFilter serviceProviderFilter) {
		this.serviceProviderFilter = serviceProviderFilter;
	}

	@Required
	public void setSpService(SPService spService) {
		this.spService = spService;
	}

	@Required
	public void setProviderService(ServiceProviderService providerService) {
		this.providerService = providerService;
	}

	private String paymentPointId;

	public void setPaymentPointId(String paymentPointId) {
		this.paymentPointId = paymentPointId;
	}

	public String getPaymentPointId() {
		return paymentPointId;
	}
}
