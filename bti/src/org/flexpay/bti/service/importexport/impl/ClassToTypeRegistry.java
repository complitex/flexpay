package org.flexpay.bti.service.importexport.impl;

import org.flexpay.bti.persistence.Apartment;
import org.flexpay.common.persistence.DomainObject;

public class ClassToTypeRegistry extends org.flexpay.ab.service.importexport.imp.ClassToTypeRegistry {

	public int getType(Class<? extends DomainObject> clazz) {
		if (Apartment.class.isAssignableFrom(clazz)) {
			return 0x0031;
		}

		return super.getType(clazz);
	}

}
