package org.flexpay.bti.service.importexport.impl;

import org.flexpay.bti.persistence.Apartment;
import org.flexpay.common.persistence.DomainObject;

public class ClassToTypeRegistry extends org.flexpay.ab.service.importexport.imp.ClassToTypeRegistry {

	private static final int MODULE_BASE = 0x2000;

	public int getType(Class<? extends DomainObject> clazz) {
		if (Apartment.class.isAssignableFrom(clazz)) {
			return MODULE_BASE + 0x0031;
		}

		return super.getType(clazz);
	}
}
