package org.flexpay.common.persistence.registry;

public class PropertiesFactoryHolder {

	private ProxyPropertiesFactory factory = new ProxyPropertiesFactory();

	public ProxyPropertiesFactory getFactory() {
		return factory;
	}
}
