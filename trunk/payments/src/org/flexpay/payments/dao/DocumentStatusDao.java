package org.flexpay.payments.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.payments.persistence.DocumentStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface DocumentStatusDao extends GenericDao<DocumentStatus, Long> {

	@NotNull
	List<DocumentStatus> findByCode(int code);

}
