package org.flexpay.orgs.service.impl;

import org.flexpay.orgs.service.OrgsObjectsFactory;
import org.springframework.beans.factory.annotation.Required;

public class OrgsObjectsFactoryHolder {

	private static OrgsObjectsFactory factory = new OrgsObjectsFactoryImpl();

	public OrgsObjectsFactory getInstance() {
		synchronized (OrgsObjectsFactoryHolder.class) {
			return factory;
		}
	}

	@Required
	public void setFactory(OrgsObjectsFactory factory) {
		synchronized (OrgsObjectsFactoryHolder.class) {
			OrgsObjectsFactoryHolder.factory = factory;
		}
	}
}
