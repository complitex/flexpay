package org.flexpay.eirc.persistence.exchange;

import org.flexpay.bti.persistence.apartment.ApartmentAttributeBase;
import org.flexpay.bti.persistence.apartment.ApartmentAttributeType;
import org.flexpay.bti.persistence.apartment.BtiApartment;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.eirc.persistence.Consumer;
import org.flexpay.eirc.persistence.EircRegistryRecordProperties;
import org.jetbrains.annotations.NotNull;

public class ContainerProcessHelper {

	/**
	 * extract consumer from registry record properties
	 *
	 * @param record RegistryRecord
	 * @param factory Operations factory
	 * @return Consumer if found
	 * @throws FlexPayException if consumer cannot be found
	 */
	@NotNull
	public static Consumer getConsumer(RegistryRecord record, ServiceOperationsFactory factory) throws FlexPayException {
		EircRegistryRecordProperties props = (EircRegistryRecordProperties) record.getProperties();
		Stub<Consumer> consumerStub = props.getConsumerStub();
		if (consumerStub == null) {
			throw new FlexPayException("Consumer was not set up, cannot change number of habitants apartment parameter");
		}
		Consumer consumer = factory.getConsumerService().read(consumerStub);
		if (consumer == null) {
			throw new FlexPayException("Consumer was not set up, cannot change number of habitants apartment parameter");
		}
		return consumer;
	}

	public static void updateApartmentAttribute(RegistryRecord record, String newValue, String attributeTypeName,
												ServiceOperationsFactory factory) throws FlexPayException {

		Consumer consumer = getConsumer(record, factory);

		ApartmentAttributeType attributeType = factory.getApartmentAttributeTypeService().findTypeByName(attributeTypeName);
		BtiApartment btiApartment = factory.getBtiApartmentService().readWithAttributes(consumer.getApartmentStub());
		if (btiApartment == null) {
			throw new FlexPayException("BtiApartment for apartment with id " + consumer.getApartmentStub().getId() + " does not exist");
		}
		ApartmentAttributeBase attribute = btiApartment.getAttribute(attributeType);
		if ((attribute != null && !attribute.getCurrentValue().equals(newValue)) || attribute == null) {
			btiApartment.setNormalAttribute(attributeType, newValue);
			factory.getBtiApartmentService().updateAttributes(btiApartment);
		}
	}

}
