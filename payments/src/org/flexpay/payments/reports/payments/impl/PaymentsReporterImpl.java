package org.flexpay.payments.reports.payments.impl;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.morphology.currency.CurrencyToTextConverter;
import org.flexpay.common.service.CurrencyInfoService;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.TranslationUtil;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.orgs.service.ServiceProviderService;
import org.flexpay.payments.persistence.*;
import org.flexpay.payments.reports.payments.PaymentPrintForm;
import org.flexpay.payments.reports.payments.PaymentReportData;
import org.flexpay.payments.reports.payments.PaymentsReporter;
import org.flexpay.payments.reports.payments.ReceivedPaymentsPrintInfoData;
import static org.flexpay.payments.reports.payments.ReceivedPaymentsPrintInfoData.OperationPrintInfo;
import org.flexpay.payments.service.DocumentService;
import org.flexpay.payments.service.OperationService;
import org.flexpay.payments.service.SPService;
import org.flexpay.payments.service.ServiceTypeService;
import org.flexpay.payments.service.statistics.OperationTypeStatistics;
import org.flexpay.payments.service.statistics.PaymentsStatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PaymentsReporterImpl implements PaymentsReporter {

	private static final Logger log = LoggerFactory.getLogger(PaymentsReporterImpl.class);

	private PaymentsStatisticsService paymentsStatisticsService;
	private DocumentService documentService;
	private OrganizationService organizationService;
	private SPService spService;
	private ServiceProviderService serviceProviderService;
	private ServiceTypeService serviceTypeService;
	private OperationService operationService;
	private CurrencyToTextConverter currencyToTextConverter;
	private CurrencyInfoService currencyInfoService;

	public List<PaymentReportData> getPaymentsData(Date begin, Date end) {

		List<PaymentReportData> result = CollectionUtils.list();
		List<Document> documents = documentService.listRegisteredPaymentDocuments(begin, end);
		int operationCount = 0;
		Long previousOperationId = 0L;
		for (Document doc : documents) {
			PaymentReportData reportData = new PaymentReportData();
			Operation operation = doc.getOperation();
			ServiceType serviceType = doc.getService().getServiceType();

			if (!operation.getId().equals(previousOperationId)) {
				operationCount++;
			}

			reportData.setPaymentPointId(operation.getPaymentPoint().getId());
			reportData.setDocumentId(doc.getId());
			reportData.setDocumentSumm(doc.getSumm());
			reportData.setFio(doc.getPayerFIO());
			reportData.setOperationId(operation.getId());
			reportData.setOperationCount(operationCount);
			reportData.setServiceProviderAccount(doc.getDebtorId());
			reportData.setServiceTypeCode(serviceType.getCode());

			result.add(reportData);
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
		form.setPaymentPointStub(op.getPaymentPointStub());

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

	/**
	 * {@inheritDoc}
	 */
	public ReceivedPaymentsPrintInfoData getReceivedPaymentsPrintFormData(Date begin, Date end, PaymentPoint paymentPoint, Locale locale) {

		Organization organization = getOrganization(paymentPoint);

		ReceivedPaymentsPrintInfoData result = new ReceivedPaymentsPrintInfoData();
		List<ReceivedPaymentsPrintInfoData.OperationPrintInfo> operationPrintInfos = CollectionUtils.list();

		List<Operation> operations = operationService.listReceivedPayments(organization, begin, end);
		for (Operation operation : operations) {
			operationPrintInfos.add(convert(operation));
		}
		result.setOperationDetailses(operationPrintInfos);

		setTotals(begin, end, organization, paymentPoint, result, locale);
		return result;
	}

	private Organization getOrganization(PaymentPoint paymentPoint) {
		Long organizationId = paymentPoint.getCollector().getOrganization().getId();
		Organization organization  = organizationService.readFull(new Stub<Organization>(organizationId));
		return organization;
	}

	private void setTotals(Date begin, Date end, Organization organization, PaymentPoint paymentPoint,
						   ReceivedPaymentsPrintInfoData result, Locale locale) {

		result.setCreationDate(new Date());
		result.setBeginDate(begin);
		result.setEndDate(end);
		result.setPaymentPointName(TranslationUtil.getTranslation(paymentPoint.getNames(), locale).getName());
		result.setPaymentPointAddress(paymentPoint.getAddress());

		result.setCashierFio("Коваль А.Н."); // TODO : FIXME
	}

	private OperationPrintInfo convert(Operation operation) {
		OperationPrintInfo operationPrintInfo = new OperationPrintInfo();
		operationPrintInfo.setOperationId(operation.getId());
		operationPrintInfo.setSumm(operation.getOperationSumm());
		operationPrintInfo.setPayerFio(operation.getPayerFIO());

		// setting service payments
		Map<Integer, BigDecimal> servicePayments = CollectionUtils.map();
		for (Document document : operation.getDocuments()) {
			ServiceType serviceType = serviceTypeService.read(document.getService().getServiceTypeStub());
			servicePayments.put(serviceType.getCode(), document.getSumm());
		}
		operationPrintInfo.setServicePayments(servicePayments);

		return operationPrintInfo;
	}

	public String getServiceTypeName(Service serviceStub, Locale locale) {

		Stub<Service> stub = new Stub<Service>(serviceStub);
		Service service = spService.read(stub);
		ServiceType type = serviceTypeService.read(service.getServiceTypeStub());
		return type.getName(locale);
	}

	public String getServiceProviderName(Service serviceStub, Locale locale) {

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
		return provider.getName(locale);
	}

	public long getPaymentsCount(List<OperationTypeStatistics> typeStatisticses) {
		long count = 0;
		for (OperationTypeStatistics stats : typeStatisticses) {
			if (OperationType.isPaymentCode(stats.getOperationTypeCode())) {
				count += stats.getCount();
			}
		}
		return count;
	}

	public BigDecimal getPaymentsSumm(List<OperationTypeStatistics> typeStatisticses) {
		BigDecimal summ = BigDecimal.ZERO;
		for (OperationTypeStatistics stats : typeStatisticses) {
			if (OperationType.isPaymentCode(stats.getOperationTypeCode())) {
				summ = summ.add(stats.getSumm());
			}
		}
		return summ;
	}

	public long getReturnsCount(List<OperationTypeStatistics> typeStatisticses) {
		long count = 0;
		for (OperationTypeStatistics stats : typeStatisticses) {
			if (OperationType.isReturnCode(stats.getOperationTypeCode())) {
				count += stats.getCount();
			}
		}
		return count;
	}

	public BigDecimal getReturnsSumm(List<OperationTypeStatistics> typeStatisticses) {
		BigDecimal summ = BigDecimal.ZERO;
		for (OperationTypeStatistics stats : typeStatisticses) {
			if (OperationType.isReturnCode(stats.getOperationTypeCode())) {
				summ = summ.add(stats.getSumm());
			}
		}
		return summ;
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
	public void setServiceTypeService(ServiceTypeService serviceTypeService) {
		this.serviceTypeService = serviceTypeService;
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

	@Required
	public void setDocumentService(DocumentService documentService) {
		this.documentService = documentService;
	}
}