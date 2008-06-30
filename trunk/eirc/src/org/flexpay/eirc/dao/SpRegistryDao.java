package org.flexpay.eirc.dao;

import java.util.List;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.eirc.persistence.SpRegistry;

public interface SpRegistryDao extends GenericDao<SpRegistry, Long> {

	List<SpRegistry> findObjects(Page<SpRegistry> pager, Long spFileId);

	void deleteRecords(Long registryId);

	void deleteRegistryContainers(Long registryId);

	void deleteRecordContainers(Long registryId);

	void deleteQuittances(Long registryId);
}
