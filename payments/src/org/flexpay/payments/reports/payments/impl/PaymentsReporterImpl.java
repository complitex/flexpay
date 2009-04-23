package org.flexpay.payments.reports.payments.impl;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.reports.payments.PaymentReportData;
import org.flexpay.payments.reports.payments.PaymentsReporter;
import org.flexpay.payments.service.SPService;
import org.flexpay.payments.service.statistics.PaymentsStatisticsService;
import org.flexpay.payments.service.statistics.ServicePaymentsStatistics;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;
import java.util.List;

public class PaymentsReporterImpl implements PaymentsReporter {

	private PaymentsStatisticsService paymentsStatisticsService;
	private OrganizationService organizationService;
	private SPService spService;

	public List<PaymentReportData> getPaymentsData(Date begin, Date end) {

		List<PaymentReportData> result = CollectionUtils.list();
		List<ServicePaymentsStatistics> statisticses = paymentsStatisticsService.servicePaymentStatistics(begin, end);
		for (ServicePaymentsStatistics statistics : statisticses) {
			PaymentReportData data = new PaymentReportData();
			data.setBeginDate(begin);
			data.setEndDate(end);

			Service service = spService.read(new Stub<Service>(statistics.getServiceId()));
			data.setServiceId(service.getId());
			data.setServiceName(service.getServiceType().getName());

			Organization organization = organizationService.readFull(
					new Stub<Organization>(statistics.getOrganizationId()));
			data.setBankId(organization.getId());
			data.setSubdivisionName(organization.getName());

			data.setPayedCacheSumm(statistics.getPayedCacheSumm());
			data.setPayedCachelessSumm(statistics.getPayedCachelessSumm());
			data.setReturnedCacheSumm(statistics.getReturnedCacheSumm());
			data.setReturnedCachelessSumm(statistics.getReturnedCachelessSumm());

			result.add(data);
		}

		return result;
	}

	@Required
	public void setPaymentsStatisticsService(PaymentsStatisticsService paymentsStatisticsService) {
		this.paymentsStatisticsService = paymentsStatisticsService;
	}

	@Required
	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}

	@Required
	public void setSpService(SPService spService) {
		this.spService = spService;
	}
}
