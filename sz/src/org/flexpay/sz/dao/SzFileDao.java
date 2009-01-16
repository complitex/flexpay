package org.flexpay.sz.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.sz.persistence.SzFile;

import java.util.List;

public interface SzFileDao extends GenericDao<SzFile, Long> {
	
	List<SzFile> listSzFiles();

	List<SzFile> findSzFiles(Page<SzFile> pager);

}
