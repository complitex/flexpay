package org.flexpay.eirc.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.eirc.persistence.RegistryStatus;

import java.util.List;

public interface SpRegistryStatusDao extends GenericDao<RegistryStatus, Long> {

	List<RegistryStatus> findAll();
}
