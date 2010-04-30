package org.flexpay.eirc.service.impl;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.dao.ConsumerDao;
import org.flexpay.eirc.dao.ConsumerDaoExt;
import org.flexpay.eirc.persistence.Consumer;
import org.flexpay.eirc.service.ConsumerService;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.service.SPService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional (readOnly = true)
public class ConsumerServiceImpl implements ConsumerService {

	private Logger log = LoggerFactory.getLogger(getClass());

	private ConsumerDao consumerDao;
	private ConsumerDaoExt consumerDaoExt;
	private SPService spService;

	/**
	 * Try to find persistent consumer by example
	 *
	 * @param example Consumer
	 * @return Persistent consumer if found, or <code>null</code> otherwise
	 */
	public Consumer findConsumer(Consumer example) {
		List<Consumer> consumers = consumerDaoExt.findConsumers(
				new Page(1, 1), // request the only record
				example.getResponsiblePerson().getId(),
				example.getService().getId(),
				example.getExternalAccountNumber(),
				example.getApartment().getId()
		);
		return consumers.isEmpty() ? null : consumers.get(0);
	}

	/**
	 * Find consumer by service provider, account number and subservice code
	 *
	 * @param serviceProviderStub ServiceProvider stub
	 * @param accountNumber   External account number
	 * @param serviceCode     Service code
	 * @return Consumer if found, or <code>null</code> otherwise
	 */
	public Consumer findConsumer(Stub<ServiceProvider> serviceProviderStub, String accountNumber, String serviceCode) {
		if (serviceCode.startsWith("#")) {
			return consumerDaoExt.findConsumerByProviderServiceCode(
					serviceProviderStub.getId(), accountNumber, serviceCode.substring(1));
		}
		return consumerDaoExt.findConsumerByTypeCode(serviceProviderStub.getId(), accountNumber, Long.valueOf(serviceCode));
	}

	/**
	 * Find Service by service provider and subservice code
	 *
	 * @param serviceProviderStub ServiceProvider stub
	 * @param serviceCode	   Subservice code
	 * @return Service if found, or <code>null</code> otherwise
	 */
	public Service findService(Stub<ServiceProvider> serviceProviderStub, String serviceCode) {
		return spService.findService(serviceProviderStub, serviceCode);
	}

	/**
	 * Read consumer info
	 *
	 * @param stub Consumer stub
	 * @return Consumer instance
	 */
	@Nullable
	public Consumer read(@NotNull Stub<Consumer> stub) {
		return consumerDao.readFull(stub.getId());
	}

	/**
	 * Create or update Consumer object
	 *
	 * @param consumer Consumer to save
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation failure occurs
	 */
	@Transactional (readOnly = false)
	public void save(Consumer consumer) throws FlexPayExceptionContainer {
		if (consumer.isNew()) {
			consumer.setId(null);
			consumerDao.create(consumer);
		} else {
			consumerDao.update(consumer);
		}
	}

	/**
	 * Try to find consumer by external account number and service
	 *
	 * @param accountNumber External account number
	 * @param serviceStub   Service stub
	 * @return Consumer if found, or <code>null</code> otherwise
	 */
    @Nullable
	@Override
	public Consumer findConsumer(@NotNull String accountNumber, @NotNull Stub<Service> serviceStub) {

		List<Consumer> consumers = consumerDao.findConsumersByAccountAndService(accountNumber, serviceStub.getId());
		if (consumers.isEmpty()) {
			return null;
		}

		if (consumers.size() > 1) {
			log.info("Found several consumers by service {} and external account {}", serviceStub, accountNumber);
		}

		return consumers.get(0);
	}

    /**
     * Try to find consumers by external account number
     *
     * @param accountNumber External account number
     * @return found Consumers
     */
    @NotNull
    @Override
    public List<Consumer> findConsumers(@NotNull String accountNumber) {
        return consumerDao.findConsumersByAccount(accountNumber);
    }

	@Required
	public void setConsumerDao(ConsumerDao consumerDao) {
		this.consumerDao = consumerDao;
	}

	@Required
	public void setConsumerDaoExt(ConsumerDaoExt consumerDaoExt) {
		this.consumerDaoExt = consumerDaoExt;
	}

	@Required
	public void setSpService(SPService spService) {
		this.spService = spService;
	}
}
