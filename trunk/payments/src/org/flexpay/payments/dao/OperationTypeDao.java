package org.flexpay.payments.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.payments.persistence.OperationType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface OperationTypeDao extends GenericDao<OperationType, Long> {

	@NotNull
	List<OperationType> findByCode(int code);
}
