package org.flexpay.payments.service.impl;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.service.CashboxService;
import org.flexpay.payments.dao.OperationDao;
import org.flexpay.payments.dao.OperationDaoExt;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.persistence.OperationLevel;
import org.flexpay.payments.persistence.OperationStatus;
import org.flexpay.payments.persistence.OperationType;
import org.flexpay.payments.persistence.operation.sorter.OperationSorter;
import org.flexpay.payments.service.OperationLevelService;
import org.flexpay.payments.service.OperationService;
import org.flexpay.payments.service.OperationStatusService;
import org.flexpay.payments.service.OperationTypeService;
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
public class OperationServiceImpl implements OperationService {

	private Logger log = LoggerFactory.getLogger(getClass());

	private OperationStatusService operationStatusService;
	private OperationLevelService operationLevelService;
	private OperationTypeService operationTypeService;
	private CashboxService cashboxService;

	private OperationDao operationDao;
	private OperationDaoExt operationDaoExt;

	/**
	 * Read Operation object by Stub
	 *
	 * @param operationStub operation stub
	 * @return Operation object
	 */
	@Nullable
    @Override
	public Operation read(@NotNull Stub<Operation> operationStub) {
		return operationDao.readFull(operationStub.getId());
	}

    /**
     * Read operations collection by theirs ids
     *
     * @param operationIds Apartment ids
     * @param preserveOrder Whether to preserve order of objects
     * @return Found operations
     */
    @NotNull
    @Override
    public List<Operation> readFull(@NotNull Collection<Long> operationIds, boolean preserveOrder) {
        return operationDao.readFullCollection(operationIds, preserveOrder);
    }

    /**
	 * Save operation
	 *
	 * @param operation Operation Object
	 */
	@Transactional (readOnly = false)
    @Override
	public void create(@NotNull Operation operation) {
		operation.setId(null);
		operationDao.create(operation);
	}

	/**
	 * Save operation
	 *
	 * @param operation Operation Object
	 */
	@Transactional (readOnly = false)
    @Override
	public void update(@NotNull Operation operation) {
		operationDao.update(operation);
	}

	/**
	 * Delete Operation object
	 *
	 * @param operationStub operation stub
	 */
	@Transactional (readOnly = false)
    @Override
	public void delete(@NotNull Stub<Operation> operationStub) {
		Operation operation = operationDao.read(operationStub.getId());

		if (operation == null) {
			log.debug("Can't find operation with id {}", operationStub.getId());
			return;
		}

		operationDao.delete(operation);
	}

    @Override
	public List<Operation> listPaymentOperations(Date beginDate, Date endDate, Page<Operation> pager) {
		return operationDao.listPaymentOperations(beginDate, endDate, pager);
	}

    @Override
	public List<Operation> listPaymentOperations(Date beginDate, Date endDate) {
		return operationDao.listPaymentOperations(beginDate, endDate);
	}

    @Override
	public List<Operation> listLastPaymentOperations(Date beginDate, Date endDate) {
		return operationDao.listLastPaymentOperations(beginDate, endDate);
	}

    @Override
    public Operation getLastPaymentOperationForPaymentPoint(Stub<PaymentPoint> paymentPoint, Date beginDate, Date endDate) {
		return operationDaoExt.getLastPaymentPointPaymentOperation(paymentPoint.getId(), beginDate, endDate);
	}

    @Override
	public Operation getLastPaymentOperationsForCashbox(Stub<Cashbox> cashbox, Date beginDate, Date endDate) {
		return operationDaoExt.getLastCashboxPaymentOperation(cashbox.getId(), beginDate, endDate);
	}

    @Override
	public List<Operation> listReceivedPaymentsForCashbox(Stub<Cashbox> cashbox, Date beginDate, Date endDate) {
		return operationDao.listPayments(cashbox.getId(), beginDate, endDate, OperationStatus.REGISTERED);
	}

    @Override
	public List<Operation> listReturnedPaymentsForCashbox(Stub<Cashbox> cashbox, Date beginDate, Date endDate) {
		return operationDao.listPayments(cashbox.getId(), beginDate, endDate, OperationStatus.RETURNED);
	}

