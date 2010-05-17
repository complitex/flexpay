package org.flexpay.eirc.service;

import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.persistence.Consumer;
import org.flexpay.payments.persistence.Service;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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
	 * @param serviceProviderStub ServiceProvider stub
	 * @param accountNumber   External account number
	 * @param serviceCode     Service code
	 * @return Consumer if found, or <code>null</code> otherwise
	 */
	Consumer findConsumer(Stub<ServiceProvider> serviceProviderStub, String accountNumber, String serviceCode);

	/**
	 * Find Service by service provider and subservice code
	 *
	 * @param serviceProviderStub ServiceProvider stub
	 * @param serviceCode	   Service code
	 * @return Service if found, or <code>null</code> otherwise
	 */
	Service findService(Stub<ServiceProvider> serviceProviderStub, String serviceCode);

	/**
	 * Read consumer info
	 *
	 * @param stub Consumer stub
	 * @return Consumer instance
	 */
	@Nullable
	Consumer read(@NotNull Stub<Consumer> stub);

	/**
	 * Try to find consumer by external account number and service
	 *
	 * @param accountNumber External account number
	 * @param serviceStub Service stub
	 * @return Consumer if found, or <code>null</code> otherwise
	 */
	@Nullable
	Consumer findConsumer(@NotNull String accountNumber, @NotNull Stub<Service> serviceStub);

    /**
     * Try to find consumer by ERC account number and service
     *
     * @param ercAccount ERC account number
     * @param serviceStub   Service stub
     * @return Consumer if found, or <code>null</code> otherwise
     */
    @Nullable
    Consumer findConsumerByERCAccountAndService(@NotNull String ercAccount, @NotNull Stub<Service> serviceStub);

    /**
     * Try to find consumers by ERC account number
     *
     * @param ercAccount ERC account number
     * @return found Consumers
     */
    @NotNull
    List<Consumer> findConsumersByERCAccount(@NotNull String ercAccount);

}
