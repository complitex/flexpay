package org.flexpay.eirc.service.impl.fetch;

import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.service.fetch.ReadHints;
import org.flexpay.common.service.fetch.ReadHintsHandler;
import org.flexpay.common.service.impl.fetch.ProcessingReadHintsHandlerFactory;
import org.flexpay.eirc.dao.EircRegistryRecordPropertiesDao;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class ConsumerReadHintsHandlerFactory extends ProcessingReadHintsHandlerFactory {

	private EircRegistryRecordPropertiesDao recordPropertiesDao;

	@Override
	protected ReadHintsHandler doGetInstance(Stub<Registry> registryStub, FetchRange range, List<RegistryRecord> records) {
		return (registryStub == null && range == null)? new ConsumerAndEircAccountReadHintsHandler(records, recordPropertiesDao):
				new ConsumerReadHintsHandler(registryStub, range, records, recordPropertiesDao);
	}

	@Override
	public boolean supports(ReadHints hints) {
		return hints.hintSet(ReadHintsConstants.READ_FULL_CONSUMER);
	}

	@Required
	public void setRecordPropertiesDao(EircRegistryRecordPropertiesDao recordPropertiesDao) {
		this.recordPropertiesDao = recordPropertiesDao;
	}
}
