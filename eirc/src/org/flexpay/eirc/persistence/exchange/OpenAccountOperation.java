package org.flexpay.eirc.persistence.exchange;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.DataCorrection;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.eirc.dao.importexport.RawConsumersDataUtil;
import org.flexpay.eirc.persistence.*;
import org.flexpay.eirc.service.ConsumerService;
import org.flexpay.eirc.service.EircAccountService;
import org.flexpay.eirc.service.importexport.RawConsumerData;
import org.flexpay.eirc.util.config.ApplicationConfig;

import java.util.List;

/**
 * Open new service provider personal account
 */
public class OpenAccountOperation extends AbstractChangePersonalAccountOperation {

	private ServiceOperationsFactory factory;

	public OpenAccountOperation(ServiceOperationsFactory factory, List<String> datum) throws InvalidContainerException {
		super(datum);

		this.factory = factory;
	}

	/**
	 * Process operation
	 *
	 * @param registry Registry header
	 * @param record   Registry record
	 * @throws FlexPayException if failure occurs
	 */
	public void process(SpRegistry registry, SpRegistryRecord record) throws FlexPayException {

		if (!validate(registry, record)) {
			return;
		}
		EircAccount account = getEircAccount(record);

		Consumer consumer = new Consumer();
		consumer.setApartment(record.getApartment());
		consumer.setResponsiblePerson(record.getPerson());
		consumer.setExternalAccountNumber(record.getPersonalAccountExt());
		consumer.setBeginDate(changeApplyingDate);
		consumer.setEndDate(ApplicationConfig.getFutureInfinite());
		consumer.setService(findService(registry, record));
		consumer.setEircAccount(account);

		saveConsumerInfo(record, consumer);
		ConsumerService consumerService = factory.getConsumerService();
		try {
			consumerService.save(consumer);
		} catch (FlexPayExceptionContainer c) {
			for (FlexPayException exception : c.getExceptions()) {
				log.error("Failed saving consumer", exception);
			}
			throw new FlexPayException("Failed creating consumer");
		}

		createCorrection(registry, record, consumer);
		record.setConsumer(consumer);
	}

	private void saveConsumerInfo(SpRegistryRecord record, Consumer consumer) {
		ConsumerInfo info = new ConsumerInfo();

		info.setFirstName(record.getFirstName());
		info.setMiddleName(record.getMiddleName());
		info.setLastName(record.getLastName());

		info.setCityName(record.getCity());
		info.setStreetTypeName(record.getStreetType());
		info.setStreetName(record.getStreetName());
		info.setBuildingNumber(record.getBuildingNum());
		info.setBuildingBulk(record.getBuildingBulkNum());
		info.setApartmentNumber(record.getApartmentNum());

		factory.getConsumerInfoService().save(info);
		consumer.setConsumerInfo(info);
	}

	private void createCorrection(SpRegistry registry, SpRegistryRecord record, Consumer consumer) {

		CorrectionsService correctionsService = factory.getCorrectionsService();

		// add short consumer correction
		RawConsumerData data = RawConsumersDataUtil.convert(registry, record);
		DataCorrection corr = correctionsService.getStub(data.getShortConsumerId(), consumer,
				registry.getServiceProvider().getDataSourceDescription());
		correctionsService.save(corr);

		// add full consumer correction
		corr = correctionsService.getStub(data.getFullConsumerId(), consumer,
				registry.getServiceProvider().getDataSourceDescription());
		correctionsService.save(corr);
	}

	private Service findService(SpRegistry registry, SpRegistryRecord record) throws FlexPayException {
		ConsumerService consumerService = factory.getConsumerService();
		Service service = consumerService.findService(registry.getServiceProvider(), record.getServiceCode());
		if (service == null) {
			throw new FlexPayException("Cannot find service for provider " + registry.getServiceProvider() +
									   " and code: " + record.getServiceCode());
		}

		return service;
	}

	/**
	 * Check if EIRC account exists, and create a new one if necessary
	 *
	 * @param record RegistryRecord
	 * @return EircAccount instance
	 * @throws FlexPayException if failure occurs
	 */
	private EircAccount getEircAccount(SpRegistryRecord record) throws FlexPayException {

		EircAccountService accountService = factory.getAccountService();
		EircAccount account = accountService.findAccount(record.getPerson(), record.getApartment());
		if (account != null) {
			return account;
		}

		account = new EircAccount();
		account.setPerson(record.getPerson());
		account.setApartment(record.getApartment());

		try {
			accountService.save(account);
		} catch (FlexPayExceptionContainer c) {
			for (FlexPayException exception : c.getExceptions()) {
				log.error("Failed saving account", exception);
			}
			throw new FlexPayException("Failed creating EIRC account");
		}

		return account;
	}

	/**
	 * Validate registry record for processing, validation is not allowed if consumer already exists
	 *
	 * @param registry Registry
	 * @param record   Record
	 * @return <code>true</true> if processing allowed, or <code>false</code> otherwise
	 * @throws FlexPayException if processing cannot be done at all
	 */
	private boolean validate(SpRegistry registry, SpRegistryRecord record) throws FlexPayException {

		if (log.isDebugEnabled()) {
			log.debug("validating record: " + record);
		}

		if (registry.getRegistryType().getCode() != SpRegistryType.TYPE_INFO) {
			throw new FlexPayException("Create consumer operation only allowed in Information registry type");
		}

		if (record.getConsumer() != null) {
			log.info("Already existing consumer");
			return false;
		}

		if (record.getApartment() == null) {
			throw new FlexPayException("Cannot create consumer without apartment set");
		}

		if (record.getPerson() == null) {
			throw new FlexPayException("Cannot create consumer without person set");
		}

		if (log.isDebugEnabled()) {
			log.debug("Creating consumer: " + record);
		}

		return true;
	}
}
