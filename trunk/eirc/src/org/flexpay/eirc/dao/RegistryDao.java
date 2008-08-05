package org.flexpay.eirc.dao;

import java.util.List;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.eirc.persistence.SpRegistry;
import org.jetbrains.annotations.NotNull;

public interface RegistryDao extends GenericDao<SpRegistry, Long> {

	List<SpRegistry> findObjects(Page<SpRegistry> pager, Long spFileId);

	void deleteRecords(Long registryId);

	void deleteRegistryContainers(Long registryId);

	void deleteRecordContainers(Long registryId);

	void deleteQuittances(Long registryId);

	/**
	 * Fetch registry with containers
	 *
	 * @param registryId Registry key
	 * @return One element registry list if found, or empty list otherwise
	 */
	@NotNull
	List<SpRegistry> listRegistryWithContainers(Long registryId);

	/**
	 * Find registries by number and sender organisation
	 * @param registryNumber Registry number
	 * @param senderOrganisationId Sender organisation key
	 * @return List of registries, empty if no registries found matching criteria
	 */
	@NotNull
	List<SpRegistry> findRegistriesByNumber(Long registryNumber, Long senderOrganisationId);
}
