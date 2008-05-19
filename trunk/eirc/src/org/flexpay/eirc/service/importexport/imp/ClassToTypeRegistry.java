package org.flexpay.eirc.service.importexport.imp;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.eirc.persistence.Consumer;

public class ClassToTypeRegistry extends org.flexpay.bti.service.importexport.imp.ClassToTypeRegistry {

	public int getType(Class<? extends DomainObject> clazz) {
		if (Consumer.class.isAssignableFrom(clazz)) {
			return 0x0101;
		}

		return super.getType(clazz);
	}
}
