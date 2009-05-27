package org.flexpay.ab.service.imp;

import org.flexpay.ab.service.ObjectsFactory;
import org.springframework.beans.factory.annotation.Required;

public class ObjectsFactoryHolder {

	private static ObjectsFactory factory = new AbObjectsFactory();

	public ObjectsFactory getInstance() {
		synchronized (ObjectsFactoryHolder.class) {
			return factory;
		}
	}

	@Required
	public void setFactory(ObjectsFactory factory) {
		synchronized (ObjectsFactoryHolder.class) {
			ObjectsFactoryHolder.factory = factory;
		}
	}
}
