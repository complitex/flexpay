package org.flexpay.eirc.persistence.exchange;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.DataCorrection;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.eirc.dao.importexport.RawConsumersDataUtil;
import org.flexpay.eirc.persistence.*;
import org.flexpay.eirc.service.ConsumerService;
import org.flexpay.eirc.service.importexport.RawConsumerData;
import org.flexpay.eirc.util.config.ApplicationConfig;

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
	public void process(SpRegistry registry, SpRegistryRecord record) throws FlexPayException {

		if (!validate(registry, record)) {
			return;
		}

		Apartment apartment = record.getApartment();
		if (apartment == null) {
			apartment = ((Consumer) record.getConsumer()).getApartment();
		}
		Person person = record.getPerson();
		if (person == null) {
			person = record.getConsumer().getResponsiblePerson();
		}

		Consumer consumer = new Consumer();
		consumer.setApartment(apartment);
		consumer.setResponsiblePerson(person);
		consumer.setExternalAccountNumber(record.getPersonalAccountExt());
		consumer.setBeginDate(changeApplyingDate);
		consumer.setEndDate(ApplicationConfig.getFutureInfinite());
		consumer.setService(findService(registry));
		Consumer base = (Consumer) record.getConsumer();
		consumer.setEircAccount(base.getEircAccount());

		saveConsumerInfo(record, consumer);
		ConsumerService consumerService = factory.getConsumerService();
		try {
			consumerService.save(consumer);
		} catch (FlexPayExceptionContainer c) {
			for (FlexPayException exception : c.getExceptions()) {
				log.error("Failed saving subconsumer", exception);
			}
			throw new FlexPayException("Failed creating subconsumer");
		}

		createCorrection(registry, record, consumer);
	}

	private void saveConsumerInfo(SpRegistryRecord record, Consumer consumer) {
		ConsumerInfo info = ((Consumer) record.getConsumer()).getConsumerInfo();

		consumer.setConsumerInfo(info);
	}


	private void createCorrection(SpRegistry registry, SpRegistryRecord record, Consumer consumer) {

		CorrectionsService correctionsService = factory.getCorrectionsService();

		// add short consumer correction
		RawConsumerData data = RawConsumersDataUtil.convert(registry, record);
		data.addNameValuePair(RawConsumerData.FIELD_SERVICE_CODE, subserviceId);
		String shortId = data.getShortConsumerId();
		DataCorrection corr = correctionsService.getStub(shortId, consumer,
				registry.getServiceProvider().getDataSourceDescription());
		correctionsService.save(corr);

		// add full consumer correction
		String fullId = data.getFullConsumerId();
		if (!fullId.equals(shortId)) {
			corr = correctionsService.getStub(fullId, consumer,
					registry.getServiceProvider().getDataSourceDescription());
			correctionsService.save(corr);
		}
	}

	private Service findService(SpRegistry registry) throws FlexPayException {
		ConsumerService consumerService = factory.getConsumerService();
		Service service = consumerService.findService(registry.getServiceProvider(), subserviceId);
		if (service == null) {
			throw new FlexPayException("Cannot find subservice for provider " + registry.getServiceProvider() +
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
	private boolean validate(SpRegistry registry, SpRegistryRecord record) throws FlexPayException {

		if (registry.getRegistryType().getCode() != SpRegistryType.TYPE_INFO) {
			throw new FlexPayException("Create consumer operation only allowed in Information registry type");
		}

		Consumer consumer = (Consumer) record.getConsumer();
		if (record.getApartment() == null || consumer == null || consumer.getApartment().getId() == null) {
			throw new FlexPayException("Cannot create sub consumer without apartment set");
		}

		if (record.getPerson() == null || consumer.getResponsiblePerson().getId() == null) {
			throw new FlexPayException("Cannot create sub consumer without person set");
		}

		// add short consumer correction
		RawConsumerData data = RawConsumersDataUtil.convert(registry, record);
		data.addNameValuePair(RawConsumerData.FIELD_SERVICE_CODE, subserviceId);
		String id = data.getShortConsumerId();
		Stub<Consumer> persistent = factory.getCorrectionsService().findCorrection(
				id, Consumer.class, registry.getServiceProvider().getDataSourceDescription());
		if (persistent != null) {
			if (log.isInfoEnabled()) {
				log.info("Already existing subconsumer: " + id);
			}
			return false;
		}

		return true;
	}
}
