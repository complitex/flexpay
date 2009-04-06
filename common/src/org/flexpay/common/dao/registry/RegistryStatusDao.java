package org.flexpay.common.dao.registry;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.persistence.registry.RegistryStatus;

import java.util.List;

public interface RegistryStatusDao extends GenericDao<RegistryStatus, Long> {

	List<RegistryStatus> findAll();
}
