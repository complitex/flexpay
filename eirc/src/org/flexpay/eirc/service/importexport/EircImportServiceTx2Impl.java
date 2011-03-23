package org.flexpay.eirc.service.importexport;

import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.service.importexport.ImportServiceImpl;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.importexport.RawDataSource;
import org.flexpay.eirc.persistence.exchange.delayed.DelayedUpdatesContainer;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional (readOnly = true)
public class EircImportServiceTx2Impl extends ImportServiceImpl implements EircImportServiceTx {

	private EircImportConsumerDataTx importConsumerService;

	private DelayedUpdatesContainer delayedUpdates = new DelayedUpdatesContainer();

	@Override
	public boolean processBatch(long[] counters, boolean inited,
								Stub<DataSourceDescription> sd, RawDataSource<RawConsumerData> dataSource,
								Map<String, List<Street>> nameObjsMap) {
		log.debug("Fetching for next batch");

		// get next butch
		List<RawConsumerData> datum = readRawDataBatch(inited, dataSource);
		if (datum.isEmpty()) {
			flushStack();
			return false;
		}

		log.debug("Fetched next batch");

		for (RawConsumerData data : datum) {
			++counters[0];

			if (importConsumerService.processBatch(sd, data, nameObjsMap, delayedUpdates)) {
				addToStack(data.getRegistryRecord());
			} else {
				++counters[1];
			}
		}

		return true;
	}

	@Transactional (readOnly = false, propagation = Propagation.REQUIRED)
	@Override
	protected void flushStack() {
		try {
			delayedUpdates.doUpdate();
		} catch (FlexPayException e) {
			log.warn("Failed update: {}", e);
		} catch (FlexPayExceptionContainer flexPayExceptionContainer) {
			log.warn("Failed update: {}", flexPayExceptionContainer);
		}
		super.flushStack();
		delayedUpdates.getUpdates().clear();
	}

	private List<RawConsumerData> readRawDataBatch(boolean inited, RawDataSource<RawConsumerData> dataSource) {
		log.debug("Reading batch");

		if (!inited) {
			dataSource.initialize();
			log.debug("Inited");
		}

		List<RawConsumerData> result = dataSource.nextPage();
		log.debug("Listing records for update");

		return result;
	}

	@Required
	public void setImportConsumerService(EircImportConsumerDataTx importConsumerService) {
		this.importConsumerService = importConsumerService;
	}
}
