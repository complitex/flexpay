package org.flexpay.payments.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.payments.persistence.DocumentAdditionType;

import java.util.List;

public interface DocumentAdditionTypeDao extends GenericDao<DocumentAdditionType, Long> {

	/**
	 * Returns document addition type code instance by code
	 * @param code code
	 * @return document addition type code instance
	 */
	List<DocumentAdditionType> findTypeByCode(int code);
}
