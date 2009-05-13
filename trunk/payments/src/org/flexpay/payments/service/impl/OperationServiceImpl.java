package org.flexpay.payments.service.impl;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.payments.dao.OperationDao;
import org.flexpay.payments.dao.OperationDaoExt;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.service.OperationService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Date;
import java.math.BigDecimal;

@Transactional (readOnly = true)
public class OperationServiceImpl implements OperationService {

	private Logger log = LoggerFactory.getLogger(getClass());

	private OperationDao operationDao;
	private OperationDaoExt operationDaoExt;

	/**
	 * Read Operation object by Stub
	 *
	 * @param operationStub operation stub
	 * @return Operation object
	 */
	@Nullable
	public Operation read(@NotNull Stub<Operation> operationStub) {
		return operationDao.readFull(operationStub.getId());
	}

	/**
	 * Save operation
	 *
	 * @param operation Operation Object
	 */
	@Transactional (readOnly = false)
	public void save(@NotNull Operation operation) {
		if (operation.isNew()) {
			operation.setId(null);
			operationDao.create(operation);
		} else {
			operationDao.update(operation);
		}
	}

	/**
	 * Delete Operation object
	 *
	 * @param operationStub operation stub
	 */
	@Transactional (readOnly = false)
	public void delete(@NotNull Stub<Operation> operationStub) {
		Operation operation = operationDao.read(operationStub.getId());

		if (operation == null) {
			log.debug("Can't find operation with id {}", operationStub.getId());
			return;
		}

		operationDao.delete(operation);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Operation> listPaymentOperations(Date beginDate, Date endDate, Page<Operation> pager) {
		
		return operationDao.listPaymentOperations(beginDate, endDate, pager);
	}

	public List<Operation> listReceivedPayments(Long organizationId, Date beginDate, Date endDate) {
		return operationDao.listReceivedPayments(organizationId, beginDate, endDate);
	}

	@Required
	public void setOperationDao(OperationDao operationDao) {
		this.operationDao = operationDao;
	}

	@Required
	public void setOperationDaoExt(OperationDaoExt operationDaoExt) {
		this.operationDaoExt = operationDaoExt;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Operation> searchDocuments(Long serviceTypeId, Date begin, Date end, BigDecimal minimalSumm, BigDecimal maximalSumm, Page<Operation> pager) {

		return operationDaoExt.searchDocuments(serviceTypeId, begin, end, minimalSumm, maximalSumm, pager);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Operation> searchOperations(Date begin, Date end, BigDecimal minimalSumm, BigDecimal maximalSumm, Page<Operation> pager) {

		return operationDaoExt.searchOperations(begin, end, minimalSumm, maximalSumm, pager);
	}
}
