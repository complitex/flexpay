package org.flexpay.eirc.persistence.exchange;

import org.flexpay.bti.persistence.apartment.ApartmentAttributeConfig;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.jetbrains.annotations.NotNull;

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
	 * ProcessInstance operation
	 *
	 * @param context
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if failure occurs
	 */
	public DelayedUpdate process(@NotNull ProcessingContext context) throws FlexPayException {

		RegistryRecord record = context.getCurrentRecord();
		return ContainerProcessHelper.updateApartmentAttribute(record, newValue,
				ApartmentAttributeConfig.ATTR_TOTAL_SQUARE, factory);
	}

}
