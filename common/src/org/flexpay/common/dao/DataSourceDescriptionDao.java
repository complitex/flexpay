package org.flexpay.common.dao;

import org.flexpay.common.persistence.DataSourceDescription;

import java.util.List;

public interface DataSourceDescriptionDao extends GenericDao<DataSourceDescription, Long> {

	List<DataSourceDescription> findMasterSourceDescription();
}