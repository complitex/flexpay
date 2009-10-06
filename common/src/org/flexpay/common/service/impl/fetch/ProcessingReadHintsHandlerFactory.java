package org.flexpay.common.service.impl.fetch;

import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.service.fetch.ReadHintsHandler;
import org.flexpay.common.service.fetch.ReadHintsHandlerFactory;

import java.util.List;

public abstract class ProcessingReadHintsHandlerFactory implements ReadHintsHandlerFactory {

	/**
	 * Create instance with the following <code>params</code>
	 *
	 * @param params Registry stub, FetchRange, List&lt;RegistryRecord&gt;
	 * @return read hints handler
	 */
	@SuppressWarnings ({"unchecked"})
	@Override
	public final ReadHintsHandler getInstance(Object... params) {

		Stub<Registry> stub = (Stub<Registry>) params[0];
		FetchRange range = (FetchRange) params[1];
		List<RegistryRecord> records = (List<RegistryRecord>) params[2];

		return doGetInstance(stub, range, records);
	}

	protected abstract ReadHintsHandler doGetInstance(
			Stub<Registry> registryStub, FetchRange range, List<RegistryRecord> records);
}
