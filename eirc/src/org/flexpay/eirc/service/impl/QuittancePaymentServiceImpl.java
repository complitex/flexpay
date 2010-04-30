package org.flexpay.eirc.service.impl;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.SecurityUtil;
import org.flexpay.eirc.dao.QuittancePaymentDao;
import org.flexpay.eirc.persistence.EircAccount;
import org.flexpay.eirc.persistence.QuittanceDetailsPayment;
import org.flexpay.eirc.persistence.QuittancePacket;
import org.flexpay.eirc.persistence.QuittancePayment;
import org.flexpay.eirc.persistence.account.Quittance;
import org.flexpay.eirc.service.QuittancePaymentService;
import org.flexpay.eirc.util.config.ApplicationConfig;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.payments.persistence.*;
import org.flexpay.payments.service.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.flexpay.common.persistence.Stub.stub;

@Transactional (readOnly = true)
public class QuittancePaymentServiceImpl implements QuittancePaymentService {

	private SPService spService;
	private DocumentTypeService documentTypeService;
	private DocumentStatusService documentStatusService;
	private OperationService operationService;
	private OperationLevelService operationLevelService;
	private OperationStatusService operationStatusService;
	private OperationTypeService operationTypeService;
	private QuittancePaymentDao quittancePaymentDao;
	private OrganizationService organizationService;

	/**
	 * Get list of packets where quittance was payed
	 *
	 * @param stub Quittance stub
	 * @return List of quittance packets possibly empty
	 */
	@NotNull
	public List<QuittancePacket> getPacketsWhereQuittancePayed(@NotNull Stub<Quittance> stub) {
		return quittancePaymentDao.findQuittancePayedPackets(stub.getId());
	}

	/**
	 * Find all quittance payments
	 *
	 * @param stub Quittance stub to get quittance of
	 * @return List of registered quittance payments
	 */
	@NotNull
	public List<QuittancePayment> getQuittancePayments(@NotNull Stub<Quittance> stub) {
		return quittancePaymentDao.findQuittancePayments(stub.getId());
	}

	/**
	 * Create cash quittance payment
	 *
	 * @param payment QuittancePayement to persist
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Transactional (readOnly = false)
	public void cashPayment(@NotNull QuittancePayment payment) throws FlexPayExceptionContainer {
		validate(payment);

		quittancePaymentDao.create(payment);

		Operation operation = fromPayment(payment);
		operationService.create(operation);
	}

	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	public Operation fromPayment(QuittancePayment payment) throws FlexPayExceptionContainer {

		Organization eirc = organizationService.readFull(stub(ApplicationConfig.getSelfOrganization()));
		if (eirc == null) {
			throw new FlexPayExceptionContainer(new FlexPayException(
					"No eirc", "eirc.error.quittance.pay.no_eirc_found"));
		}

		Operation op = new Operation();
		try {
			op.setOperationSumm(payment.getAmount());
			op.setOperationInputSumm(payment.getAmount());
			op.setChange(BigDecimal.ZERO);
			op.setCreationDate(new Date());
			op.setCreatorOrganization(eirc);
			op.setCreatorUserName(SecurityUtil.getUserName());
			op.setOperationStatus(operationStatusService.read(OperationStatus.CREATED));
			op.setOperationLevel(operationLevelService.read(OperationLevel.AVERAGE));
			op.setOperationType(operationTypeService.read(OperationType.SERVICE_CASH_PAYMENT));
		} catch (FlexPayException ex) {
			throw new FlexPayExceptionContainer(ex);
		}

		EircAccount account = payment.getQuittance().getEircAccount();
		DocumentType documentType;
		try {
			documentType = documentTypeService.read(DocumentType.CASH_PAYMENT);
		} catch (FlexPayException ex) {
			throw new FlexPayExceptionContainer(new FlexPayException(
					"No doc type", "eirc.error.quittance.pay.no_doc_cash_payment"));
		}

		try {
			for (QuittanceDetailsPayment qdPayment : payment.getDetailsPayments()) {
				Document doc = new Document();
				doc.setDocumentStatus(documentStatusService.read(DocumentStatus.CREATED));
				doc.setDocumentType(documentType);
				doc.setSumm(qdPayment.getAmount());

				doc.setDebtorOrganization(eirc);
				doc.setDebtorId(account.getAccountNumber());
				doc.setService(qdPayment.getQuittanceDetails().getConsumer().getService());

				Stub<Service> stub = qdPayment.getQuittanceDetails().getConsumer().getServiceStub();
				Service service = spService.readFull(stub);
				if (service == null) {
					throw new FlexPayExceptionContainer(new FlexPayException(
							"No service " + stub, "eirc.error.quittance.pay.no_service_found", stub.getId()));
				}
				doc.setCreditorOrganization(service.getServiceProvider().getOrganization());
				doc.setCreditorId(qdPayment.getQuittanceDetails().getConsumer().getExternalAccountNumber());

				op.addDocument(doc);
			}
		} catch (FlexPayException ex) {
			throw new FlexPayExceptionContainer(ex);
		}

		return op;
	}

	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	private void validate(QuittancePayment payment) throws FlexPayExceptionContainer {

		FlexPayExceptionContainer ex = new FlexPayExceptionContainer();

		if (payment.isNotNew()) {
			ex.addException(new FlexPayException("not new", "eirc.error.quittance.pay.payment_not_new"));
		}

		BigDecimal total = BigDecimal.ZERO;
		for (QuittanceDetailsPayment qdPayment : payment.getDetailsPayments()) {
			total = total.add(qdPayment.getAmount());
		}

		if (!total.equals(payment.getAmount())) {
			ex.addException(new FlexPayException("invalid division",
					"eirc.error.quittances.pay.invalid_summ_division", payment.getAmount(), total));
		}

		if (ex.isNotEmpty()) {
			throw ex;
		}
	}

	@Required
	public void setQuittancePaymentDao(QuittancePaymentDao quittancePaymentDao) {
		this.quittancePaymentDao = quittancePaymentDao;
	}

	@Required
	public void setSpService(SPService spService) {
		this.spService = spService;
	}

	@Required
	public void setOperationService(OperationService operationService) {
		this.operationService = operationService;
	}

	@Required
	public void setDocumentTypeService(DocumentTypeService documentTypeService) {
		this.documentTypeService = documentTypeService;
	}

	@Required
	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
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
	public void setDocumentStatusService(DocumentStatusService documentStatusService) {
		this.documentStatusService = documentStatusService;
	}
}
