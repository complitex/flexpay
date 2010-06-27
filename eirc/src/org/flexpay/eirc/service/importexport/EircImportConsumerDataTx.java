package org.flexpay.eirc.service.importexport;

import org.flexpay.ab.persistence.Street;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.persistence.exchange.delayed.DelayedUpdatesContainer;

import java.util.List;
import java.util.Map;

public interface EircImportConsumerDataTx {
	public boolean processBatch(Stub<DataSourceDescription> sd, RawConsumerData data,
								Map<String, List<Street>> nameObjsMap, DelayedUpdatesContainer delayedUpdates);

	public void postProcessed();
}
