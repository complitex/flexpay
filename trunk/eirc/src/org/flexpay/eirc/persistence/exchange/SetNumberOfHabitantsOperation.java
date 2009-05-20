package org.flexpay.eirc.persistence.exchange;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.bti.persistence.apartment.ApartmentAttributeConfig;
import org.flexpay.bti.persistence.apartment.ApartmentAttributeType;
import org.flexpay.bti.persistence.apartment.BtiApartment;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.eirc.persistence.EircRegistryRecordProperties;

import java.util.List;

/**
 * Open new service provider personal account
 */
public class SetNumberOfHabitantsOperation extends AbstractChangePersonalAccountOperation {

	private ServiceOperationsFactory factory;

	public SetNumberOfHabitantsOperation(ServiceOperationsFactory factory, List<String> datum) throws InvalidContainerException {
		super(datum);
		this.factory = factory;
	}

	/**
	 * Process operation
	 *
	 * @param registry Registry header
	 * @param record   Registry record
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if failure occurs
	 */
	public void process(Registry registry, RegistryRecord record) throws FlexPayException {

		EircRegistryRecordProperties props = (EircRegistryRecordProperties) record.getProperties();
		Apartment apartment = props.getApartment();
		if (apartment == null) {
			throw new FlexPayException("Apartment was not set up, cannot change number of habitants parameter");
		}

		BtiApartment btiApartment = factory.getBtiApartmentService().readWithAttributes(new Stub<BtiApartment>(apartment.getId()));
		if (btiApartment == null) {
			throw new FlexPayException("BtiApartment for apartment with id " + apartment.getId() + " does not exist");
		}
		ApartmentAttributeType attributeType = factory.getApartmentAttributeTypeService().findTypeByName(ApartmentAttributeConfig.ATTR_NUMBER_OF_HABITANTS);
		btiApartment.setNormalAttribute(attributeType, newValue);

		factory.getBtiApartmentService().updateAttributes(btiApartment);

	}

}
