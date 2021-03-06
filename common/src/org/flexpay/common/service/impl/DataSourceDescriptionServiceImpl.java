package org.flexpay.common.service.impl;

import org.flexpay.common.dao.DataSourceDescriptionDao;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.DataSourceFilter;
import org.flexpay.common.service.DataSourceDescriptionService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.jpa.JpaTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public class DataSourceDescriptionServiceImpl implements DataSourceDescriptionService {

    private DataSourceDescriptionDao dataSourceDescriptionDao;

    @Override
    public DataSourceDescription read(@NotNull Stub<DataSourceDescription> stub) {
        return dataSourceDescriptionDao.readFull(stub.getId());
    }

    @Override
    public List<DataSourceDescription> find() {
        return dataSourceDescriptionDao.find();
    }

    @Override
    public List<DataSourceDescription> find(Long internalId, int type) {
        return dataSourceDescriptionDao.findAvailableDataSources(internalId, type);
    }

    @Override
    public DataSourceFilter initDataSourceFilter(DataSourceFilter filter) {
        if (filter == null) {
            filter = new DataSourceFilter();
        }

        List<DataSourceDescription> dataSources = find();
        filter.setDataSources(dataSources);

        return filter;
    }

    @Override
    public DataSourceFilter initDataSourceFilter(Long internalId, int type, DataSourceFilter filter) {
        if (filter == null) {
            filter = new DataSourceFilter();
        }

        List<DataSourceDescription> dataSources = find(internalId, type);
        filter.setDataSources(dataSources);

        return filter;
    }

    @Override
    public void setJpaTemplate(JpaTemplate jpaTemplate) {
        dataSourceDescriptionDao.setJpaTemplate(jpaTemplate);
    }

    @Required
    public void setDataSourceDescriptionDao(DataSourceDescriptionDao dataSourceDescriptionDao) {
        this.dataSourceDescriptionDao = dataSourceDescriptionDao;
    }

}
