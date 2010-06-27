package org.flexpay.eirc.persistence.exchange;

import org.apache.commons.lang.time.StopWatch;
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
import org.flexpay.eirc.service.impl.fetch.ReadHintsConstants;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContainerProcessHelper {

	static private final Logger log = LoggerFactory.getLogger(ContainerProcessHelper.class);

	static private final StopWatch findAttributeTypeWatch = new StopWatch();
	static private final StopWatch btiApartmentWatch = new StopWatch();
	static private final StopWatch setNormalAttributeWatch = new StopWatch();

	static {
		findAttributeTypeWatch.start();
		findAttributeTypeWatch.suspend();

		btiApartmentWatch.start();
		btiApartmentWatch.suspend();

		setNormalAttributeWatch.start();
		setNormalAttributeWatch.suspend();
	}

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

		findAttributeTypeWatch.resume();
		ApartmentAttributeType attributeType = factory.getApartmentAttributeTypeService().findTypeByName(attributeTypeName);
		findAttributeTypeWatch.suspend();
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
			btiApartmentWatch.resume();
			btiApartment = factory.getBtiApartmentService().readWithAttributes(consumer.getApartmentStub());
			btiApartmentWatch.suspend();
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
			setNormalAttributeWatch.resume();
			btiApartment.setNormalAttribute(attribute);
			setNormalAttributeWatch.suspend();
			return new DelayedUpdateApartmentAttributes(btiApartment, factory.getBtiApartmentService());
		}

		return DelayedUpdateNope.INSTANCE;
	}

	public static void printWatch() {
		log.debug("Time find attribute type: {}, bti apartment: {}, set normal attribute: {}",
				new Object[]{findAttributeTypeWatch, btiApartmentWatch, setNormalAttributeWatch});
	}
}
