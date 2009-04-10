package org.flexpay.eirc.service;

import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.persistence.Consumer;
import org.flexpay.payments.persistence.Service;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

	/**
	 * Find consumer by service provider, account number and subservice code
	 *
	 * @param serviceProvider ServiceProvider stub
	 * @param accountNumber   External account number
	 * @param serviceId	   Service code
	 * @return Consumer if found, or <code>null</code> otherwise
	 */
	Consumer findConsumer(ServiceProvider serviceProvider, String accountNumber, String serviceId);

	/**
	 * Find Service by service provider and subservice code
	 *
	 * @param serviceProvider ServiceProvider stub
	 * @param serviceId	   Service code
	 * @return Service if found, or <code>null</code> otherwise
	 */
	Service findService(ServiceProvider serviceProvider, String serviceId);

	/**
	 * Read consumer info
	 *
	 * @param stub Consumer stub
	 * @return Consumer instance
	 */
	@Nullable
	Consumer read(@NotNull Stub<Consumer> stub);
}
