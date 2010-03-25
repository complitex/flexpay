package org.flexpay.common.service.importexport;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.DataCorrection;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public interface CorrectionsService {

	/**
	 * Create or update data correction
	 *
	 * @param correction DataCorrection
	 */
	void save(DataCorrection correction);

    List<DataCorrection> find(Long internalId, int type, @NotNull Page<DataCorrection> pager);

    DataCorrection read(Stub<DataCorrection> stub);

	/**
	 * Find domain object by its external data source id
	 *
	 * @param externalId		External id
	 * @param cls			  Object class to find
	 * @param sourceDescription External data source description
	 * @return DomainObject
	 */
	@Nullable
	<T extends DomainObject> Stub<T> findCorrection(String externalId, Class<T> cls, Stub<DataSourceDescription> sourceDescription);

	/**
	 * Check if correction exists 
	 *
	 * @param externalId		External id
	 * @param cls			  Object class to find
	 * @param sourceDescription External data source description
	 * @return DomainObject
	 */
	boolean existsCorrection(String externalId, Class<? extends DomainObject> cls, Stub<DataSourceDescription> sourceDescription);

	/**
	 * Create stub for new data correction or get existing one
	 *
	 * @param externalId External object id
	 * @param obj DomainObject
	 * @param sourceDescription Data source description
	 * @return stub for a new DataCorrection
	 * @throws IllegalArgumentException if <code>obj</code> class is not supported
	 */
	@NotNull
	DataCorrection getStub(String externalId, DomainObject obj, Stub<DataSourceDescription> sourceDescription)
			throws IllegalArgumentException;

	/**
	 *
	 * @param internalId internalId
	 * @param type type
	 * @param dataSourceDescriptionStub dataSourceDescriptionStub
	 * @return externalId
	 */
	String getExternalId(Long internalId, int type, Stub<DataSourceDescription> dataSourceDescriptionStub);

	/**
	 * Find external identifier of internal object
	 *
	 * @param obj Object to get external identifier of
	 * @param sourceDescription DataSourceDescription to get
	 * @return External id that if found, or <code>null</code> otherwise
	 */
	@Nullable
	<T extends DomainObject> String getExternalId(@NotNull T obj, Stub<DataSourceDescription> sourceDescription);

	/**
	 * Delete correction
	 * 
	 * @param correction Data correction to delete
	 */
	void delete(@NotNull DataCorrection correction);

    /**
     * Delete corrections
     *
     * @param objectIds Set of service provider identifiers
     * @param type corrections type
     */
    void delete(@NotNull Set<Long> objectIds, int type);

}
