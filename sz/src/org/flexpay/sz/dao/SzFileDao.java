package org.flexpay.sz.dao;

import java.util.List;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.sz.persistence.SzFile;

public interface SzFileDao extends GenericDao<SzFile, Long>{
	
	List<SzFile> listSzFiles();

}
