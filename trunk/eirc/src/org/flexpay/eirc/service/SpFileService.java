package org.flexpay.eirc.service;

import java.util.List;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.eirc.persistence.SpFile;

public interface SpFileService {
	/**
	 * Create SpFile
	 * 
	 * @param spFile
	 *            SpFile
	 * @return created SpFile object
	 */
	public SpFile create(SpFile spFile) throws FlexPayException;

	/**
	 * Read SpFile object by its unique id
	 * 
	 * @param id
	 *            SpFile key
	 * @return SpFile object, or <code>null</code> if object not found
	 */
	SpFile read(Long id);

	/**
	 * Update SpFile
	 * 
	 * @param spFile
	 *            SpFile to update for
	 * @return Updated SpFile object
	 * @throws FlexPayException
	 *             if SpFile object is invalid
	 */
	SpFile update(SpFile spFile) throws FlexPayException;

	/**
	 * Get a list of available SpFile
	 * 
	 * @return List of SpFile
	 */
	List<SpFile> getEntities();

	void delete(SpFile spFile);


}
