package org.flexpay.eirc.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.eirc.persistence.SpRegistryType;

import java.util.List;

public interface SpRegistryTypeDao extends GenericDao<SpRegistryType, Long> {

	List<SpRegistryType> findAll();
}
