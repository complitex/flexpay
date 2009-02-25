package org.flexpay.sz.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.sz.persistence.Oszn;

import java.util.List;

public interface OsznDao extends GenericDao<Oszn, Long> {
	
	List<Oszn> listOszn();

}
