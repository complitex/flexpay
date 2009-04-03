package org.flexpay.payments.dao;

import org.flexpay.payments.persistence.DocumentType;
import org.flexpay.common.dao.GenericDao;

import java.util.List;

public interface DocumentTypeDao extends GenericDao<DocumentType, Long> {

	List<DocumentType> findDocumentType(String code);
}
