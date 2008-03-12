package org.flexpay.eirc.service;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.eirc.persistence.SpRegistryRecord;

public interface SpRegistryRecordService {
	/**
	 * Create SpRegistryRecord
	 * 
	 * @param spRegistryRecord
	 *            SpRegistryRecord
	 * @return created SpRegistryRecord object
	 */
	public SpRegistryRecord create(SpRegistryRecord spRegistryRecord)
			throws FlexPayException;

	/**
	 * Read SpRegistryRecord object by its unique id
	 * 
	 * @param id
	 *            SpRegistryRecord key
	 * @return SpRegistryRecord object, or <code>null</code> if object not
	 *         found
	 */
	SpRegistryRecord read(Long id);

	/**
	 * Update SpRegistryRecord
	 * 
	 * @param spRegistryRecord
	 *            SpRegistryRecord to update for
	 * @return Updated SpRegistryRecord object
	 * @throws FlexPayException
	 *             if SpRegistryRecord object is invalid
	 */
	SpRegistryRecord update(SpRegistryRecord spRegistryRecord)
			throws FlexPayException;

	void delete(SpRegistryRecord spRegistryRecord);

}
