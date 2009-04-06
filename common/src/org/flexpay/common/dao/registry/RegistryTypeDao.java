package org.flexpay.common.dao.registry;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.persistence.registry.RegistryType;

import java.util.List;

public interface RegistryTypeDao extends GenericDao<RegistryType, Long> {

	List<RegistryType> findAll();
}
