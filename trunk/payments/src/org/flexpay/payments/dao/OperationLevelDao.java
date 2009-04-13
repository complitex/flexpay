package org.flexpay.payments.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.payments.persistence.OperationLevel;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface OperationLevelDao extends GenericDao<OperationLevel, Long> {

	@NotNull
	List<OperationLevel> findByCode(int code);
}
