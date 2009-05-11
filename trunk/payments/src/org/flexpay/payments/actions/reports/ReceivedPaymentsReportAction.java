package org.flexpay.payments.actions.reports;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.orgs.service.ServiceProviderService;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.persistence.ServiceType;
import org.flexpay.payments.service.OperationService;
import org.flexpay.payments.service.SPService;
import org.flexpay.payments.service.ServiceTypeService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.apache.commons.lang.time.DateUtils;

import java.util.Date;
import java.util.List;

public class ReceivedPaymentsReportAction extends FPActionSupport {

	// form data
	private BeginDateFilter beginDateFilter = new BeginDateFilter();
	private List<Organization> organizations = CollectionUtils.list();
	private Long organizationId;

	private List<Operation> operations = CollectionUtils.list();

	// required services
	private OrganizationService organizationService;
	private OperationService operationService;

	private SPService spService;
	private ServiceTypeService serviceTypeService;
	private ServiceProviderService serviceProviderService;

	@NotNull
	protected String doExecute() throws Exception {

		organizations = organizationService.listOrganizationsWithCollectors();

		log.debug("Collector organizations: {}", organizations);

		if (isSubmit()) {
			Date beginDate = DateUtil.truncateDay(beginDateFilter.getDate());
			Date endDate = DateUtil.truncateDay(beginDateFilter.getDate());
			endDate = DateUtils.setHours(endDate, 23);
			endDate = DateUtils.setMinutes(endDate, 59);
			endDate = DateUtils.setSeconds(endDate, 59);

			log.debug("Report for org: {}", organizationId);
			log.debug("Report period: {} - {}", beginDate, endDate);

			operations = operationService.listReceivedPayments(organizationId, beginDate, endDate);
		} else {
			beginDateFilter.setDate(DateUtil.now());
		}

		return SUCCESS;
	}

	@NotNull
	protected String getErrorResult() {
		
		return SUCCESS;
	}

	// rendering utility methods
	public boolean operationsAreEmpty() {
		return operations.isEmpty() ;
	}

	// form data
	public BeginDateFilter getBeginDateFilter() {
		return beginDateFilter;
	}

	public void setBeginDateFilter(BeginDateFilter beginDateFilter) {
		this.beginDateFilter = beginDateFilter;
	}

	public List<Organization> getOrganizations() {
		return organizations;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public List<Operation> getOperations() {
		return operations;
	}

	public String getServiceTypeName(Service serviceStub) {

		Stub<Service> stub = new Stub<Service>(serviceStub);
		Service service = spService.read(stub);
		ServiceType type = serviceTypeService.getServiceType(service.getServiceType());
		return  type.getName(getLocale());
	}

	public String getServiceProviderName(Service serviceStub) {

		Stub<Service> stub = new Stub<Service>(serviceStub);
		Service service = spService.read(stub);
		ServiceProvider provider = serviceProviderService.read(service.getServiceProviderStub());
		return provider.getName(getLocale());
	}

	// required services
	@Required
	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}

	@Required
	public void setOperationService(OperationService operationService) {
		this.operationService = operationService;
	}

	@Required
	public void setSpService(SPService spService) {
		this.spService = spService;
	}

	@Required
	public void setServiceTypeService(ServiceTypeService serviceTypeService) {
		this.serviceTypeService = serviceTypeService;
	}


	public void setServiceProviderService(ServiceProviderService serviceProviderService) {
		this.serviceProviderService = serviceProviderService;
	}
}
