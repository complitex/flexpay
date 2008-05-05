package org.flexpay.eirc.dao;

import java.util.List;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.eirc.persistence.SpRegistryStatus;

public interface SpRegistryStatusDao extends GenericDao<SpRegistryStatus, Long> {

	List<SpRegistryStatus> findAll();
}
