package org.flexpay.common.service.importexport;

import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.JpaSetService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface MasterIndexService extends JpaSetService {

	/**
	 * Get new master index value
	 *
	 * @param obj Object to get index for
	 * @return index value, or <code>null</null> if application instance is not intended to generate any shared data
	 */
	@Nullable
	<T extends DomainObject> String getNewMasterIndex(@NotNull T obj);

	/**
	 * Find master index of internal object
	 *
	 * @param obj internal object to find index for
	 * @return master index value if available, or <code>null</code> otherwise
	 */
	@Nullable
	<T extends DomainObject> String getMasterIndex(@NotNull T obj);

	/**
	 * Get a reference to master data source description
	 *
	 * @return DataSourceDescription
	 */
	@NotNull
	Stub<DataSourceDescription> getMasterSourceDescriptionStub();
}
