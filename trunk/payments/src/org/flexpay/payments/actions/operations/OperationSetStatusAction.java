package org.flexpay.payments.actions.operations;

import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.payments.actions.OperatorAWPActionSupport;
import org.flexpay.payments.persistence.Document;
import org.flexpay.payments.persistence.DocumentStatus;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.persistence.OperationStatus;
import org.flexpay.payments.service.DocumentService;
import org.flexpay.payments.service.DocumentStatusService;
import org.flexpay.payments.service.OperationService;
import org.flexpay.payments.service.OperationStatusService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.DateUtil.now;
import static org.flexpay.common.util.SecurityUtil.getUserName;
import static org.flexpay.payments.persistence.OperationStatus.REGISTERED;

public class OperationSetStatusAction extends OperatorAWPActionSupport {

	private Operation operation = new Operation();
	private Integer status;

	private OperationService operationService;
	private OperationStatusService operationStatusService;
	private DocumentService documentService;
	private DocumentStatusService documentStatusService;
	private OrganizationService organizationService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		if (operation == null || operation.isNew()) {
			log.warn("Incorrect operation id {}", operation);
			return SUCCESS;
		}

		if (status == null || status <= 0 || status > 6) {
			log.warn("Incorrect status code {}", status);
			return SUCCESS;
		}

		OperationStatus operationStatus = operationStatusService.read(status);
		Stub<Operation> stub = stub(operation);
		operation = operationService.read(stub);
		if (operation == null) {
			log.warn("Can't get operation with id {} from DB", stub.getId());
			return SUCCESS;
		}

		operation.setOperationStatus(operationStatus);

		if (operationStatus.getCode() == REGISTERED) {
			operation.setRegisterDate(now());
			operation.setRegisterOrganization(getSelfOrganization());
			operation.setRegisterUserName(getUserName());
		}

		// setting documents status
        DocumentStatus documentStatus = documentStatusService.read(status);
		for (Document document : operation.getDocuments()) {
			document.setDocumentStatus(documentStatus);
			if (document.isNew()) {
				documentService.create(document);
			} else {
				documentService.update(document);
			}
		}

		operationService.update(operation);

		return SUCCESS;
	}

	private Organization getSelfOrganization() {

		Cashbox cashbox = cashboxService.read(new Stub<Cashbox>(getCashboxId()));
		if (cashbox == null) {
			throw new IllegalArgumentException("Invalid cashbox id: " + getCashboxId());
		}
		return organizationService.readFull(stub(cashbox.getPaymentPoint().getCollector().getOrganization()));
	}

	@NotNull
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Required
	public void setOperationService(OperationService operationService) {
		this.operationService = operationService;
	}

	@Required
	public void setOperationStatusService(OperationStatusService operationStatusService) {
		this.operationStatusService = operationStatusService;
	}

	@Required
	public void setDocumentService(DocumentService documentService) {
		this.documentService = documentService;
	}

	@Required
	public void setDocumentStatusService(DocumentStatusService documentStatusService) {
		this.documentStatusService = documentStatusService;
	}

	@Required
	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}
}
