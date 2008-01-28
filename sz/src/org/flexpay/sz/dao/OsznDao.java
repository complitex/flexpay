package org.flexpay.sz.dao;

import java.util.List;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.sz.persistence.Oszn;

public interface OsznDao extends GenericDao<Oszn, Long> {
	
	List<Oszn> listOszn();

}
