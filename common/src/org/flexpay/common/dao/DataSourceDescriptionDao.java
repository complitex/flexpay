package org.flexpay.common.dao;

import org.flexpay.common.persistence.DataSourceDescription;

import java.util.List;

public interface DataSourceDescriptionDao extends GenericDao<DataSourceDescription, Long> {

	List<DataSourceDescription> findMasterSourceDescription();

    /**
     * Find all data source descriptions
     *
     * @return list of data source descriptions
     */
    List<DataSourceDescription> find();

    List<DataSourceDescription> findAvailableDataSources(Long internalId, int type);

}
