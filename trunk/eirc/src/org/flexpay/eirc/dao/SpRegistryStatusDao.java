package org.flexpay.eirc.dao;

import java.util.List;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.eirc.persistence.RegistryStatus;

public interface SpRegistryStatusDao extends GenericDao<RegistryStatus, Long> {

	List<RegistryStatus> findAll();
}
