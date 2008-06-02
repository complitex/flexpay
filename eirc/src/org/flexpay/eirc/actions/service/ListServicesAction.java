package org.flexpay.eirc.actions.service;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.filter.EndDateFilter;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.eirc.service.SPService;
import org.flexpay.eirc.persistence.Service;
import org.flexpay.eirc.persistence.filters.ServiceProviderFilter;

import java.util.List;
import java.util.ArrayList;

public class ListServicesAction extends FPActionSupport {

	private SPService spService;

	private BeginDateFilter beginDateFilter = new BeginDateFilter();
	private EndDateFilter endDateFilter = new EndDateFilter();
	private ServiceProviderFilter serviceProviderFilter = new ServiceProviderFilter();
	private Page<Service> pager = new Page<Service>();

	private List<Service> services;

	public String execute() throws Exception {

		serviceProviderFilter = spService.initServiceProvidersFilter(serviceProviderFilter);

		List<ObjectFilter> filters = new ArrayList<ObjectFilter>();
		filters.add(beginDateFilter);
		filters.add(endDateFilter);
		filters.add(serviceProviderFilter);

		services = spService.listServices(filters, pager);

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

	public Page<Service> getPager() {
		return pager;
	}

	public void setPager(Page<Service> pager) {
		this.pager = pager;
	}

	public void setSpService(SPService spService) {
		this.spService = spService;
	}
}
