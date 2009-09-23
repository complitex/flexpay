package org.flexpay.eirc.service.importexport;

import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.service.importexport.ImportService;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.importexport.RawDataSource;

import java.util.List;
import java.util.Map;

public interface EircImportServiceTx extends ImportService {

	public boolean processBatch(long[] counters, boolean inited,
								Stub<DataSourceDescription> sd, RawDataSource<RawConsumerData> dataSource,
								Map<String, List<Street>> nameObjsMap);
}
