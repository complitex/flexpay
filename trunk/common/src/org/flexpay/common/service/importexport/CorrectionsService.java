package org.flexpay.common.service.importexport;

import org.flexpay.common.persistence.DataCorrection;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.NonNls;

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
	@Nullable
	<T extends DomainObject> Stub<T> findCorrection(String externalId, Class<T> cls, DataSourceDescription sourceDescription);

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
	 * Create stub for new data correction or get existing one
	 *
	 * @param externalId External object id
	 * @param obj DomainObject
	 * @param sourceDescription Data source description
	 * @return stub for a new DataCorrection
	 */
	@NotNull
	DataCorrection getStub(String externalId, DomainObject obj, DataSourceDescription sourceDescription);

	/**
	 *
	 * @param internalId
	 * @param type
	 * @param dataSourceDescriptionId
	 * @return
	 * @deprecated refactor to use Objects instead of Longs
	 */
	String getExternalId(@NonNls Long internalId, int type, Long dataSourceDescriptionId);

	/**
	 * Delete correction
	 * 
	 * @param correction Data correction to delete
	 */
	void delete(@NotNull DataCorrection correction);
}
