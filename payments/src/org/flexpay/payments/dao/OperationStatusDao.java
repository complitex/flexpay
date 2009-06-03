package org.flexpay.payments.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.payments.persistence.OperationStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface OperationStatusDao extends GenericDao<OperationStatus, Long> {

	@NotNull
	List<OperationStatus> findByCode(int code);

}
