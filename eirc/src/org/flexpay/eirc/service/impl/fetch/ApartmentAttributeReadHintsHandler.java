package org.flexpay.eirc.service.impl.fetch;

import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.service.impl.fetch.ProcessingReadHintsHandler;
import org.flexpay.eirc.dao.EircRegistryRecordPropertiesDao;
import org.flexpay.eirc.persistence.EircRegistryRecordProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;

public class ApartmentAttributeReadHintsHandler extends ProcessingReadHintsHandler {

	private Logger log = LoggerFactory.getLogger(getClass());
	private EircRegistryRecordPropertiesDao recordPropertiesDao;

	public ApartmentAttributeReadHintsHandler(Stub<Registry> registryStub, FetchRange range, List<RegistryRecord> records,
											  EircRegistryRecordPropertiesDao recordPropertiesDao) {
		super(registryStub, range, records);
		this.recordPropertiesDao = recordPropertiesDao;
	}

	@Override
	public void read() {

		List<EircRegistryRecordProperties> props = recordPropertiesDao.findWithApartmentAttributes(
				registryStub.getId(), range.getLowerBound(), range.getUpperBound());
		Iterator<EircRegistryRecordProperties> propsIt = props.iterator();

		EircRegistryRecordProperties prev = null;
		for (RegistryRecord record : records) {
			EircRegistryRecordProperties prop = (EircRegistryRecordProperties) record.getProperties();
			EircRegistryRecordProperties fetchedProp = propsIt.next();
			// skip duplicates
			while (prev != null && fetchedProp.equals(prev)) {
				fetchedProp = propsIt.next();
			}
			prev = fetchedProp;

			if (!prop.equals(fetchedProp)) {
				log.error("Invalid properties read: expected {}, but found {}, fetch range: {}",
						new Object[] {prop, fetchedProp, range});
				throw new IllegalStateException("Invalid properties read, expected the same set");
			}

			if (fetchedProp.getConsumer() != null) {
				prop.setFullApartment(fetchedProp.getApartment());
			}
		}
	}
}
