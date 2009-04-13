package org.flexpay.payments.service.impl;

import org.flexpay.payments.service.OperationLevelService;
import org.flexpay.payments.persistence.OperationLevel;
import org.flexpay.payments.dao.OperationLevelDao;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.exception.FlexPayException;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

@Transactional (readOnly = true)
public class OperationLevelServiceImpl implements OperationLevelService {

	private OperationLevelDao levelDao;

	/**
	 * Read full operation type details
	 *
	 * @param stub Operation level stub
	 * @return OperationLevel if found, or <code>null</code> otherwise
	 */
	public OperationLevel read(@NotNull Stub<OperationLevel> stub) {
		return levelDao.readFull(stub.getId());
	}

	/**
	 * Read full operation level details by code
	 *
	 * @param code Operation level code
	 * @return OperationLevel if found, or <code>null</code> otherwise
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if lookup by code fails
	 */
	@NotNull
	public OperationLevel read(int code) throws FlexPayException {
		List<OperationLevel> levels = levelDao.findByCode(code);
		if (levels.isEmpty()) {
			throw new FlexPayException("Level not found #" + code);
		}
		return levels.get(0);

	}

	@Required
	public void setLevelDao(OperationLevelDao levelDao) {
		this.levelDao = levelDao;
	}
}
