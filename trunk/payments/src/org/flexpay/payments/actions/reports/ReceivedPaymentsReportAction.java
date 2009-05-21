package org.flexpay.payments.actions.reports;

import org.apache.commons.lang.time.DateUtils;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.util.BigDecimalFormat;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateUtil;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.orgs.service.ServiceProviderService;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.persistence.OperationType;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.persistence.ServiceType;
import org.flexpay.payments.service.OperationService;
import org.flexpay.payments.service.SPService;
import org.flexpay.payments.service.ServiceTypeService;
import org.flexpay.payments.service.statistics.OperationTypeStatistics;
import org.flexpay.payments.service.statistics.PaymentsStatisticsService;
import org.flexpay.payments.actions.PaymentPointAwareAction;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ReceivedPaymentsReportAction extends FPActionSupport implements PaymentPointAwareAction {

	// form data
	private BeginDateFilter beginDateFilter = new BeginDateFilter();

	private List<Operation> operations = Collections.emptyList();
	private List<OperationTypeStatistics> typeStatisticses = Collections.emptyList();

	// required services
	private OperationService operationService;

	private SPService spService;
	private ServiceTypeService serviceTypeService;
	private ServiceProviderService serviceProviderService;
	private PaymentPointService paymentPointService;
	private OrganizationService organizationService;

	private PaymentsStatisticsService statisticsService;

	private Long paymentPointId;

	@NotNull
	protected String doExecute() throws Exception {

		if (isSubmit()) {
			Date beginDate = DateUtil.truncateDay(beginDateFilter.getDate());
			Date endDate = DateUtil.truncateDay(beginDateFilter.getDate());
			endDate = DateUtils.setHours(endDate, 23);
			endDate = DateUtils.setMinutes(endDate, 59);
			endDate = DateUtils.setSeconds(endDate, 59);

			operations = operationService.listReceivedPayments(getSelfOrganization(), beginDate, endDate);

			typeStatisticses = statisticsService.operationTypeStatistics(
								new Stub<Organization>(getSelfOrganization().getId()), beginDate, endDate);
		} else {
			beginDateFilter.setDate(DateUtil.now());
		}

		return SUCCESS;
	}

	@NotNull
	protected String getErrorResult() {

		return SUCCESS;
	}

	private Organization getSelfOrganization() {

		PaymentPoint paymentPoint = paymentPointService.read(new Stub<PaymentPoint>(paymentPointId));
		Long organizationId = paymentPoint.getCollector().getOrganization().getId();
		return organizationService.readFull(new Stub<Organization>(organizationId));
	}

	// rendering utility methods
	public boolean operationsAreEmpty() {
		return operations.isEmpty();
	}

	// form data
	public BeginDateFilter getBeginDateFilter() {
		return beginDateFilter;
	}

	public void setBeginDateFilter(BeginDateFilter beginDateFilter) {
		this.beginDateFilter = beginDateFilter;
	}

	public List<Operation> getOperations() {
		return operations;
	}

	public String getServiceTypeName(Service serviceStub) {

		Stub<Service> stub = new Stub<Service>(serviceStub);
		Service service = spService.read(stub);
		if (service == null) {
			log.warn("No service found by stub {}", serviceStub);
			return null;
		}
		ServiceType type = serviceTypeService.getServiceType(service.getServiceType());
		return type.getName(getLocale());
	}

	public String getServiceProviderName(Service serviceStub) {

		Stub<Service> stub = new Stub<Service>(serviceStub);
		Service service = spService.read(stub);
		if (service == null) {
			log.warn("No service found by stub {}", serviceStub);
			return null;
		}
		ServiceProvider provider = serviceProviderService.read(service.getServiceProviderStub());
		if (provider == null) {
			log.warn("No service provider found {}", service.getServiceProviderStub());
			return null;
		}
		return provider.getName(getLocale());
	}

	public long getPaymentsCount() {
		long count = 0;
		for (OperationTypeStatistics stats : typeStatisticses) {
			if (OperationType.isPaymentCode(stats.getOperationTypeCode())) {
				count += stats.getCount();
			}
		}
		return count;
	}

	public String getPaymentsSumm() {
		BigDecimal summ = BigDecimal.ZERO;
		for (OperationTypeStatistics stats : typeStatisticses) {
			if (OperationType.isPaymentCode(stats.getOperationTypeCode())) {
				summ = summ.add(stats.getSumm());
			}
		}

		return BigDecimalFormat.format(summ, 2).toPlainString();
	}

	public long getReturnsCount() {
		long count = 0;
		for (OperationTypeStatistics stats : typeStatisticses) {
			if (OperationType.isReturnCode(stats.getOperationTypeCode())) {
				count += stats.getCount();
			}
		}
		return count;
	}

	public String getReturnsSumm() {
		BigDecimal summ = BigDecimal.ZERO;
		for (OperationTypeStatistics stats : typeStatisticses) {
			if (OperationType.isReturnCode(stats.getOperationTypeCode())) {
				summ = summ.add(stats.getSumm());
			}
		}
		return BigDecimalFormat.format(summ, 2).toPlainString();
	}

	// required services
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

	@Required
	public void setServiceProviderService(ServiceProviderService serviceProviderService) {
		this.serviceProviderService = serviceProviderService;
	}

	@Required
	public void setStatisticsService(PaymentsStatisticsService statisticsService) {
		this.statisticsService = statisticsService;
	}

	@Required
	public void setPaymentPointService(PaymentPointService paymentPointService) {
		this.paymentPointService = paymentPointService;
	}

	@Required
	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}

	public Long getPaymentPointId() {
		return paymentPointId;
	}

	public void setPaymentPointId(Long paymentPointId) {
		this.paymentPointId = paymentPointId;
	}
}
