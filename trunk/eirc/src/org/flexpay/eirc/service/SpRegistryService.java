package org.flexpay.eirc.service;

import java.util.List;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.eirc.persistence.SpRegistry;

public interface SpRegistryService {
	/**
	 * Create SpRegistry
	 * 
	 * @param spRegistry
	 *            SpRegistry
	 * @return created SpRegistry object
	 */
	public SpRegistry create(SpRegistry spRegistry) throws FlexPayException;
	
	/**
	 * Get all SpRegistry by SpFile in page mode
	 *
	 * @param pager Page object
	 * @return List of SpRegistry objects for pager
	 */
	List<SpRegistry> findObjects(Page<SpRegistry> pager, Long spFileId);

	/**
	 * Read SpRegistry object by its unique id
	 * 
	 * @param id
	 *            SpRegistry key
	 * @return SpRegistry object, or <code>null</code> if object not found
	 */
	SpRegistry read(Long id);

	/**
	 * Update SpRegistry
	 * 
	 * @param spRegistry
	 *            SpRegistry to update for
	 * @return Updated SpRegistry object
	 * @throws FlexPayException
	 *             if SpRegistry object is invalid
	 */
	SpRegistry update(SpRegistry spRegistry) throws FlexPayException;

	void delete(SpRegistry spRegistry);
}
