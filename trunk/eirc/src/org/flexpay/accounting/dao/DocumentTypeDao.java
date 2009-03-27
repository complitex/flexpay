package org.flexpay.accounting.dao;

import org.flexpay.accounting.persistence.DocumentType;
import org.flexpay.common.dao.GenericDao;

import java.util.List;

public interface DocumentTypeDao extends GenericDao<DocumentType, Long> {

	List<DocumentType> findDocumentType(int code);
}
