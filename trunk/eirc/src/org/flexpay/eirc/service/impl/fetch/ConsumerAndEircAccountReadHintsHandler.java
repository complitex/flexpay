package org.flexpay.eirc.service.impl.fetch;

import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.service.impl.fetch.ProcessingReadHintsHandler;
import org.flexpay.eirc.dao.EircRegistryRecordPropertiesDao;
import org.flexpay.eirc.persistence.Consumer;
import org.flexpay.eirc.persistence.EircRegistryRecordProperties;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.flexpay.common.util.CollectionUtils.map;

public class ConsumerAndEircAccountReadHintsHandler extends ProcessingReadHintsHandler {
	private EircRegistryRecordPropertiesDao recordPropertiesDao;

	public ConsumerAndEircAccountReadHintsHandler(@NotNull List<RegistryRecord> records,
									@NotNull EircRegistryRecordPropertiesDao recordPropertiesDao) {
		super(null, null, records);
		this.recordPropertiesDao = recordPropertiesDao;
	}

	@Override
	public void read() {

		List<Long> recordIds = new LinkedList<Long>();

		for (RegistryRecord record : records) {
			recordIds.add(record.getId());
		}

		List<EircRegistryRecordProperties> props = recordPropertiesDao.findWithConsumers(recordIds);

		Map<Long, Consumer> consumers = map();
		for (EircRegistryRecordProperties prop : props) {
			if (prop.getConsumer() != null) {
				consumers.put(prop.getId(), prop.getConsumer());
			}
		}

		for (RegistryRecord record : records) {
			EircRegistryRecordProperties prop = (EircRegistryRecordProperties) record.getProperties();

			if (consumers.containsKey(prop.getId())) {
				prop.setFullConsumer(consumers.get(prop.getId()));
			}
		}
	}
}
