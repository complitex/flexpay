package org.flexpay.eirc.service;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.eirc.persistence.SpFile;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.persistence.SpRegistryRecord;
import org.flexpay.eirc.persistence.SpRegistryType;

import java.util.List;

public interface SpFileService {

	/**
	 * Create SpFile
	 *
	 * @param spFile SpFile
	 * @return created SpFile object
	 * @throws FlexPayException if failure occurs
	 */
	public SpFile create(SpFile spFile) throws FlexPayException;

	/**
	 * Read SpFile object by its unique id
	 *
	 * @param id SpFile key
	 * @return SpFile object, or <code>null</code> if object not found
	 */
	SpFile read(Long id);

	/**
	 * Update SpFile
	 *
	 * @param spFile SpFile to update for
	 * @return Updated SpFile object
	 * @throws FlexPayException if SpFile object is invalid
	 */
	SpFile update(SpFile spFile) throws FlexPayException;

	/**
	 * Get a list of available SpFile
	 *
	 * @return List of SpFile
	 */
	List<SpFile> getEntities();

	/**
	 * Delete file
	 *
	 * @param spFile ServiceProvider obtained file
	 */
	void delete(SpFile spFile);

	/**
	 * Get registries for file
	 *
	 * @param spFile ServiceProvider obtained file
	 * @return List of registries in a file
	 */
	List<SpRegistry> getRegistries(SpFile spFile);

	/**
	 * Get registry records for processing
	 *
	 * @param registry Registry header
	 * @param pager Page
	 * @return list of records
	 */
	List<SpRegistryRecord> getRecordsForProcessing(SpRegistry registry, Page<SpRegistryRecord> pager);

	/**
	 * Find registry type by id
	 *
	 * @param type SpRegistryType enum id
	 * @return SpRegistryType if found
	 * @throws InvalidRegistryTypeException if registry type is not supported
	 */
	SpRegistryType getRegistryType(int type) throws InvalidRegistryTypeException;

	/**
	 * Clear current session
	 */
	void clearSession();
}
