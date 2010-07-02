package org.flexpay.common.dao;

import org.flexpay.common.persistence.DataCorrection;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.Nullable;

public interface CorrectionDaoExt {

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
	 * @param externalId DataSource internal object id
	 * @param type	   DomainObject type
	 * @param cls		DomainObject class to retrive
	 * @param sd		 Data source description
	 * @return DomainObject
	 */
	@Nullable
	<T extends DomainObject> Stub<T> findCorrection(String externalId, int type, Class<T> cls, Stub<DataSourceDescription> sd);

	@Nullable
	String getExternalId(Long internalId, int type, Long dataSourceDescriptionId);

	/**
	 * Check if correction exists
	 *
	 * @param externalId DataSource internal object id
	 * @param type	   DomainObject type
	 * @param sd		 DataSourceDescription
	 * @return DomainObject
	 */
	boolean existsCorrection(String externalId, int type, Stub<DataSourceDescription> sd);

	/**
	 * Find existing correction if any
	 *
	 * @param externalId External object id
	 * @param type	   Object type id
	 * @param sd		 DataSourceDescription
	 * @return DataCorrection if exists, or <code>null</code> otherwise
	 */
	DataCorrection findCorrection(String externalId, int type, Stub<DataSourceDescription> sd);
}
