package org.flexpay.payments.reports.payments.impl;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.morphology.currency.CurrencyToTextConverter;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.service.CurrencyInfoService;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.orgs.service.ServiceProviderService;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.persistence.Document;
import org.flexpay.payments.reports.payments.PaymentPrintForm;
import org.flexpay.payments.reports.payments.PaymentReportData;
import org.flexpay.payments.reports.payments.PaymentsReporter;
import org.flexpay.payments.service.OperationService;
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
	private ServiceProviderService serviceProviderService;
	private OperationService operationService;
	private CurrencyToTextConverter currencyToTextConverter;
	private CurrencyInfoService currencyInfoService;

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

	/**
	 * Get quittance payment print form data
	 *
	 * @param stub Payment operation to build form for
	 * @return PaymentPrintForm form data
	 * @throws IllegalArgumentException if Operation reference is invalid
	 */
	public PaymentPrintForm getPaymentPrintFormData(Stub<Operation> stub) throws IllegalArgumentException {

		Operation op = operationService.read(stub);
		if (op == null) {
			throw new IllegalArgumentException("Invalid operation stub " + stub);
		}

		Organization org = organizationService.readFull(op.getCreatorOrganizationStub());
		if (org == null) {
			throw new IllegalArgumentException("Creator organization not found " +
											   op.getCreatorOrganizationStub());
		}

		PaymentPrintForm form = new PaymentPrintForm();
		form.setQuittanceNumber(String.valueOf(op.getId()));
		form.setOperationDate(op.getCreationDate());
		form.setOrganizationName(org.getName());

		// todo: fixme
		form.setCashierFIO("Коваль А.Н.");

		form.setTotal(op.getOperationSumm());
		form.setTotalSpelling("(" + currencyToTextConverter.toText(
				op.getOperationSumm(), currencyInfoService.getDefaultCurrency()) + ")");
		form.setInputSumm(op.getOperationInputSumm());
		form.setChangeSumm(op.getChange());

		List<PaymentPrintForm.PaymentDetails> detailses = CollectionUtils.list();
		for (Document doc : op.getDocuments()) {
			PaymentPrintForm.PaymentDetails details = new PaymentPrintForm.PaymentDetails();
			details.setAccountNumber(doc.getCreditorId());
			// todo: fixme
			details.setCounterValue("");

			details.setAddress(doc.getAddress());
			details.setFio(doc.getPayerFIO());
			details.setPaymentSumm(doc.getSumm());

			details.setPaymentPeriod(DateUtil.formatMonth(DateUtil.previousMonth(op.getCreationDate())));

			Service service = spService.read(doc.getServiceStub());
			details.setServiceName(service.getServiceType().getName());

			ServiceProvider provider = serviceProviderService.read(service.getServiceProviderStub());
			details.setServiceProviderName(provider.getName());

			detailses.add(details);
		}
		form.setDetailses(detailses);

		return form;
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

	@Required
	public void setServiceProviderService(ServiceProviderService serviceProviderService) {
		this.serviceProviderService = serviceProviderService;
	}

	@Required
	public void setOperationService(OperationService operationService) {
		this.operationService = operationService;
	}

	@Required
	public void setCurrencyToTextConverter(CurrencyToTextConverter currencyToTextConverter) {
		this.currencyToTextConverter = currencyToTextConverter;
	}

	@Required
	public void setCurrencyInfoService(CurrencyInfoService currencyInfoService) {
		this.currencyInfoService = currencyInfoService;
	}
}
