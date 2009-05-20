package org.flexpay.eirc.persistence.exchange;

import org.flexpay.common.exception.FlexPayException;
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
		if (props.getConsumer() == null) {
			throw new FlexPayException("Consumer was not set up, cannot change FIO");
		}

/*
		Apartment apartment = props.getApartment();


		//TODO: dd
		info.setCityName(newValue);

		factory.getConsumerInfoService().save(info);
*/

	}

}
