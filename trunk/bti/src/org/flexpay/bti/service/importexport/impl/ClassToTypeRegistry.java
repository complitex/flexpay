package org.flexpay.bti.service.importexport.impl;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.bti.persistence.Apartment;

public class ClassToTypeRegistry extends org.flexpay.ab.service.importexport.imp.ClassToTypeRegistry {

	public int getType(Class<? extends DomainObject> clazz) {
		if (Apartment.class.isAssignableFrom(clazz)) {
			return 0x0011;
		}

		return super.getType(clazz);
	}

}
