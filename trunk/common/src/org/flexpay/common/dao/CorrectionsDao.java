package org.flexpay.common.dao;

import org.flexpay.common.persistence.DataCorrection;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.DomainObject;

public interface CorrectionsDao {

	/**
	 * Create or update data correction
	 *
	 * @param correction DataCorrection
	 */
	void save(DataCorrection correction);

	/**
	 * Delete correction
	 *
	 * @param correction DataCorrection
	 */
	void delete(DataCorrection correction);

	/**
	 * Find domain object by correction
	 *
	 * @param externalId		DataSource internal object id
	 * @param type			  DomainObject type
	 * @param cls			   DomainObject class to retrive
	 * @param sourceDescription Data source description
	 * @return DomainObject
	 */
	<T extends DomainObject> T findCorrection(String externalId, int type, Class<T> cls, DataSourceDescription sourceDescription);

	/**
	 * Get correction internal object id
	 *
	 * @param externalId		DataSource internal object id
	 * @param type			  DomainObject type
	 * @param sourceDescription Data source description
	 * @return DomainObject
	 */
	public Long getInternalId(final String externalId, final int type,
									final DataSourceDescription sourceDescription);

	/**
	 * Check if correction exists
	 *
	 * @param externalId		DataSource internal object id
	 * @param type			  DomainObject type
	 * @param cls			   DomainObject class to retrive
	 * @param sourceDescription Data source description
	 * @return DomainObject
	 */
	boolean existsCorrection(String externalId, int type, Class<?> cls, DataSourceDescription sourceDescription);
}
