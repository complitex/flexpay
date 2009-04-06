package org.flexpay.common.dao.registry;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.persistence.registry.RegistryRecordContainer;

import java.util.List;

public interface RegistryRecordContainerDao extends GenericDao<RegistryRecordContainer, Long> {

	/**
	 * Get list of record containers
	 *
	 * @param recordId Registry record identifier
	 * @return List of containers associated with the record
	 */
	List<RegistryRecordContainer> findRecordContainers(Long recordId);
}
