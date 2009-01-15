package org.flexpay.eirc.service.importexport.imp;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.eirc.persistence.Consumer;
import org.flexpay.eirc.persistence.Service;

public class ClassToTypeRegistry extends org.flexpay.bti.service.importexport.impl.ClassToTypeRegistry {

	public int getType(Class<? extends DomainObject> clazz) {
		if (Consumer.class.isAssignableFrom(clazz)) {
			return 0x0101;
		}

		if (Service.class.isAssignableFrom(clazz)) {
			return 0x0102;
		}

		return super.getType(clazz);
	}
}
