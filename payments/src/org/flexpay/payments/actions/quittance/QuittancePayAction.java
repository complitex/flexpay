package org.flexpay.payments.actions.quittance;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.SecurityUtil;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.orgs.service.ServiceProviderService;
import org.flexpay.payments.actions.PaymentPointAwareAction;
import org.flexpay.payments.persistence.*;
import org.flexpay.payments.persistence.quittance.QuittanceDetailsResponse;
import org.flexpay.payments.service.*;
import org.flexpay.payments.util.ServiceFullIndexUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public class QuittancePayAction extends FPActionSupport implements PaymentPointAwareAction {

	private String actionName;

	// form data
	private QuittanceDetailsResponse.QuittanceInfo quittanceInfo = new QuittanceDetailsResponse.QuittanceInfo();
	private Map<String, BigDecimal> payments = CollectionUtils.map();
	private Map<String, String> serviceProviderAccounts = CollectionUtils.map();
	private Map<String, String> payerFios = CollectionUtils.map();
	private Map<String, String> addresses = CollectionUtils.map();
	private Map<String, String> eircAccounts = CollectionUtils.map();
	private BigDecimal changeSumm;
	private BigDecimal inputSumm;
	private BigDecimal totalToPay;

	// required services
	private DocumentTypeService documentTypeService;
	private DocumentStatusService documentStatusService;
	private OperationService operationService;
	private OperationLevelService operationLevelService;
	private OperationStatusService operationStatusService;
	private OperationTypeService operationTypeService;
	private PaymentPointService paymentPointService;
	private OrganizationService organizationService;
	private SPService spService;
	private ServiceProviderService serviceProviderService;

	private Long paymentPointId;

	private Operation operation = new Operation();

	@NotNull
	protected String doExecute() throws Exception {

		operation = createOperation();
		operationService.save(operation);
		return REDIRECT_SUCCESS;
	}

	private Operation createOperation() throws FlexPayException {

		Operation op = buildOperation();
		for (String serviceIndx : payments.keySet()) {

			Document document = buildDocument(serviceIndx);

			if (StringUtils.isEmpty(op.getAddress())) {
				op.setAddress(document.getAddress());
				op.setPayerFIO(document.getPayerFIO());
			}

			if (document.getSumm().compareTo(BigDecimal.ZERO) > 0) {
				op.addDocument(document);
			}
		}

		return op;
	}

	private PaymentPoint getPaymentPoint() {
		return paymentPointService.read(new Stub<PaymentPoint>(paymentPointId));
	}

	private Organization getSelfOrganization() {

		PaymentPoint paymentPoint = getPaymentPoint();
		if (paymentPoint == null) {
			throw new IllegalStateException("Invalid payment point id: " + paymentPointId);
		}
		Long organizationId = paymentPoint.getCollector().getOrganization().getId();
		return organizationService.readFull(new Stub<Organization>(organizationId));
	}

	private Operation buildOperation() throws FlexPayException {

		Operation op = new Operation();
		op.setOperationSumm(totalToPay);
		op.setOperationInputSumm(inputSumm);
		op.setChange(changeSumm);
		op.setCreationDate(new Date());
		op.setRegisterDate(new Date());
		op.setCreatorOrganization(getSelfOrganization());
		op.setPaymentPoint(getPaymentPoint());
		op.setRegisterOrganization(getSelfOrganization());
		op.setCreatorUserName(SecurityUtil.getUserName());
		op.setRegisterUserName(SecurityUtil.getUserName());
		op.setOperationStatus(operationStatusService.read(OperationStatus.CREATED));
		op.setOperationLevel(operationLevelService.read(OperationLevel.AVERAGE));
		op.setOperationType(operationTypeService.read(OperationType.SERVICE_CASH_PAYMENT));
		return op;
	}

	private Document buildDocument(String serviceFullIndx) throws FlexPayException {

		BigDecimal documentSumm = payments.get(serviceFullIndx);
		String serviceId = getServiceIdFromIndex(serviceFullIndx);
		Service service = spService.read(new Stub<Service>(Long.parseLong(serviceId)));
		ServiceProvider serviceProvider = serviceProviderService.read(new Stub<ServiceProvider>(service.getServiceProvider().getId()));
		Organization serviceProviderOrganization = serviceProvider.getOrganization();

		String serviceProviderAccount = this.serviceProviderAccounts.get(serviceFullIndx);

		Document document = new Document();
		document.setService(service);
		document.setDocumentStatus(documentStatusService.read(DocumentStatus.CREATED));
		document.setDocumentType(documentTypeService.read(DocumentType.CASH_PAYMENT));
		document.setSumm(documentSumm);
		document.setAddress(addresses.get(serviceFullIndx));
		document.setPayerFIO(payerFios.get(serviceFullIndx));
		document.setDebtorOrganization(getSelfOrganization());
		document.setDebtorId(eircAccounts.get(serviceFullIndx));
		document.setCreditorOrganization(serviceProviderOrganization);
		document.setCreditorId(serviceProviderAccount);
		return document;
	}

	private String getServiceIdFromIndex(String serviceFullIndex) {

		return ServiceFullIndexUtil.getServiceIdFromIndex(serviceFullIndex);
	}

	@NotNull
	protected String getErrorResult() {
		return SUCCESS;
	}

	// form data
	public QuittanceDetailsResponse.QuittanceInfo getQuittanceInfo() {
		return quittanceInfo;
	}

	public void setQuittanceInfo(QuittanceDetailsResponse.QuittanceInfo quittanceInfo) {
		this.quittanceInfo = quittanceInfo;
	}

	public Map<String, BigDecimal> getPayments() {
		return payments;
	}

	public void setPayments(Map<String, BigDecimal> payments) {
		this.payments = payments;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public BigDecimal getChangeSumm() {
		return changeSumm;
	}

	public void setChangeSumm(BigDecimal changeSumm) {
		this.changeSumm = changeSumm;
	}

	public BigDecimal getInputSumm() {
		return inputSumm;
	}

	public void setInputSumm(BigDecimal inputSumm) {
		this.inputSumm = inputSumm;
	}

	public void setTotalToPay(BigDecimal totalToPay) {
		this.totalToPay = totalToPay;
	}

	public Map<String, String> getServiceProviderAccounts() {
		return serviceProviderAccounts;
	}

	public void setServiceProviderAccounts(Map<String, String> serviceProviderAccounts) {
		this.serviceProviderAccounts = serviceProviderAccounts;
	}

	public Map<String, String> getPayerFios() {
		return payerFios;
	}

	public void setPayerFios(Map<String, String> payerFios) {
		this.payerFios = payerFios;
	}

	public Map<String, String> getAddresses() {
		return addresses;
	}

	public void setAddresses(Map<String, String> addresses) {
		this.addresses = addresses;
	}

	public Map<String, String> getEircAccounts() {
		return eircAccounts;
	}

	public void setEircAccounts(Map<String, String> eircAccounts) {
		this.eircAccounts = eircAccounts;
	}

	// required services
	@Required
	public void setDocumentTypeService(DocumentTypeService documentTypeService) {
		this.documentTypeService = documentTypeService;
	}

	@Required
	public void setDocumentStatusService(DocumentStatusService documentStatusService) {
		this.documentStatusService = documentStatusService;
	}

	@Required
	public void setOperationService(OperationService operationService) {
		this.operationService = operationService;
	}

	@Required
	public void setOperationLevelService(OperationLevelService operationLevelService) {
		this.operationLevelService = operationLevelService;
	}

	@Required
	public void setOperationStatusService(OperationStatusService operationStatusService) {
		this.operationStatusService = operationStatusService;
	}

	@Required
	public void setOperationTypeService(OperationTypeService operationTypeService) {
		this.operationTypeService = operationTypeService;
	}

	@Required
	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}

	@Required
	public void setPaymentPointService(PaymentPointService paymentPointService) {
		this.paymentPointService = paymentPointService;
	}

	@Required
	public void setSpService(SPService spService) {
		this.spService = spService;
	}

	@Required
	public void setServiceProviderService(ServiceProviderService serviceProviderService) {
		this.serviceProviderService = serviceProviderService;
	}

	public Long getPaymentPointId() {
		return paymentPointId;
	}

	public void setPaymentPointId(Long paymentPointId) {
		this.paymentPointId = paymentPointId;
	}

	public Operation getOperation() {
		return operation;
	}
}
