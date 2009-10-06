package org.flexpay.common.service.impl.fetch;

import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.service.fetch.ReadHintsHandler;

import java.util.List;

public abstract class ProcessingReadHintsHandler implements ReadHintsHandler {

	protected Stub<Registry> registryStub;
	protected FetchRange range;
	protected List<RegistryRecord> records;

	public ProcessingReadHintsHandler(Stub<Registry> registryStub, FetchRange range, List<RegistryRecord> records) {
		this.registryStub = registryStub;
		this.range = range;
		this.records = records;
	}
}
