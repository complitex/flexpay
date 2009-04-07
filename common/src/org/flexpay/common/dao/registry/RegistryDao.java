package org.flexpay.common.dao.registry;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.registry.Registry;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface RegistryDao extends GenericDao<Registry, Long> {

	List<Registry> findObjects(Page<Registry> pager, Long spFileId);

	void deleteRecords(Long registryId);

	void deleteRegistryContainers(Long registryId);

	void deleteRecordContainers(Long registryId);

	void deleteRecordProperties(Long registryId);

	/**
	 * Fetch registry with containers
	 *
	 * @param registryId Registry key
	 * @return One element registry list if found, or empty list otherwise
	 */
	@NotNull
	List<Registry> listRegistryWithContainers(Long registryId);

    /**
     * List registries for file
     *
     * @param fileId spFile id
     * @return list of registries
     */
    List<Registry> listRegistries(Long fileId);

	/**
	 * Find registries by number and sender organization
	 * @param registryNumber Registry number
	 * @param senderOrganizationId Sender organization key
	 * @return List of registries, empty if no registries found matching criteria
	 */
	@NotNull
	List<Registry> findRegistriesByNumber(Long registryNumber, Long senderOrganizationId);
}
