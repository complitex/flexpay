package org.flexpay.eirc.service;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.persistence.Consumer;
import org.flexpay.eirc.persistence.EircAccount;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.persistence.ServiceType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public interface ConsumerService {

    /**
     * Try to find persistent consumer by EIRC account stub
     *
     * @param eircAccountStub EIRC account stub
     * @return List of consumers if found, or <code>null</code> otherwise
     */
    List<Consumer> findConsumers(Stub<EircAccount> eircAccountStub);

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
     * @param serviceTypeStub   Service type stub
     * @return found Consumers
     */
    @Nullable
    List<Consumer> findConsumersByExAccountAndServiceType(@NotNull String accountNumber, @NotNull Stub<ServiceType> serviceTypeStub);

    /**
     * Try to find consumer by external account number
     *
     * @param accountNumber External account number
     * @return found Consumers
     */
    @Nullable
    List<Consumer> findConsumersByExAccount(@NotNull String accountNumber);

    /**
     * Try to find consumers by apartments
     *
     * @param apartmentStubs apartment stubs
     * @return found Consumers
     */
    @NotNull
    List<Consumer> findConsumersByApartment(@NotNull Set<Stub<Apartment>> apartmentStubs);

    /**
     * Try to find consumers by ERC account number and service type
     *
     * @param ercAccount ERC account number
     * @param serviceTypeStub   Service type stub
     *
     * @return found Consumers
     */
    @NotNull
    List<Consumer> findConsumersByERCAccountAndServiceType(@NotNull String ercAccount, @NotNull Stub<ServiceType> serviceTypeStub);

    /**
     * Try to find consumers by ERC account number
     *
     * @param ercAccount ERC account number
     * 
     * @return found Consumers
     */
    @NotNull
    List<Consumer> findConsumersByERCAccount(@NotNull String ercAccount);

}
