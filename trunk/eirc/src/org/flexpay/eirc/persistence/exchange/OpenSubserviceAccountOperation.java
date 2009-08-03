package org.flexpay.eirc.persistence.exchange;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.persistence.registry.RegistryType;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.eirc.dao.importexport.RawConsumersDataUtil;
import org.flexpay.eirc.persistence.Consumer;
import org.flexpay.eirc.persistence.ConsumerInfo;
import org.flexpay.eirc.persistence.EircRegistryRecordProperties;
import org.flexpay.eirc.persistence.exchange.delayed.DelayedUpdateConsumer;
import org.flexpay.eirc.persistence.exchange.delayed.DelayedUpdateCorrection;
import org.flexpay.eirc.persistence.exchange.delayed.DelayedUpdateNope;
import org.flexpay.eirc.persistence.exchange.delayed.DelayedUpdatesContainer;
import org.flexpay.eirc.service.ConsumerService;
import org.flexpay.eirc.service.importexport.RawConsumerData;
import org.flexpay.eirc.util.config.ApplicationConfig;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.payments.persistence.EircRegistryProperties;
import org.flexpay.payments.persistence.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Open subservice provider personal account.
 * <p/>
 * Container type #14
 */
public class OpenSubserviceAccountOperation extends ContainerOperation {

	private ServiceOperationsFactory factory;

	private Date changeApplyingDate;
	private String subserviceId;

	public OpenSubserviceAccountOperation(ServiceOperationsFactory factory, List<String> datum) throws InvalidContainerException {
		super(Integer.valueOf(datum.get(0)));

		if (datum.size() < 4) {
			throw new InvalidContainerException("Not enough data for open subservice consumer");
		}

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyy");
		try {
			changeApplyingDate = simpleDateFormat.parse(datum.get(1));
		} catch (ParseException e) {
			throw new InvalidContainerException("Cannot parse date: " + datum.get(1));
		}

		subserviceId = datum.get(3);

		this.factory = factory;
	}

	/**
	 * Process operation
	 *
	 * @param registry Registry header
	 * @param record   Registry record
	 * @throws FlexPayException if failure occurs
	 */
	public DelayedUpdate process(Registry registry, RegistryRecord record) throws FlexPayException {

		if (!validate(registry, record)) {
			return DelayedUpdateNope.INSTANCE;
		}

		EircRegistryRecordProperties props = (EircRegistryRecordProperties) record.getProperties();
		Apartment apartment = props.getApartment();
		if (apartment == null) {
			apartment = props.getConsumer().getApartment();
		}
		Person person = props.getPerson();
		if (person == null) {
			person = props.getConsumer().getResponsiblePerson();
		}

		Consumer consumer = new Consumer();
		consumer.setApartment(apartment);
		consumer.setResponsiblePerson(person);
		consumer.setExternalAccountNumber(record.getPersonalAccountExt());
		consumer.setBeginDate(changeApplyingDate);
		consumer.setEndDate(ApplicationConfig.getFutureInfinite());
		consumer.setService(findService(registry));
		Consumer base = props.getConsumer();
		consumer.setEircAccount(base.getEircAccount());

		saveConsumerInfo(record, consumer);
		DelayedUpdatesContainer container = new DelayedUpdatesContainer();
		container.addUpdate(new DelayedUpdateConsumer(consumer, factory.getConsumerService()));

		createCorrection(registry, record, consumer, container);
		return container;
	}

	private void saveConsumerInfo(RegistryRecord record, Consumer consumer) {

		EircRegistryRecordProperties props = (EircRegistryRecordProperties) record.getProperties();
		ConsumerInfo info = props.getConsumer().getConsumerInfo();
		consumer.setConsumerInfo(info);
	}


	private void createCorrection(Registry registry, RegistryRecord record, Consumer consumer, DelayedUpdatesContainer container) {

		CorrectionsService correctionsService = factory.getCorrectionsService();

		// add short consumer correction
		RawConsumerData data = RawConsumersDataUtil.convert(registry, record);
		data.addNameValuePair(RawConsumerData.FIELD_SERVICE_CODE, subserviceId);
		String shortId = data.getShortConsumerId();
		EircRegistryProperties props = (EircRegistryProperties) registry.getProperties();
		ServiceProvider provider = factory.getServiceProviderService().read(props.getServiceProviderStub());
		Stub<DataSourceDescription> sd = provider.getDataSourceDescriptionStub();

		container.addUpdate(new DelayedUpdateCorrection(correctionsService, consumer, shortId, sd));

		// add full consumer correction
		String fullId = data.getFullConsumerId();
		if (!fullId.equals(shortId)) {
			container.addUpdate(new DelayedUpdateCorrection(correctionsService, consumer, fullId, sd));
		}
	}

	private Service findService(Registry registry) throws FlexPayException {

		ConsumerService consumerService = factory.getConsumerService();
		EircRegistryProperties props = (EircRegistryProperties) registry.getProperties();
		ServiceProvider provider = factory.getServiceProviderService().read(props.getServiceProviderStub());
		Service service = consumerService.findService(new Stub<ServiceProvider>(provider), subserviceId);
		if (service == null) {
			throw new FlexPayException("Cannot find subservice for provider " + provider +
									   " code: " + subserviceId);
		}

		return service;
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

		if (registry.getRegistryType().getCode() != RegistryType.TYPE_INFO) {
			throw new FlexPayException("Create consumer operation only allowed in Information registry type");
		}

		EircRegistryRecordProperties props = (EircRegistryRecordProperties) record.getProperties();
		Consumer consumer = props.getConsumer();
		if (props.getApartment() == null || consumer == null || consumer.getApartment().getId() == null) {
			throw new FlexPayException("Cannot create sub consumer without apartment set");
		}

		if (props.getPerson() == null || consumer.getResponsiblePerson() == null) {
			log.warn("Creating sub consumer without person set");
		}

		// add short consumer correction
		RawConsumerData data = RawConsumersDataUtil.convert(registry, record);
		data.addNameValuePair(RawConsumerData.FIELD_SERVICE_CODE, subserviceId);
		String id = data.getShortConsumerId();
		EircRegistryProperties registryProps = (EircRegistryProperties) registry.getProperties();
		ServiceProvider provider = factory.getServiceProviderService().read(registryProps.getServiceProviderStub());
		Stub<Consumer> persistent = factory.getCorrectionsService().findCorrection(
				id, Consumer.class, provider.getDataSourceDescriptionStub());
		if (persistent != null) {
			log.info("Already existing subconsumer: {}", id);
			return false;
		}

		log.debug("Creating subconsumer");

		return true;
	}
}
