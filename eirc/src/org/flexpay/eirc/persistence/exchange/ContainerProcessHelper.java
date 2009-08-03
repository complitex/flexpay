package org.flexpay.eirc.persistence.exchange;

import org.flexpay.bti.persistence.apartment.ApartmentAttribute;
import org.flexpay.bti.persistence.apartment.ApartmentAttributeType;
import org.flexpay.bti.persistence.apartment.BtiApartment;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.service.fetch.ReadHints;
import org.flexpay.common.service.fetch.ReadHintsHolder;
import org.flexpay.eirc.persistence.Consumer;
import org.flexpay.eirc.persistence.EircRegistryRecordProperties;
import org.flexpay.eirc.persistence.exchange.delayed.DelayedUpdateApartmentAttributes;
import org.flexpay.eirc.persistence.exchange.delayed.DelayedUpdateNope;
import org.flexpay.eirc.service.imp.fetch.ReadHintsConstants;
import org.jetbrains.annotations.NotNull;

public class ContainerProcessHelper {

	/**
	 * extract consumer from registry record properties
	 *
	 * @param record  RegistryRecord
	 * @param factory Operations factory
	 * @return Consumer if found
	 * @throws FlexPayException if consumer cannot be found
	 */
	@NotNull
	public static Consumer getConsumer(RegistryRecord record, ServiceOperationsFactory factory) throws FlexPayException {
		EircRegistryRecordProperties props = (EircRegistryRecordProperties) record.getProperties();
		if (props.hasFullConsumer()) {
			return props.getConsumer();
		}
		Stub<Consumer> consumerStub = props.getConsumerStub();
		if (consumerStub == null) {
			throw new FlexPayException("Consumer was not set up, cannot change number of habitants apartment parameter");
		}

		ReadHints hints = ReadHintsHolder.getHints();
		if (hints != null) {
			hints.setHint(ReadHintsConstants.READ_FULL_CONSUMER);
		}

		Consumer consumer = factory.getConsumerService().read(consumerStub);
		if (consumer == null) {
			throw new FlexPayException("Consumer was not set up, cannot change number of habitants apartment parameter");
		}
		props.setFullConsumer(consumer);
		return consumer;
	}

	public static DelayedUpdate updateApartmentAttribute(RegistryRecord record, String newValue, String attributeTypeName,
														 ServiceOperationsFactory factory)
			throws FlexPayException {

		Consumer consumer = getConsumer(record, factory);

		ApartmentAttributeType attributeType = factory.getApartmentAttributeTypeService().findTypeByName(attributeTypeName);
		BtiApartment btiApartment = null;
		EircRegistryRecordProperties props = (EircRegistryRecordProperties) record.getProperties();
		if (props.hasFullApartment()) {
			btiApartment = (BtiApartment) props.getApartment();
		} else {
			ReadHints hints = ReadHintsHolder.getHints();
			if (hints != null) {
				hints.setHint(ReadHintsConstants.READ_APARTMENT_ATTRIBUTES);
			}
		}
		if (btiApartment == null) {
			btiApartment = factory.getBtiApartmentService().readWithAttributes(consumer.getApartmentStub());
			if (btiApartment != null) {
				props.setFullApartment(btiApartment);
			}
		}
		if (btiApartment == null) {
			throw new FlexPayException("BtiApartment for apartment with id " + consumer.getApartmentStub().getId() + " does not exist");
		}
		ApartmentAttribute attribute = btiApartment.getCurrentAttribute(attributeType);
		if ((attribute != null && !attribute.getStringValue().equals(newValue)) || attribute == null) {
			attribute = new ApartmentAttribute();
			attribute.setAttributeType(attributeType);
			attribute.setStringValue(newValue);
			btiApartment.setNormalAttribute(attribute);
			return new DelayedUpdateApartmentAttributes(btiApartment, factory.getBtiApartmentService());
		}

		return DelayedUpdateNope.INSTANCE;
	}
}
