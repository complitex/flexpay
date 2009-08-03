package org.flexpay.eirc.persistence.exchange;

import org.flexpay.bti.persistence.apartment.ApartmentAttributeConfig;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;

import java.util.List;

/**
 * Set total live square of apartment
 */
public class SetLiveSquareOperation extends AbstractChangePersonalAccountOperation {

	private ServiceOperationsFactory factory;

	public SetLiveSquareOperation(ServiceOperationsFactory factory, List<String> datum) throws InvalidContainerException {
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
				ApartmentAttributeConfig.ATTR_LIVE_SQUARE, factory);
	}

}
