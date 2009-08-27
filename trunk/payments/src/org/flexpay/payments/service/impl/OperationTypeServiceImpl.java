package org.flexpay.payments.service.impl;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.payments.dao.OperationTypeDao;
import org.flexpay.payments.persistence.OperationType;
import org.flexpay.payments.service.OperationTypeService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Transactional (readOnly = true)
public class OperationTypeServiceImpl implements OperationTypeService {

	private Logger log = LoggerFactory.getLogger(getClass());
	private OperationTypeDao operationTypeDao;

	/**
	 * Read full operation type details
	 *
	 * @param stub Operation type stub
	 * @return OperationType if found, or <code>null</code> otherwise
	 */
	public OperationType read(@NotNull Stub<OperationType> stub) {
		log.debug("Reading {}", stub);
		return operationTypeDao.readFull(stub.getId());
	}

	/**
	 * Read full operation type details by code
	 *
	 * @param code Operation type code
	 * @return OperationType if found, or <code>null</code> otherwise
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if lookup by code fails
	 */
	@NotNull
	public OperationType read(int code) throws FlexPayException {

		log.debug("Reading {}", code);
		List<OperationType> types = operationTypeDao.findByCode(code);
		if (types.isEmpty()) {
			throw new FlexPayException("Type not found #" + code);
		}
		return types.get(0);
	}

	@Required
	public void setOperationTypeDao(OperationTypeDao operationTypeDao) {
		this.operationTypeDao = operationTypeDao;
	}
}
