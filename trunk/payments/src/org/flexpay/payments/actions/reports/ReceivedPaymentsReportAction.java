package org.flexpay.payments.actions.reports;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.service.OperationService;
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

	@NotNull
	protected String doExecute() throws Exception {

		organizations = organizationService.listOrganizationsWithCollectors();

		if (isSubmit()) {
			Date beginDate = DateUtil.truncateDay(beginDateFilter.getDate());
			Date endDate = DateUtil.truncateDay(beginDateFilter.getDate());
			endDate = DateUtils.setHours(endDate, 23);
			endDate = DateUtils.setMinutes(endDate, 59);
			endDate = DateUtils.setSeconds(endDate, 59);

			operations = operationService.listReceivedPayments(organizationId, beginDate, endDate);
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

	// required services
	@Required
	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}

	@Required
	public void setOperationService(OperationService operationService) {
		this.operationService = operationService;
	}
}
