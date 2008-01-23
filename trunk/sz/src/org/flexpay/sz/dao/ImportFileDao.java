package org.flexpay.sz.dao;

import java.util.List;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.sz.persistence.ImportFile;

public interface ImportFileDao extends GenericDao<ImportFile, Long>{
	
	List<ImportFile> listImportFiles();

}
