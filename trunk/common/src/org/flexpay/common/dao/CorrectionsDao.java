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
	DomainObject findCorrection(String externalId, int type, Class cls, DataSourceDescription sourceDescription);
}
