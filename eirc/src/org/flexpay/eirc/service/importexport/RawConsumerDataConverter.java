package org.flexpay.eirc.service.importexport;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.service.importexport.DataConverter;
import org.flexpay.eirc.persistence.*;
import org.flexpay.eirc.service.SPService;

public class RawConsumerDataConverter implements DataConverter<Consumer, RawConsumerData> {

	private SPService spService;

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
		SpRegistryRecord record = rawData.getRegistryRecord();
		ServiceType type = spService.getServiceType(record.getServiceCode().intValue());
		Service service = spService.getService(registry.getServiceProvider(), type);

		Consumer consumer = new Consumer();
		consumer.setExternalAccountNumber(rawData.getAccountNumber());
		consumer.setApartment(apartmentStub);
		consumer.setResponsiblePerson(personStub);
		consumer.setService(service);

		return consumer;
	}

	/**
	 * Setter for property 'spService'.
	 *
	 * @param spService Value to set for property 'spService'.
	 */
	public void setSpService(SPService spService) {
		this.spService = spService;
	}

}
