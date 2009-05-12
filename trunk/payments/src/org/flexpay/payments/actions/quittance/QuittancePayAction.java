package org.flexpay.payments.actions.quittance;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.util.SecurityUtil;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.payments.persistence.*;
import org.flexpay.payments.persistence.quittance.QuittanceDetailsResponse;
import org.flexpay.payments.service.*;
import org.flexpay.payments.util.ServiceFullIndexUtil;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.orgs.service.ServiceProviderService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Date;

public class QuittancePayAction extends FPActionSupport {

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

		Operation operation = buildOperation();
		for (String serviceIndx : payments.keySet()) {

			Document document = buildDocument(serviceIndx);

			if (StringUtils.isEmpty(operation.getAddress())) {
				operation.setAddress(document.getAddress());
				operation.setPayerFIO(document.getPayerFIO());
			}

			operation.addDocument(document);
		}

		return operation;
	}

	private Organization getSelfOrganization() {

		// TODO get organization id from cookies (FP-539)
		return organizationService.readFull(new Stub<Organization>(1L));
	}

	private Operation buildOperation() throws FlexPayException {

		Operation operation = new Operation();
		operation.setOperationSumm(totalToPay);
		operation.setOperationInputSumm(inputSumm);
		operation.setChange(changeSumm);
		operation.setCreationDate(new Date());
		operation.setRegisterDate(new Date());
		operation.setCreatorOrganization(getSelfOrganization());
		operation.setRegisterOrganization(getSelfOrganization());
		operation.setCreatorUserName(SecurityUtil.getUserName());
		operation.setRegisterUserName(SecurityUtil.getUserName());
		operation.setOperationStatus(operationStatusService.read(OperationStatus.REGISTERED));
		operation.setOperationLevel(operationLevelService.read(OperationLevel.AVERAGE));
		operation.setOperationType(operationTypeService.read(OperationType.SERVICE_CASH_PAYMENT));
		return operation;
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
		document.setDocumentStatus(documentStatusService.read(DocumentStatus.REGISTERED));
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
	public void setSpService(SPService spService) {
		this.spService = spService;
	}

	@Required
	public void setServiceProviderService(ServiceProviderService serviceProviderService) {
		this.serviceProviderService = serviceProviderService;
	}
}
