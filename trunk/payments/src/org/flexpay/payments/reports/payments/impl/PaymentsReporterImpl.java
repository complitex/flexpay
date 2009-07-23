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
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.orgs.service.ServiceProviderService;
import org.flexpay.orgs.service.CashboxService;
import org.flexpay.payments.persistence.*;
import static org.flexpay.payments.reports.payments.PaymentsPrintInfoData.OperationPrintInfo;
import org.flexpay.payments.reports.payments.*;
import org.flexpay.payments.service.DocumentService;
import org.flexpay.payments.service.OperationService;
import org.flexpay.payments.service.SPService;
import org.flexpay.payments.service.ServiceTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PaymentsReporterImpl implements PaymentsReporter {

	// logger
	private Logger log = LoggerFactory.getLogger(getClass());

	// service types
	private static final int SERVICE_TYPE_KVARTPLATA = 1;
	private static final int SERVICE_TYPE_DOGS = 2;
	private static final int SERVICE_TYPE_GARAGE = 3;
	private static final int SERVICE_TYPE_WARMING = 4;
	private static final int SERVICE_TYPE_HOT_WATER = 7;
	private static final int SERVICE_TYPE_COLD_WATER = 6;
	private static final int SERVICE_TYPE_SEWER = 13; // TODO: FIXME find proper one

	// required services
	private DocumentService documentService;
	private OrganizationService organizationService;
	private PaymentPointService paymentPointService;
	private CashboxService cashboxService;
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
	 */
	public PaymentPrintForm getPaymentPrintFormData(Stub<Operation> stub) {

		Operation op = operationService.read(stub);
		if (op == null) {
			log.debug("Operation with id {} does not exist", stub.getId());
			return null;
		}

		return getPaymentPrintFormData(op);
	}

	/**
	 * Get quittance payment print form data
	 *
	 * @param op Payment operation to build form for
	 * @return PaymentPrintForm form data
	 */
	public PaymentPrintForm getPaymentPrintFormData(Operation op) {

		Organization org = organizationService.readFull(op.getCreatorOrganizationStub());
		if (org == null) {
			log.error("Can't find organization with id {}", op.getCreatorOrganizationStub());
			return null;
		}

		PaymentPoint paymentPoint = paymentPointService.read(op.getPaymentPointStub());

		PaymentPrintForm form = new PaymentPrintForm();
		form.setQuittanceNumber(op.getId().toString());
		form.setOperationDate(op.getCreationDate());
		form.setOrganizationName(org.getName());
		form.setPaymentPointStub(op.getPaymentPointStub());
		form.setPayerFIO(op.getPayerFIO());
		form.setPaymentPointAddress(paymentPoint.getAddress());
		form.setPaymentPointName(paymentPoint.getName());
		form.setCashierFIO(op.getCashierFio());

		form.setTotal(op.getOperationSumm());
		form.setTotalSpelling("(" + currencyToTextConverter.toText(
				op.getOperationSumm(), currencyInfoService.getDefaultCurrency()) + ")");
		form.setInputSumm(op.getOperationInputSumm());
		form.setChangeSumm(op.getChange());

		List<PaymentPrintForm.PaymentDetails> detailses = CollectionUtils.list();
		for (Document doc : op.getDocuments()) {
			PaymentPrintForm.PaymentDetails details = new PaymentPrintForm.PaymentDetails();
			details.setAccountNumber(doc.getCreditorId());

			details.setCounterValue(""); // todo: fixme

			details.setAddress(doc.getAddress());
			details.setFio(doc.getPayerFIO());
			details.setPaymentSumm(doc.getSumm());
			details.setDebt(doc.getDebt());

			details.setPaymentPeriod(DateUtil.formatMonth(DateUtil.previousMonth(op.getCreationDate())));

			Service service = spService.readFull(doc.getServiceStub());
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
	public PaymentsPrintInfoData getReturnedPaymentsPrintFormData(Date begin, Date end, Cashbox cashbox, Locale locale) {

		PaymentsPrintInfoData result = new PaymentsPrintInfoData();
		PaymentPoint paymentPoint = getPaymentPoint(cashbox);

		result.setOperationDetailses(convert(getReturnedPayments(begin, end, cashbox)));
		result.setCreationDate(new Date());
		result.setBeginDate(begin);
		result.setEndDate(end);
		result.setPaymentPointName(TranslationUtil.getTranslation(paymentPoint.getNames(), locale).getName());
		result.setPaymentPointAddress(paymentPoint.getAddress());
		Organization collectorOrganization = organizationService.readFull(paymentPoint.getCollector().getOrganizationStub());
		result.setPaymentCollectorOrgName(collectorOrganization.getName(locale));
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	public PaymentsPrintInfoData getReceivedPaymentsPrintFormData(Date begin, Date end, Cashbox cashbox, Locale locale) {

		PaymentsPrintInfoData result = new PaymentsPrintInfoData();
		PaymentPoint paymentPoint = getPaymentPoint(cashbox);

		result.setOperationDetailses(convert(getReceivedPayments(begin, end, cashbox)));
		result.setCreationDate(new Date());
		result.setBeginDate(begin);
		result.setEndDate(end);
		result.setPaymentPointName(TranslationUtil.getTranslation(paymentPoint.getNames(), locale).getName());
		result.setPaymentPointAddress(paymentPoint.getAddress());
		Organization collectorOrganization = organizationService.readFull(paymentPoint.getCollector().getOrganizationStub());
		result.setPaymentCollectorOrgName(collectorOrganization.getName(locale));
		return result;
	}

	private List<OperationPrintInfo> convert(List<Operation> operations) {
		List<OperationPrintInfo> operationPrintInfos = CollectionUtils.list();
		for (Operation operation : operations) {
			operationPrintInfos.add(convert(operation));
		}
		return operationPrintInfos;
	}

	private List<Operation> getReceivedPayments(Date begin, Date end, Cashbox cashbox) {
		return operationService.listReceivedPayments(cashbox, begin, end);
	}

	private List<Operation> getReturnedPayments(Date begin, Date end, Cashbox cashbox) {
		return operationService.listReturnedPayments(cashbox, begin, end);
	}

	private PaymentPoint getPaymentPoint(Cashbox cashbox) {

		return paymentPointService.read(cashbox.getPaymentPointStub());
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

	@Override
	public AccPaymentReportData getAccPaymentsReportData(AccPaymentsReportRequest reportRequest, Cashbox cashbox) {

		AccPaymentReportData result = new AccPaymentReportData();
		PaymentPoint paymentPoint = getPaymentPoint(cashbox);
		Organization collectorOrganization = organizationService.readFull(paymentPoint.getCollector().getOrganizationStub());

		result.setAccountantFio("Коваль А.Н."); // TODO : use actual accountant name
		result.setCreationDate(new Date());
		result.setBeginDate(reportRequest.getBeginDate());
		result.setEndDate(reportRequest.getEndDate());
		result.setDetailses(fillDetailses(reportRequest));
		result.setPaymentPointName(paymentPoint.getName(reportRequest.getLocale()));
		result.setPaymentPointAddress(paymentPoint.getAddress());
		result.setPaymentCollectorOrgName(collectorOrganization.getName(reportRequest.getLocale()));

		return result;
	}

	private List<AccPaymentReportData.PaymentDetails> fillDetailses(AccPaymentsReportRequest request) {

		Long paymentPointId = request.getPaymentPointId();
		Long cashboxId = request.getCashboxId();
		Date beginDate = request.getBeginDate();
		Date endDate = request.getEndDate();
		int status = request.getPaymentStatus();
		Locale locale = request.getLocale();

		switch (request.getDetailsLevel()) {
			case AccPaymentsReportRequest.DETAILS_LEVEL_PAYMENT_POINT:
				if (paymentPointId == null && cashboxId == null) {
					return getAllPaymentPointsNoDetails(status, beginDate, endDate, locale);
				} else if (paymentPointId != null && cashboxId == null) {
					return getPaymentPointNoDetails(paymentPointId, status, beginDate, endDate, locale);
				}
				break;
			case AccPaymentsReportRequest.DETAILS_LEVEL_CASHBOX:
				if (paymentPointId == null && cashboxId == null) {
					return getAllPaymentPointsCashboxes(status, beginDate, endDate, locale);
				} else if (paymentPointId != null && cashboxId == null) {
					return getPaymentPointCashboxes(paymentPointId, status, beginDate, endDate, locale);
				} else if (paymentPointId != null && cashboxId != null) {
					return getCashbox(cashboxId, status, beginDate, endDate, locale);
				}
				break;
			case AccPaymentsReportRequest.DETAILS_LEVEL_PAYMENT:
				if (paymentPointId == null && cashboxId == null) {
					return getAllPaymentPointsPayments(status, beginDate, endDate, locale);
				} else if (paymentPointId != null && cashboxId == null) {
					return getPaymentPointPayments(paymentPointId, status, beginDate, endDate, locale);
				} else if (paymentPointId != null && cashboxId != null) {
					return getCashboxPayments(cashboxId, status, beginDate, endDate, locale);
				}
				break;
			default:
				break;
		}

		return null;
	}

	private List<AccPaymentReportData.PaymentDetails> getAllPaymentPointsNoDetails(int status, Date beginDate, Date endDate, Locale locale) {

		List<AccPaymentReportData.PaymentDetails> result = CollectionUtils.list();

		List<PaymentPoint> allPaymentPoints = paymentPointService.findAll();
		for (PaymentPoint paymentPoint : allPaymentPoints) {
			AccPaymentReportData.PaymentDetails pointSummary = getPaymentPointSummary(new Stub<PaymentPoint>(paymentPoint.getId()), status, beginDate, endDate, locale);
			result.add(pointSummary);
		}

		return result;
	}

	private List<AccPaymentReportData.PaymentDetails> getPaymentPointNoDetails(Long paymentPointId, int status, Date beginDate, Date endDate, Locale locale) {

		AccPaymentReportData.PaymentDetails details = getPaymentPointSummary(new Stub<PaymentPoint>(paymentPointId), status, beginDate, endDate, locale);
		return CollectionUtils.list(details);
	}

	private List<AccPaymentReportData.PaymentDetails> getAllPaymentPointsCashboxes(int status, Date beginDate, Date endDate, Locale locale) {

		List<AccPaymentReportData.PaymentDetails> paymentPointSummaries = CollectionUtils.list();

		List<PaymentPoint> allPaymentPoints = paymentPointService.findAll();
		for (PaymentPoint paymentPoint : allPaymentPoints) {
			List<AccPaymentReportData.PaymentDetails> cashboxSummaries = CollectionUtils.list();
			List<Cashbox> cashboxes = cashboxService.findCashboxesForPaymentPoint(paymentPoint.getId());
			for (Cashbox cashbox : cashboxes) {
				AccPaymentReportData.PaymentDetails cashboxSummary = getCashboxSummary(new Stub<Cashbox>(cashbox.getId()), status, beginDate, endDate, locale);
				cashboxSummaries.add(cashboxSummary);
			}

			AccPaymentReportData.PaymentDetails pointSummary = getPaymentPointSummary(new Stub<PaymentPoint>(paymentPoint.getId()), status, beginDate, endDate, locale);
			pointSummary.setChildDetailses(cashboxSummaries);

			paymentPointSummaries.add(pointSummary);
		}

		return paymentPointSummaries;
	}

	private List<AccPaymentReportData.PaymentDetails> getPaymentPointCashboxes(Long paymentPointId, int status, Date beginDate, Date endDate, Locale locale) {

		List<AccPaymentReportData.PaymentDetails> cashboxDetailses = CollectionUtils.list();
		List<Cashbox> cashboxes = cashboxService.findCashboxesForPaymentPoint(paymentPointId);
		for (Cashbox cashbox : cashboxes) {
			AccPaymentReportData.PaymentDetails cashboxSummary = getCashboxSummary(new Stub<Cashbox>(cashbox.getId()), status, beginDate, endDate, locale);
			cashboxDetailses.add(cashboxSummary);
		}

		AccPaymentReportData.PaymentDetails paymentPointSummary = getPaymentPointSummary(new Stub<PaymentPoint>(paymentPointId), status, beginDate, endDate, locale);
		paymentPointSummary.setChildDetailses(cashboxDetailses);
		return CollectionUtils.list(paymentPointSummary);
	}

	private List<AccPaymentReportData.PaymentDetails> getCashbox(Long cashboxId, int status, Date beginDate, Date endDate, Locale locale) {

		AccPaymentReportData.PaymentDetails details = getCashboxSummary(new Stub<Cashbox>(cashboxId), status, beginDate, endDate, locale);
		return CollectionUtils.list(details);
	}

	private List<AccPaymentReportData.PaymentDetails> getAllPaymentPointsPayments(int status, Date beginDate, Date endDate, Locale locale) {

		List<AccPaymentReportData.PaymentDetails> paymentPointSummaries = CollectionUtils.list();

		List<PaymentPoint> allPaymentPoints = paymentPointService.findAll();
		for (PaymentPoint paymentPoint : allPaymentPoints) {
			AccPaymentReportData.PaymentDetails paymentPointSummary = getPaymentPointSummary(new Stub<PaymentPoint>(paymentPoint.getId()), status, beginDate, endDate, locale);
			paymentPointSummary.setChildDetailses(getPaymentPointPayments(paymentPoint.getId(), status, beginDate, endDate, locale));
			paymentPointSummaries.add(paymentPointSummary);
		}

		return paymentPointSummaries;
	}

	private List<AccPaymentReportData.PaymentDetails> getPaymentPointPayments(Long paymentPointId, int status, Date beginDate, Date endDate, Locale locale) {

		List<AccPaymentReportData.PaymentDetails> cashboxSummaries = CollectionUtils.list();

		List<Cashbox> cashboxes = cashboxService.findCashboxesForPaymentPoint(paymentPointId);
		for (Cashbox cashbox : cashboxes) {
			cashboxSummaries.add(getCashboxPayments(cashbox.getId(), status, beginDate, endDate, locale).get(0));
		}

		return cashboxSummaries;
	}

	private List<AccPaymentReportData.PaymentDetails> getCashboxPayments(Long cashboxId, int status, Date beginDate, Date endDate, Locale locale) {

		List<AccPaymentReportData.PaymentDetails> operationDetailses = CollectionUtils.list();
		Cashbox cashbox = cashboxService.read(new Stub<Cashbox>(cashboxId));

		List<Operation> operations;
		switch (status) {
			case OperationStatus.REGISTERED:
				operations = operationService.listReceivedPayments(cashbox, beginDate, endDate);
				break;
			case OperationStatus.RETURNED:
				operations = operationService.listReturnedPayments(cashbox, beginDate, endDate);
				break;
			default:
				operations = CollectionUtils.list();
				break;
		}

		for (Operation operation : operations) {
			AccPaymentReportData.PaymentDetails operationDetails = getPaymentDetails(new Stub<Operation>(operation.getId()));
			operationDetailses.add(operationDetails);
		}

		AccPaymentReportData.PaymentDetails result = getCashboxSummary(new Stub<Cashbox>(cashboxId), status, beginDate, endDate, locale);
		result.setChildDetailses(operationDetailses);

		return CollectionUtils.list(result);
	}

	// converts operation info into payment details
	private AccPaymentReportData.PaymentDetails getPaymentDetails(Stub<Operation> operationStub) {

		AccPaymentReportData.PaymentDetails paymentDetails = new AccPaymentReportData.PaymentDetails();
		Operation operation = operationService.read(operationStub);

		paymentDetails.setDivisionName("");
		paymentDetails.setDivisionAddress("");
		paymentDetails.setObjectId(operationStub.getId());
		paymentDetails.setPaymentKvartplata(documentService.getOperationServiceSumm(operationStub, SERVICE_TYPE_KVARTPLATA));
		paymentDetails.setPaymentDogs(documentService.getOperationServiceSumm(operationStub, SERVICE_TYPE_DOGS));
		paymentDetails.setPaymentGarage(documentService.getOperationServiceSumm(operationStub, SERVICE_TYPE_GARAGE));
		paymentDetails.setPaymentWarming(documentService.getOperationServiceSumm(operationStub, SERVICE_TYPE_WARMING));
		paymentDetails.setPaymentHotWater(documentService.getOperationServiceSumm(operationStub, SERVICE_TYPE_HOT_WATER));
		paymentDetails.setPaymentColdWater(documentService.getOperationServiceSumm(operationStub, SERVICE_TYPE_COLD_WATER));
		paymentDetails.setPaymentSewer(documentService.getOperationServiceSumm(operationStub, SERVICE_TYPE_SEWER));
		paymentDetails.setSumm(operation.getOperationSumm());

		return paymentDetails;
	}

	// builds summary for cashbox
	private AccPaymentReportData.PaymentDetails getCashboxSummary(Stub<Cashbox> cashboxStub, int status, Date beginDate, Date endDate, Locale locale) {

		AccPaymentReportData.PaymentDetails paymentDetails = new AccPaymentReportData.PaymentDetails();
		Cashbox cashbox = cashboxService.read(cashboxStub);

		paymentDetails.setDivisionName(cashbox.getName(locale));
		paymentDetails.setDivisionAddress("");
		paymentDetails.setObjectId(cashboxStub.getId());
		paymentDetails.setPaymentKvartplata(documentService.getCashboxServiceSumm(cashboxStub, status, SERVICE_TYPE_KVARTPLATA, beginDate, endDate));
		paymentDetails.setPaymentDogs(documentService.getCashboxServiceSumm(cashboxStub, status, SERVICE_TYPE_DOGS, beginDate, endDate));
		paymentDetails.setPaymentGarage(documentService.getCashboxServiceSumm(cashboxStub, status, SERVICE_TYPE_GARAGE, beginDate, endDate));
		paymentDetails.setPaymentWarming(documentService.getCashboxServiceSumm(cashboxStub, status, SERVICE_TYPE_WARMING, beginDate, endDate));
		paymentDetails.setPaymentHotWater(documentService.getCashboxServiceSumm(cashboxStub, status, SERVICE_TYPE_HOT_WATER, beginDate, endDate));
		paymentDetails.setPaymentColdWater(documentService.getCashboxServiceSumm(cashboxStub, status, SERVICE_TYPE_COLD_WATER, beginDate, endDate));
		paymentDetails.setPaymentSewer(documentService.getCashboxServiceSumm(cashboxStub, status, SERVICE_TYPE_SEWER, beginDate, endDate));
		paymentDetails.setSumm(documentService.getCashboxTotalSumm(cashboxStub, status, beginDate, endDate));

		return paymentDetails;
	}

	// builds summary for payment point
	private AccPaymentReportData.PaymentDetails getPaymentPointSummary(Stub<PaymentPoint> paymentPointStub, int status, Date beginDate, Date endDate, Locale locale) {

		AccPaymentReportData.PaymentDetails paymentDetails = new AccPaymentReportData.PaymentDetails();
		PaymentPoint paymentPoint = paymentPointService.read(paymentPointStub);

		paymentDetails.setDivisionName(paymentPoint.getName(locale));
		paymentDetails.setDivisionAddress(paymentPoint.getAddress());
		paymentDetails.setObjectId(paymentPointStub.getId());
		paymentDetails.setPaymentKvartplata(documentService.getPaymentPointServiceSumm(paymentPointStub, status, SERVICE_TYPE_KVARTPLATA, beginDate, endDate));
		paymentDetails.setPaymentDogs(documentService.getPaymentPointServiceSumm(paymentPointStub, status, SERVICE_TYPE_DOGS, beginDate, endDate));
		paymentDetails.setPaymentGarage(documentService.getPaymentPointServiceSumm(paymentPointStub, status, SERVICE_TYPE_GARAGE, beginDate, endDate));
		paymentDetails.setPaymentWarming(documentService.getPaymentPointServiceSumm(paymentPointStub, status, SERVICE_TYPE_WARMING, beginDate, endDate));
		paymentDetails.setPaymentHotWater(documentService.getPaymentPointServiceSumm(paymentPointStub, status, SERVICE_TYPE_HOT_WATER, beginDate, endDate));
		paymentDetails.setPaymentColdWater(documentService.getPaymentPointServiceSumm(paymentPointStub, status, SERVICE_TYPE_COLD_WATER, beginDate, endDate));
		paymentDetails.setPaymentSewer(documentService.getPaymentPointServiceSumm(paymentPointStub, status, SERVICE_TYPE_SEWER, beginDate, endDate));
		paymentDetails.setSumm(documentService.getPaymentPointTotalSumm(paymentPointStub, status, beginDate, endDate));

		return paymentDetails;
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

	@Required
	public void setPaymentPointService(PaymentPointService paymentPointService) {
		this.paymentPointService = paymentPointService;
	}

	@Required
	public void setCashboxService(CashboxService cashboxService) {
		this.cashboxService = cashboxService;
	}
}
