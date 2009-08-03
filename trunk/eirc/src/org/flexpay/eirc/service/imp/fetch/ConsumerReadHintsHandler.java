package org.flexpay.eirc.service.imp.fetch;

import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.service.imp.fetch.ProcessingReadHintsHandler;
import org.flexpay.eirc.dao.EircRegistryRecordPropertiesDao;
import org.flexpay.eirc.persistence.EircRegistryRecordProperties;

import java.util.Iterator;
import java.util.List;

public class ConsumerReadHintsHandler extends ProcessingReadHintsHandler {

	private EircRegistryRecordPropertiesDao recordPropertiesDao;

	public ConsumerReadHintsHandler(Stub<Registry> registryStub, FetchRange range, List<RegistryRecord> records,
									EircRegistryRecordPropertiesDao recordPropertiesDao) {
		super(registryStub, range, records);
		this.recordPropertiesDao = recordPropertiesDao;
	}

	@Override
	public void read() {

		List<EircRegistryRecordProperties> props = recordPropertiesDao.findWithConsumers(
				registryStub.getId(), range.getLowerBound(), range.getUpperBound());
		Iterator<EircRegistryRecordProperties> propsIt = props.iterator();
		for (RegistryRecord record : records) {
			EircRegistryRecordProperties prop = (EircRegistryRecordProperties) record.getProperties();
			EircRegistryRecordProperties fetchedProp = propsIt.next();

			if (!prop.equals(fetchedProp)) {
				throw new IllegalStateException("Invalid properties read, expected the same set");
			}

			if (fetchedProp.getConsumer() != null) {
				prop.setFullConsumer(fetchedProp.getConsumer());
			}
		}
	}
}
