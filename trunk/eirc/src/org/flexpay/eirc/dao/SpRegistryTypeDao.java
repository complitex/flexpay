package org.flexpay.eirc.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.eirc.persistence.RegistryType;

import java.util.List;

public interface SpRegistryTypeDao extends GenericDao<RegistryType, Long> {

	List<RegistryType> findAll();
}
