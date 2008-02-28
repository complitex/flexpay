package org.flexpay.eirc.dao;

import java.util.List;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.eirc.persistence.SpFile;

public interface SpFileDao extends GenericDao<SpFile, Long>{
	
	List<SpFile> listSpFiles();

}