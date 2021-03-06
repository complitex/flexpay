package org.flexpay.eirc.persistence;

import org.flexpay.common.persistence.registry.PropertiesFactory;
import org.flexpay.common.persistence.registry.RegistryProperties;
import org.flexpay.common.persistence.registry.RegistryRecordProperties;
import org.flexpay.payments.persistence.EircRegistryProperties;

public class EircPropertiesFactory implements PropertiesFactory {

	public RegistryProperties newRegistryProperties() {
		return new EircRegistryProperties();
	}

	public RegistryRecordProperties newRecordProperties() {
		return new EircRegistryRecordProperties();
	}

}
