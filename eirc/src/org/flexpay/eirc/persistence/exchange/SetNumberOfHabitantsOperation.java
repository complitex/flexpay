package org.flexpay.eirc.persistence.exchange;

import org.flexpay.bti.persistence.apartment.ApartmentAttributeConfig;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;

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
	public DelayedUpdate process(Registry registry, RegistryRecord record) throws FlexPayException {

		return ContainerProcessHelper.updateApartmentAttribute(record, newValue,
				ApartmentAttributeConfig.ATTR_NUMBER_OF_HABITANTS, factory);

	}

}
