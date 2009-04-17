package org.flexpay.payments.actions.quittance;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.util.SecurityUtil;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.persistence.Stub;
import org.flexpay.payments.persistence.*;
import org.flexpay.payments.persistence.quittance.QuittanceDetailsResponse;
import org.flexpay.payments.service.*;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.service.OrganizationService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

// FIXME WARN: full of hacks and other shit. By now this class is big piece of shit
public class QuittancePayAction extends FPActionSupport {

	private String actionName;

	// form data
	private QuittanceDetailsResponse.QuittanceInfo quittanceInfo = new QuittanceDetailsResponse.QuittanceInfo();
	private Map<String, BigDecimal> paymentsMap = CollectionUtils.map(); // maps serviceMasterIndex to payment value
	private String payerFio;
	private String address;

	// required services
	private DocumentTypeService documentTypeService;
	private DocumentStatusService documentStatusService;
	private OperationService operationService;
	private OperationLevelService operationLevelService;
	private OperationStatusService operationStatusService;
	private OperationTypeService operationTypeService;
	private OrganizationService organizationService;
	private SPService spService;

	@NotNull
	protected String doExecute() throws Exception {

		// FIXME I am small but very dirt fucking hack!
		Organization org = organizationService.readFull(new Stub<Organization>(1L));
		Service service = spService.read(new Stub<Service>(1L));

		// construct operations
		Operation operation = new Operation();
		BigDecimal operationSumm = new BigDecimal("0.00");

		//for (QuittanceDetailsResponse.QuittanceInfo.ServiceDetails serviceDetails : quittanceInfo.getDetailses()) {
		//	BigDecimal documentSumm = paymentsMap.get(serviceDetails.getServiceMasterIndex());

		for (String serviceMasterIndex : paymentsMap.keySet()) { // FIXME hack
			BigDecimal documentSumm = paymentsMap.get(serviceMasterIndex);
			operationSumm = operationSumm.add(documentSumm);

			Document document = new Document();
			document.setDocumentStatus(documentStatusService.read(DocumentStatus.CREATED));
			document.setDocumentType(documentTypeService.read(DocumentType.CASH_PAYMENT));
			document.setSumm(documentSumm);
			document.setAddress(address);
			document.setPayerFIO(payerFio);
			document.setDebtorOrganization(org); // TODO where to get?
			document.setDebtorId("debtorId"); // TODO where to get? account.getAccountNumber()
			document.setService(service); // TODO where to get? doc.setService(qdPayment.getQuittanceDetails().getConsumer().getService());
			document.setCreditorOrganization(org); // TODO where to get? doc.setCreditorOrganization(service.getServiceProvider().getOrganization());
			document.setCreditorId("creditorId"); // TODO where to get? doc.setCreditorId(qdPayment.getQuittanceDetails().getConsumer().getExternalAccountNumber());
			operation.addDocument(document);
		}

		operation.setAddress(address);
		operation.setPayerFIO(payerFio);
		operation.setOperationSumm(operationSumm);
		operation.setOperationInputSumm(operationSumm);
		operation.setChange(BigDecimal.ZERO);
		operation.setCreationDate(new Date());
		operation.setCreatorOrganization(org); // TODO where to get? org
		operation.setCreatorUserName(SecurityUtil.getUserName());
		operation.setOperationStatus(operationStatusService.read(OperationStatus.CREATED));
		operation.setOperationLevel(operationLevelService.read(OperationLevel.AVERAGE));
		operation.setOperationType(operationTypeService.read(OperationType.SERVICE_CASH_PAYMENT));

		// save documents and operation
		operationService.save(operation);

		return REDIRECT_SUCCESS;
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
}
