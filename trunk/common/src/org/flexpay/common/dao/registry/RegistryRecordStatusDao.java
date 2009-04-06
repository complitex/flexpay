package org.flexpay.common.dao.registry;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.persistence.registry.RegistryRecordStatus;

import java.util.List;

public interface RegistryRecordStatusDao extends GenericDao<RegistryRecordStatus, Long> {

	List<RegistryRecordStatus> findAll();
}
