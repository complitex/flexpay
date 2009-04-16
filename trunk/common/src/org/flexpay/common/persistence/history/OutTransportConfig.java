package org.flexpay.common.persistence.history;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.service.transport.OutTransport;

/**
 * base configuration class for {@link OutTransport}s
 */
public abstract class OutTransportConfig extends DomainObject {

	/**
	 * Create OutTransport from config
	 *
	 * @return Transport instance
	 */
	public abstract OutTransport createTransport();
}
