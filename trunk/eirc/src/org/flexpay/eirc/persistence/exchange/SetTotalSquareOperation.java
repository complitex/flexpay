package org.flexpay.eirc.persistence.exchange;

import org.flexpay.bti.persistence.apartment.ApartmentAttributeConfig;
import org.flexpay.bti.persistence.apartment.ApartmentAttributeType;
import org.flexpay.bti.persistence.apartment.BtiApartment;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.eirc.persistence.Consumer;

import java.util.List;

/**
 * Open new service provider personal account
 */
public class SetTotalSquareOperation extends AbstractChangePersonalAccountOperation {

	private ServiceOperationsFactory factory;

	public SetTotalSquareOperation(ServiceOperationsFactory factory, List<String> datum) throws InvalidContainerException {
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

		ContainerProcessHelper.updateApartmentAttribute(record, newValue, ApartmentAttributeConfig.ATTR_TOTAL_SQUARE, factory);

	}

}
