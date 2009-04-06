package org.flexpay.common.persistence.registry;

public class CommonPropertiesFactory implements PropertiesFactory {

	public RegistryProperties newRegistryProperties() {
		return new RegistryProperties();
	}

	public RegistryRecordProperties newRecordProperties() {
		return new RegistryRecordProperties();
	}
}
