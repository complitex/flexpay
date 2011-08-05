package org.flexpay.common.service;

import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.DataSourceFilter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface DataSourceDescriptionService extends JpaSetService {

    DataSourceDescription read(@NotNull Stub<DataSourceDescription> stub);

    /**
     * Get a list of available data sources
     *
     * @return List of data sources
     */
    List<DataSourceDescription> find();

    /**
     * Get a list of available data sources for
     * object with type and internalId
     *
     * @param internalId internalId
     * @param type type
     * @return List of data sources
     */
    List<DataSourceDescription> find(Long internalId, int type);

    /**
     * Initialize filter
     *
     * @param filter DataSourceFilter to initialize
     * @return DataSourceFilter back
     */
    DataSourceFilter initDataSourceFilter(DataSourceFilter filter);

    /**
     * Initialize filter
     *
     * @param internalId internalId
     * @param type type
     * @param filter DataSourceFilter to initialize
     * @return DataSourceFilter back
     */
    DataSourceFilter initDataSourceFilter(Long internalId, int type, DataSourceFilter filter);

}
