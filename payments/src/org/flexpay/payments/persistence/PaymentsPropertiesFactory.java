package org.flexpay.payments.persistence;

import org.flexpay.common.persistence.registry.PropertiesFactory;
import org.flexpay.common.persistence.registry.RegistryProperties;
import org.flexpay.common.persistence.registry.RegistryRecordProperties;

public class PaymentsPropertiesFactory implements PropertiesFactory {

	public RegistryProperties newRegistryProperties() {
		return new EircRegistryProperties();
	}

	public RegistryRecordProperties newRecordProperties() {
		return new RegistryRecordProperties();
	}

}