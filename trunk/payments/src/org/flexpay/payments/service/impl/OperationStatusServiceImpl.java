package org.flexpay.payments.service.impl;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.payments.dao.OperationStatusDao;
import org.flexpay.payments.persistence.OperationStatus;
import org.flexpay.payments.service.OperationStatusService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional (readOnly = true)
public class OperationStatusServiceImpl implements OperationStatusService {

	private Logger log = LoggerFactory.getLogger(getClass());
	private OperationStatusDao operationStatusDao;

	/**
	 * Read full operation status details
	 *
	 * @param stub Operation status stub
	 * @return OperationStatus if found, or <code>null</code> otherwise
	 */
	public OperationStatus read(@NotNull Stub<OperationStatus> stub) {
		log.debug("Reading {}", stub);
		return operationStatusDao.readFull(stub.getId());
	}

	/**
	 * Read full operation status details by code
	 *
	 * @param code Operation status code
	 * @return OperationStatus if found, or <code>null</code> otherwise
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if lookup by code fails
	 */
	@NotNull
	public OperationStatus read(int code) throws FlexPayException {

		log.debug("Reading {}", code);
		List<OperationStatus> statuses = operationStatusDao.findByCode(code);
		if (statuses.isEmpty()) {
			throw new FlexPayException("Status not found #" + code);
		}
		return statuses.get(0);
	}

	@Required
	public void setOperationStatusDao(OperationStatusDao operationStatusDao) {
		this.operationStatusDao = operationStatusDao;
	}
}