	@Override
	public List<Operation> listReceivedPaymentsForOperator(Stub<Cashbox> cashbox, Date beginDate, Date endDate, String registerUserName) {

		return operationDao.listPaymentsForOperator(cashbox.getId(), beginDate, endDate, OperationStatus.REGISTERED, registerUserName);
	}

	@Override
	public List<Operation> listReturnedPaymentsForOperator(Stub<Cashbox> cashbox, Date beginDate, Date endDate, String registerUserName) {

		return operationDao.listPaymentsForOperator(cashbox.getId(), beginDate, endDate, OperationStatus.RETURNED, registerUserName);
	}

    @Override
	public List<Operation> listReceivedPaymentsForPaymentCollector(Stub<PaymentCollector> stub, Date beginDate, Date endDate) {
		return operationDao.listPaymentsByPaymentCollector(stub.getId(), beginDate, endDate, OperationStatus.REGISTERED);
	}

    @Override
	public List<Operation> listReceivedPaymentsForOrganization(Stub<Organization> organization, Date beginDate, Date endDate) {
		return operationDao.listPaymentsByOrganization(organization.getId(), beginDate, endDate, OperationStatus.REGISTERED);
	}

    @Override
	public List<Operation> searchDocuments(OperationSorter operationSorter, Stub<Cashbox> cashbox, Long serviceTypeId, Date begin,
										   Date end, BigDecimal minimalSum, BigDecimal maximalSum, Page<Operation> pager) {
		return operationDaoExt.searchDocuments(operationSorter, cashbox, serviceTypeId, begin, end, minimalSum, maximalSum, pager);
	}

    @Override
	public List<Operation> searchOperations(OperationSorter operationSorter, Long tradingDayProcessId, Stub<Cashbox> cashbox, Date begin, Date end, BigDecimal minimalSum,
											BigDecimal maximalSum, Page<Operation> pager) {
		return operationDaoExt.searchOperations(operationSorter, tradingDayProcessId, cashbox, begin, end, minimalSum, maximalSum, pager);
	}

	/**
	 * Creates new operation with no data and BLANK state
	 *
	 * @return new operation instance
	 */
    @Transactional (readOnly = false)
    @Override
	public Operation createBlankOperation(String creator, Stub<Cashbox> cashboxStub) throws FlexPayException {

        log.debug("Creating blank operation for creator = {} and cashboxStub = {}", creator, cashboxStub);

		Cashbox cashbox = cashboxService.read(cashboxStub);
		if (cashbox == null) {
            log.error("Invalid cashbox id {}", cashboxStub.getId());
			throw new FlexPayException("Invalid cashbox id: " + cashboxStub.getId());
		}

		PaymentPoint paymentPoint = cashbox.getPaymentPoint();
		Organization creatorOrganization = cashbox.getPaymentPoint().getCollector().getOrganization();

		Operation operation = new Operation();
		operation.setOperationStatus(operationStatusService.read(OperationStatus.BLANK));
		operation.setOperationType(operationTypeService.read(OperationType.SERVICE_CASH_PAYMENT));
		operation.setOperationLevel(operationLevelService.read(OperationLevel.AVERAGE));
		operation.setCreationDate(new Date());
		operation.setOperationSum(new BigDecimal("0.00"));
		operation.setCreatorUserName(creator);
		operation.setCreatorOrganization(creatorOrganization);
		operation.setPaymentPoint(paymentPoint);
		operation.setCashbox(cashbox);

        log.debug("Creating operation {}", operation);

		operationDao.create(operation);

		return operation;
	}

	@Transactional (readOnly = true)
    @Override
	public Long getBlankOperationsCount() throws FlexPayException {
		return operationDaoExt.getBlankOperationsCount();
	}

	@Transactional (readOnly = false)
    @Override
	public void deleteAllBlankOperations() throws FlexPayException {
		operationDaoExt.deleteAllBlankOperations();
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

	@Required
	public void setCashboxService(CashboxService cashboxService) {
		this.cashboxService = cashboxService;
	}
}
