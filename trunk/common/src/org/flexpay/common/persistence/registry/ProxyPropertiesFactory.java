package org.flexpay.common.persistence.registry;

public class ProxyPropertiesFactory implements PropertiesFactory {

	private PropertiesFactory factory;

	public RegistryProperties newRegistryProperties() {
		return factory.newRegistryProperties();
	}

	public RegistryRecordProperties newRecordProperties() {
		return factory.newRecordProperties();
	}

	public void setFactory(PropertiesFactory factory) {
		this.factory = factory;
	}
}
