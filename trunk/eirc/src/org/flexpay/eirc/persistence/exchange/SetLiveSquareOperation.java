package org.flexpay.eirc.persistence.exchange;

import org.flexpay.bti.persistence.apartment.ApartmentAttributeConfig;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.jetbrains.annotations.NotNull;

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
	 * ProcessInstance operation
	 *
	 * @param context
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if failure occurs
	 */
	public DelayedUpdate process(@NotNull ProcessingContext context) throws FlexPayException {

		RegistryRecord record = context.getCurrentRecord();
		return ContainerProcessHelper.updateApartmentAttribute(record, newValue,
				ApartmentAttributeConfig.ATTR_LIVE_SQUARE, factory);
	}

}
