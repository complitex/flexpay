package org.flexpay.eirc.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.eirc.persistence.RegistryRecordContainer;

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
