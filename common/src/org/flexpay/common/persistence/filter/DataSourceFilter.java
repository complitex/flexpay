package org.flexpay.common.persistence.filter;

import org.flexpay.common.persistence.DataSourceDescription;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static org.flexpay.common.util.CollectionUtils.list;

public class DataSourceFilter extends PrimaryKeyFilter<DataSourceDescription> {

	private List<DataSourceDescription> dataSources = list();

	public DataSourceFilter() {
		super(-1L);
	}

    public List<DataSourceDescription> getDataSources() {
        return dataSources;
    }

    public void setDataSources(List<DataSourceDescription> dataSources) {
        this.dataSources = dataSources;
    }

    @Nullable
    public DataSourceDescription getSelected() {

        Long selectedId = getSelectedId();
        if (needFilter() && selectedId != null) {
            for (DataSourceDescription dataSource : dataSources) {
                if (selectedId.equals(dataSource.getId())) {
                    return dataSource;
                }
            }
        }

        return null;
    }

}
