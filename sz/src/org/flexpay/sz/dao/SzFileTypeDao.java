package org.flexpay.sz.dao;

import java.util.List;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.sz.persistence.SzFileType;

public interface SzFileTypeDao extends GenericDao<SzFileType, Long> {

	List<SzFileType> listSzFileTypes();

}