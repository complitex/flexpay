package org.flexpay.common.dao.registry;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.persistence.registry.RegistryArchiveStatus;

import java.util.List;

public interface RegistryArchiveStatusDao extends GenericDao<RegistryArchiveStatus, Long> {

	List<RegistryArchiveStatus> findByCode(int code);
}
