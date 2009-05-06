package org.flexpay.payments.actions.quittance;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.util.SecurityUtil;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.payments.persistence.*;
import org.flexpay.payments.persistence.quittance.QuittanceDetailsResponse;
import org.flexpay.payments.service.*;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.orgs.service.ServiceProviderService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;


public class QuittancePayAction extends FPActionSupport {

	private String actionName;

	// form data
	private QuittanceDetailsResponse.QuittanceInfo quittanceInfo = new QuittanceDetailsResponse.QuittanceInfo();
	private Map<String, BigDecimal> paymentsMap = CollectionUtils.map();
	private Map<String, String> serviceProviderAccountsMap = CollectionUtils.map();
	private String payerFio;
	private String address;
	private String eircAccount;
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
	private OrganizationService organizationService;
	private SPService spService;
	private ServiceProviderService serviceProviderService;

	@NotNull
	protected String doExecute() throws Exception {

		Operation operation = createOperation();
		operationService.save(operation);
		return REDIRECT_SUCCESS;
	}

	private Operation createOperation() throws FlexPayException {


		Organization creatorOrganization = getCreatorOrganization();

		Operation operation = buildOperation(creatorOrganization);
		for (String serviceId : paymentsMap.keySet()) {
			Document document = buildDocument(creatorOrganization, serviceId);
			operation.addDocument(document);
		}

		return operation;

	}

	private Organization getCreatorOrganization() {

		// TODO get organization id from cookies (FP-539)
		Organization creatorOrganization = organizationService.readFull(new Stub<Organization>(1L));
		return creatorOrganization;
	}

	private Operation buildOperation(Organization creatorOrganization) throws FlexPayException {
		Operation operation = new Operation();
		operation.setAddress(address);
		operation.setPayerFIO(payerFio);
		operation.setOperationSumm(totalToPay);
		operation.setOperationInputSumm(inputSumm);
		operation.setChange(changeSumm);
		operation.setCreationDate(new Date());
		operation.setCreatorOrganization(creatorOrganization);
		operation.setCreatorUserName(SecurityUtil.getUserName());
		operation.setOperationStatus(operationStatusService.read(OperationStatus.REGISTERED));
		operation.setOperationLevel(operationLevelService.read(OperationLevel.AVERAGE));
		operation.setOperationType(operationTypeService.read(OperationType.SERVICE_CASH_PAYMENT));
		return operation;
	}

	private Document buildDocument(Organization creatorOrganization, String serviceId) throws FlexPayException {
		BigDecimal documentSumm = paymentsMap.get(serviceId);

		Service service = spService.read(new Stub<Service>(Long.parseLong(serviceId)));
		ServiceProvider serviceProvider = serviceProviderService.read(new Stub<ServiceProvider>(service.getServiceProvider().getId()));
		Organization serviceProviderOrganization = serviceProvider.getOrganization();

		String serviceProviderAccount = serviceProviderAccountsMap.get(serviceId);

		Document document = new Document();
		document.setService(service);
		document.setDocumentStatus(documentStatusService.read(DocumentStatus.REGISTERED));
		document.setDocumentType(documentTypeService.read(DocumentType.CASH_PAYMENT));
		document.setSumm(documentSumm);
		document.setAddress(address);
		document.setPayerFIO(payerFio);
		document.setDebtorOrganization(creatorOrganization);
		document.setDebtorId(eircAccount);
		document.setCreditorOrganization(serviceProviderOrganization);
		document.setCreditorId(serviceProviderAccount);
		return document;
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

	public Map<String, BigDecimal> getPaymentsMap() {
		return paymentsMap;
	}

	public void setPaymentsMap(Map<String, BigDecimal> paymentsMap) {
		this.paymentsMap = paymentsMap;
	}

	public String getPayerFio() {
		return payerFio;
	}

	public void setPayerFio(String payerFio) {
		this.payerFio = payerFio;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public void setEircAccount(String eircAccount) {
		this.eircAccount = eircAccount;
	}

	public Map<String, String> getServiceProviderAccountsMap() {
		return serviceProviderAccountsMap;
	}

	public void setServiceProviderAccountsMap(Map<String, String> serviceProviderAccountsMap) {
		this.serviceProviderAccountsMap = serviceProviderAccountsMap;
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
	public void setSpService(SPService spService) {
		this.spService = spService;
	}

	@Required
	public void setServiceProviderService(ServiceProviderService serviceProviderService) {
		this.serviceProviderService = serviceProviderService;
	}
}
