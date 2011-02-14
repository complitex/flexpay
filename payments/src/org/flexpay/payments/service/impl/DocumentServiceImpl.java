package org.flexpay.payments.service.impl;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.common.persistence.DateRange;
import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.payments.dao.DocumentDao;
import org.flexpay.payments.dao.DocumentDaoExt;
import org.flexpay.payments.persistence.*;
import org.flexpay.payments.service.DocumentService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Transactional (readOnly = true)
public class DocumentServiceImpl implements DocumentService {

	private Logger log = LoggerFactory.getLogger(getClass());

	private DocumentDao documentDao;
	private DocumentDaoExt documentDaoExt;

	/**
	 * Read Document object by Stub
	 *
	 * @param documentStub document stub
	 * @return Document object
	 */
	@Nullable
    @Override
	public Document read(@NotNull Stub<Document> documentStub) {
		return documentDao.readFull(documentStub.getId());
	}

	/**
	 * Save document
	 *
	 * @param document Document Object
	 */
	@Transactional (readOnly = false)
    @Override
	public void create(@NotNull Document document) {
		document.setId(null);
		documentDao.create(document);
	}

	/**
	 * Save document
	 *
	 * @param document Document Object
	 */
	@Transactional (readOnly = false)
    @Override
	public void update(@NotNull Document document) {
		documentDao.update(document);
	}

	/**
	 * Delete Document object
	 *
	 * @param documentStub document stub
	 */
	@Transactional (readOnly = false)
    @Override
	public void delete(@NotNull Stub<Document> documentStub) {
		Document document = documentDao.read(documentStub.getId());

		if (document == null) {
			log.debug("Can't find document with id {}", documentStub.getId());
			return;
		}

		documentDao.delete(document);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Document> searchDocuments(@NotNull Collection<Long> documentIds) {
		return documentDao.readFullCollection(documentIds, true);
	}

    @Override
	public List<Document> searchDocuments(@NotNull Stub<Operation> operation, @NotNull ArrayStack filters) {
		return documentDaoExt.searchDocuments(operation, filters);
	}

	/**
	 * {@inheritDoc}
	 */
    @Override
	public List<Document> listRegisteredPaymentDocuments(@NotNull Date begin, @NotNull Date end) {
		return documentDao.listRegisteredPaymentDocuments(begin, end);
	}

	/**
	 * {@inheritDoc}
	 */
    @Override
	public List<Document> listRegisteredPaymentDocuments(
			@NotNull Stub<ServiceProvider> providerStub, @NotNull Stub<Organization> orgStub, @NotNull DateRange range) {

		return documentDao.listPaymentDocumentsByServiceProvider(
				providerStub.getId(), orgStub.getId(), range, DocumentType.CASH_PAYMENT, DocumentStatus.REGISTERED);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Service> listPaymentsServices(@NotNull Stub<ServiceProvider> providerStub,
											  @NotNull Stub<Organization> orgStub, @NotNull DateRange range) {

		return documentDao.listPaymentsServicesByServiceProvider(
				providerStub.getId(), orgStub.getId(), range, DocumentType.CASH_PAYMENT, DocumentStatus.REGISTERED);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<PaymentPoint> listPaymentsPoints(@NotNull Stub<ServiceProvider> providerStub,
											@NotNull Stub<Organization> orgStub, @NotNull DateRange range) {

		return documentDao.listPaymentsPointsByServiceProvider(
				providerStub.getId(), orgStub.getId(), range, DocumentType.CASH_PAYMENT, DocumentStatus.REGISTERED);
	}

	@Override
	public BigDecimal getCashboxServiceSum(Stub<Cashbox> cashboxStub, int statusCode, int serviceTypeCode, Date beginDate, Date endDate) {

		List result = documentDao.findCashboxServiceSum(cashboxStub.getId(), statusCode, serviceTypeCode, beginDate, endDate);
		return (BigDecimal) (result.get(0) != null ? result.get(0) : new BigDecimal("0.00"));
	}

	@Override
	public BigDecimal getCashboxTotalSum(Stub<Cashbox> cashboxStub, int statusCode, Date beginDate, Date endDate) {

		List result = documentDao.findCashboxTotalSum(cashboxStub.getId(), statusCode, beginDate, endDate);
		return (BigDecimal) (result.get(0) != null ? result.get(0) : new BigDecimal("0.00"));
	}

	@Override
	public BigDecimal getPaymentPointServiceSum(Stub<PaymentPoint> paymentPointStub, int statusCode, int serviceTypeCode, Date beginDate, Date endDate) {

		List result = documentDao.findPaymentPointServiceSum(paymentPointStub.getId(), statusCode, serviceTypeCode, beginDate, endDate);
		return (BigDecimal) (result.get(0) != null ? result.get(0) : new BigDecimal("0.00"));
	}

	@Override
	public BigDecimal getPaymentPointTotalSum(Stub<PaymentPoint> paymentPointStub, int statusCode, Date beginDate, Date endDate) {

		List result = documentDao.findPaymentPointTotalSum(paymentPointStub.getId(), statusCode, beginDate, endDate);
		return (BigDecimal) (result.get(0) != null ? result.get(0) : new BigDecimal("0.00"));
	}

	@Override
	public BigDecimal getOperationServiceSum(Stub<Operation> operationStub, int serviceTypeCode) {

		List result = documentDao.findOperationServiceSum(operationStub.getId(), serviceTypeCode);
		return (BigDecimal) (result.isEmpty() ? new BigDecimal("0.00") : result.get(0));
	}

	@Required
	public void setDocumentDao(DocumentDao documentDao) {
		this.documentDao = documentDao;
	}

	@Required
	public void setDocumentDaoExt(DocumentDaoExt documentDaoExt) {
		this.documentDaoExt = documentDaoExt;
	}
}
