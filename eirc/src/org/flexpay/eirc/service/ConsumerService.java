package org.flexpay.eirc.service;

import org.flexpay.eirc.persistence.Consumer;

public interface ConsumerService {

	/**
	 * Try to find persistent consumer by example
	 *
	 * @param example Consumer
	 * @return Persistent consumer if found, or <code>null</code> otherwise
	 */
	Consumer findConsumer(Consumer example);
}
