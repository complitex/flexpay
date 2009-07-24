package org.flexpay.payments.service.impl;

import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.payments.dao.DocumentDao;
import org.flexpay.payments.dao.DocumentDaoExt;
import org.flexpay.payments.persistence.Document;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.service.DocumentService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
	public Document read(@NotNull Stub<Document> documentStub) {
		return documentDao.readFull(documentStub.getId());
	}

	/**
	 * Save document
	 *
	 * @param document Document Object
	 */
	@Transactional (readOnly = false)
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
	public void update(@NotNull Document document) {
		documentDao.update(document);
	}

	/**
	 * Delete Document object
	 *
	 * @param documentStub document stub
	 */
	@Transactional (readOnly = false)
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
	public List<Document> searchDocuments(@NotNull Operation operation, Long serviceTypeId, BigDecimal minimalSumm, BigDecimal maximalSumm) {
		return documentDaoExt.searchDocuments(operation, serviceTypeId, minimalSumm, maximalSumm);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Document> listRegisteredPaymentDocuments(@NotNull Date begin, @NotNull Date end) {
		return documentDao.listRegisteredPaymentDocuments(begin, end);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Document> listRegisteredPaymentDocuments(@NotNull ServiceProvider serviceProvider, @NotNull Organization organization, @NotNull Date begin, @NotNull Date end) {
		return documentDao.listRegisteredPaymentDocumentsByServiceProvider(serviceProvider.getId(), organization.getId(), begin, end);
	}

	@Override
	public BigDecimal getCashboxServiceSumm(Stub<Cashbox> cashboxStub, int statusCode, int serviceTypeCode, Date beginDate, Date endDate) {

		List result = documentDao.findCashboxServiceSumm(cashboxStub.getId(), statusCode, serviceTypeCode, beginDate, endDate);
		return (BigDecimal) (result.get(0) != null ? result.get(0) : new BigDecimal("0.00"));
	}

	@Override
	public BigDecimal getCashboxTotalSumm(Stub<Cashbox> cashboxStub, int statusCode, Date beginDate, Date endDate) {

		List result = documentDao.findCashboxTotalSumm(cashboxStub.getId(), statusCode, beginDate, endDate);
		return (BigDecimal) (result.get(0) != null ? result.get(0) : new BigDecimal("0.00"));
	}

	@Override
	public BigDecimal getPaymentPointServiceSumm(Stub<PaymentPoint> paymentPointStub, int statusCode, int serviceTypeCode, Date beginDate, Date endDate) {

		List result = documentDao.findPaymentPointServiceSumm(paymentPointStub.getId(), statusCode, serviceTypeCode, beginDate, endDate);
		return (BigDecimal) (result.get(0) != null ? result.get(0) : new BigDecimal("0.00"));
	}

	@Override
	public BigDecimal getPaymentPointTotalSumm(Stub<PaymentPoint> paymentPointStub, int statusCode, Date beginDate, Date endDate) {

		List result = documentDao.findPaymentPointTotalSumm(paymentPointStub.getId(), statusCode, beginDate, endDate);
		return (BigDecimal) (result.get(0) != null ? result.get(0) : new BigDecimal("0.00"));
	}

	@Override
	public BigDecimal getOperationServiceSumm(Stub<Operation> operationStub, int serviceTypeCode) {

		List result = documentDao.findOperationServiceSumm(operationStub.getId(), serviceTypeCode);
		return (BigDecimal) (result.size() > 0 ? result.get(0) : new BigDecimal("0.00"));
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
