package org.flexpay.payments.service.impl;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.payments.dao.OperationDao;
import org.flexpay.payments.dao.OperationDaoExt;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.persistence.OperationStatus;
import org.flexpay.payments.persistence.OperationLevel;
import org.flexpay.payments.persistence.OperationType;
import org.flexpay.payments.service.OperationService;
import org.flexpay.payments.service.OperationStatusService;
import org.flexpay.payments.service.OperationLevelService;
import org.flexpay.payments.service.OperationTypeService;
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
public class OperationServiceImpl implements OperationService {

	private Logger log = LoggerFactory.getLogger(getClass());

	private OperationStatusService operationStatusService;
	private OperationLevelService operationLevelService;
	private OperationTypeService operationTypeService;

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

	public List<Operation> listPaymentOperations(Date beginDate, Date endDate, Page<Operation> pager) {
		return operationDao.listPaymentOperations(beginDate, endDate, pager);
	}

	public List<Operation> listPaymentOperations(Date beginDate, Date endDate) {
		return operationDao.listPaymentOperations(beginDate, endDate);
	}

    public List<Operation> listLastPaymentOperations(Date beginDate, Date endDate) {
        return operationDao.listLastPaymentOperations(beginDate, endDate);
    }

    public List<Operation> listLastPaymentOperations(PaymentPoint paymentPoint, Date beginDate, Date endDate) {
        return operationDao.listLastPaymentPointPaymentOperations(paymentPoint.getId(), beginDate, endDate);
    }

    public List<Operation> listLastPaymentOperations(Cashbox cashbox, Date beginDate, Date endDate) {
        return operationDao.listLastCashboxPaymentOperations(cashbox.getId(), beginDate, endDate);
    }

	public List<Operation> listReceivedPayments(Cashbox cashbox, Date beginDate, Date endDate) {
		return operationDao.listPayments(cashbox.getId(), beginDate, endDate, OperationStatus.REGISTERED);
	}

	public List<Operation> listReceivedPayments(PaymentPoint paymentPoint, Date beginDate, Date endDate) {
		return operationDao.listPaymentsByPaymentPoint(paymentPoint.getId(), beginDate, endDate, OperationStatus.REGISTERED);
	}

	public List<Operation> listReceivedPayments(Organization organization, Date beginDate, Date endDate) {
		return operationDao.listPaymentsByOrganization(organization.getId(), beginDate, endDate, OperationStatus.REGISTERED);
	}

	public List<Operation> listReturnedPayments(Cashbox cashbox, Date beginDate, Date endDate) {
		return operationDao.listPayments(cashbox.getId(), beginDate, endDate, OperationStatus.RETURNED);
	}

	public List<Operation> searchDocuments(Cashbox cashbox, Long serviceTypeId, Date begin,
										   Date end, BigDecimal minimalSumm, BigDecimal maximalSumm, Page<Operation> pager) {
		return operationDaoExt.searchDocuments(cashbox, serviceTypeId, begin, end, minimalSumm, maximalSumm, pager);
	}

	public List<Operation> searchOperations(Cashbox cashbox, Date begin, Date end, BigDecimal minimalSumm,
											BigDecimal maximalSumm, Page<Operation> pager) {
		return operationDaoExt.searchOperations(cashbox, begin, end, minimalSumm, maximalSumm, pager);
	}

	@Transactional (readOnly = false)
	public Operation createBlankOperation() throws FlexPayException {

		Operation operation = new Operation();
		operation.setOperationStatus(operationStatusService.read(OperationStatus.BLANK));
		operation.setOperationType(operationTypeService.read(OperationType.SERVICE_CASH_PAYMENT));
		operation.setOperationLevel(operationLevelService.read(OperationLevel.AVERAGE));
		operationDao.create(operation);

		return operation;
	}

	@Required
	public void setOperationDao(OperationDao operationDao) {
		this.operationDao = operationDao;
	}

	@Required
	public void setOperationDaoExt(OperationDaoExt operationDaoExt) {
		this.operationDaoExt = operationDaoExt;
	}

	@Required
	public void setOperationStatusService(OperationStatusService operationStatusService) {
		this.operationStatusService = operationStatusService;
	}

	@Required
	public void setOperationLevelService(OperationLevelService operationLevelService) {
		this.operationLevelService = operationLevelService;
	}

	@Required
	public void setOperationTypeService(OperationTypeService operationTypeService) {
		this.operationTypeService = operationTypeService;
	}
}
