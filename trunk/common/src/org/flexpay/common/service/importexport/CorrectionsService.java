package org.flexpay.common.service.importexport;

import org.flexpay.common.persistence.DataCorrection;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.DomainObject;

public interface CorrectionsService {

	/**
	 * Create or update data correction
	 *
	 * @param correction DataCorrection
	 */
	void save(DataCorrection correction);

	/**
	 * Find domain object by its external data source id
	 *
	 * @param externalId		External id
	 * @param cls			  Object class to find
	 * @param sourceDescription External data source description
	 * @return DomainObject
	 */
	DomainObject findCorrection(String externalId, Class<? extends DomainObject> cls, DataSourceDescription sourceDescription);

	/**
	 * Check if correction exists 
	 *
	 * @param externalId		External id
	 * @param cls			  Object class to find
	 * @param sourceDescription External data source description
	 * @return DomainObject
	 */
	boolean existsCorrection(String externalId, Class<? extends DomainObject> cls, DataSourceDescription sourceDescription);

	/**
	 * Create stub for new data correction
	 *
	 * @param externalId External object id
	 * @param obj DomainObject
	 * @param sourceDescription Data source description
	 * @return stub for a new DataCorrection
	 */
	DataCorrection getStub(String externalId, DomainObject obj, DataSourceDescription sourceDescription);
}
