package org.flexpay.eirc.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.eirc.persistence.SpRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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
     * List registries for file
     *
     * @param fileId spFile id
     * @return list of registries
     */
    List<SpRegistry> listRegistries(Long fileId);

	/**
	 * Find registries by number and sender organization
	 * @param registryNumber Registry number
	 * @param senderOrganizationId Sender organization key
	 * @return List of registries, empty if no registries found matching criteria
	 */
	@NotNull
	List<SpRegistry> findRegistriesByNumber(Long registryNumber, Long senderOrganizationId);
}
