package org.flexpay.payments.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.payments.persistence.DocumentType;

import java.util.List;

public interface DocumentTypeDao extends GenericDao<DocumentType, Long> {

	List<DocumentType> findByCode(int code);

}
