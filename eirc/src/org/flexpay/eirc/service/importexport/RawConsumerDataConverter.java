package org.flexpay.eirc.service.importexport;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.service.importexport.DataConverter;
import org.flexpay.eirc.persistence.Consumer;
import org.flexpay.eirc.persistence.Service;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.service.ConsumerService;

public class RawConsumerDataConverter implements DataConverter<Consumer, RawConsumerData> {

	private ConsumerService consumerService;

	/**
	 * Convert raw data to domain object
	 *
	 * @param rawData			RawData
	 * @param sd				 Data source description
	 * @param correctionsService CorrectionsService
	 * @return DomainObject
	 * @throws FlexPayException if failure occurs
	 */
	public Consumer fromRawData(RawConsumerData rawData, DataSourceDescription sd, CorrectionsService correctionsService)
			throws FlexPayException {

		Person personStub = correctionsService.findCorrection(
				rawData.getPersonCorrectionId(), Person.class, sd);
		Apartment apartmentStub = correctionsService.findCorrection(
				rawData.getApartmentId(), Apartment.class, sd);

		SpRegistry registry = rawData.getRegistry();
		Service service = consumerService.findService(registry.getServiceProvider(), rawData.getServiceCode());

		Consumer consumer = new Consumer();
		consumer.setExternalAccountNumber(rawData.getAccountNumber());
		consumer.setApartment(apartmentStub);
		consumer.setResponsiblePerson(personStub);
		consumer.setService(service);

		return consumer;
	}

	public void setConsumerService(ConsumerService consumerService) {
		this.consumerService = consumerService;
	}
}
