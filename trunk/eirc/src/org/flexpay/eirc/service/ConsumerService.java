package org.flexpay.eirc.service;

import org.flexpay.eirc.persistence.Consumer;
import org.flexpay.common.exception.FlexPayExceptionContainer;

public interface ConsumerService {

	/**
	 * Try to find persistent consumer by example
	 *
	 * @param example Consumer
	 * @return Persistent consumer if found, or <code>null</code> otherwise
	 */
	Consumer findConsumer(Consumer example);

	/**
	 * Create or update Consumer object
	 * 
	 * @param consumer Consumer to save
	 * @throws FlexPayExceptionContainer if validation failure occurs
	 */
	void save(Consumer consumer) throws FlexPayExceptionContainer;
}
