package org.flexpay.common.service.impl;

import org.flexpay.common.dao.DataSourceDescriptionDao;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.DataSourceDescriptionService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
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

    @Required
    public void setDataSourceDescriptionDao(DataSourceDescriptionDao dataSourceDescriptionDao) {
        this.dataSourceDescriptionDao = dataSourceDescriptionDao;
    }
}
