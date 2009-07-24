package org.flexpay.eirc.persistence.exchange;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.DataCorrection;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.payments.persistence.EircRegistryProperties;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.persistence.registry.RegistryType;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.eirc.dao.importexport.RawConsumersDataUtil;
import org.flexpay.eirc.persistence.*;
import org.flexpay.eirc.service.ConsumerService;
import org.flexpay.eirc.service.EircAccountService;
import org.flexpay.eirc.service.importexport.RawConsumerData;
import org.flexpay.eirc.util.config.ApplicationConfig;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.payments.persistence.Service;

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
	public void process(Registry registry, RegistryRecord record) throws FlexPayException {

		if (!validate(registry, record)) {
			return;
		}
		ConsumerInfo info = saveConsumerInfo(record);
		EircAccount account = getEircAccount(record, info);

		EircRegistryRecordProperties props = (EircRegistryRecordProperties) record.getProperties();
		Consumer consumer = new Consumer();
		consumer.setApartment(props.getApartment());
		consumer.setResponsiblePerson(props.getPerson());
		consumer.setExternalAccountNumber(record.getPersonalAccountExt());
		consumer.setBeginDate(changeApplyingDate);
		consumer.setEndDate(ApplicationConfig.getFutureInfinite());
		consumer.setService(findService(registry, record));
		consumer.setEircAccount(account);
		consumer.setConsumerInfo(info);

		ConsumerService consumerService = factory.getConsumerService();
		try {
			consumerService.save(consumer);
		} catch (FlexPayExceptionContainer c) {
			for (FlexPayException exception : c.getExceptions()) {
				log.error("Failed saving consumer", exception);
			}
			throw new FlexPayException("Failed creating consumer");
		}

		createCorrections(registry, record, consumer);
		props.setFullConsumer(consumer);
	}

	private ConsumerInfo saveConsumerInfo(RegistryRecord record) {

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

		return info;
	}

	private void createCorrections(Registry registry, RegistryRecord record, Consumer consumer) {

		CorrectionsService correctionsService = factory.getCorrectionsService();

		// add short consumer correction
		EircRegistryProperties props = (EircRegistryProperties) registry.getProperties();
		RawConsumerData data = RawConsumersDataUtil.convert(registry, record);
		ServiceProvider provider = factory.getServiceProviderService().read(props.getServiceProviderStub());
		Stub<DataSourceDescription> sd = provider.getDataSourceDescriptionStub();
		DataCorrection corr = correctionsService.getStub(data.getShortConsumerId(), consumer, sd);
		correctionsService.save(corr);

		// add full consumer correction
		corr = correctionsService.getStub(data.getFullConsumerId(), consumer, sd);
		correctionsService.save(corr);
	}

	private Service findService(Registry registry, RegistryRecord record) throws FlexPayException {
		ConsumerService consumerService = factory.getConsumerService();
		EircRegistryProperties props = (EircRegistryProperties) registry.getProperties();
		ServiceProvider provider = factory.getServiceProviderService().read(props.getServiceProviderStub());
		Service service = consumerService.findService(new Stub<ServiceProvider>(provider), record.getServiceCode());
		if (service == null) {
			throw new FlexPayException("Cannot find service for provider " + provider +
									   " and code: " + record.getServiceCode());
		}

		return service;
	}

	/**
	 * Check if EIRC account exists, and create a new one if necessary
	 *
	 * @param record RegistryRecord
	 * @param info ConsumerInfo
	 * @return EircAccount instance
	 * @throws FlexPayException if failure occurs
	 */
	private EircAccount getEircAccount(RegistryRecord record, ConsumerInfo info) throws FlexPayException {

		EircAccountService accountService = factory.getAccountService();

		EircRegistryRecordProperties props = (EircRegistryRecordProperties) record.getProperties();
		Stub<Person> personStub = props.getPersonStub();
		Stub<Apartment> apartmentStub = props.getApartmentStub();
		if (personStub != null && apartmentStub != null) {
			EircAccount account = accountService.findAccount(personStub, apartmentStub);
			if (account != null) {
				return account;
			}
		}

		EircAccount account = new EircAccount();
		account.setPerson(props.getPerson());
		account.setApartment(props.getApartment());
		account.setConsumerInfo(info);

		try {
			accountService.create(account);
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
	private boolean validate(Registry registry, RegistryRecord record) throws FlexPayException {

		log.debug("validating record: {}", record);

		if (registry.getRegistryType().getCode() != RegistryType.TYPE_INFO) {
			throw new FlexPayException("Create consumer operation only allowed in Information registry type");
		}

		EircRegistryRecordProperties props = (EircRegistryRecordProperties) record.getProperties();
		if (props.getConsumer() != null) {
			log.info("Already existing consumer");
			return false;
		}

		if (props.getApartment() == null) {
			throw new FlexPayException("Cannot create consumer without apartment set");
		}

		if (props.getPerson() == null) {
			log.warn("Creating account without person set");
		}

		log.debug("Creating consumer: {}", record);

		return true;
	}
}
