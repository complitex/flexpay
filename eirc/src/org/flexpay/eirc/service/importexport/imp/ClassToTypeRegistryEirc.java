package org.flexpay.eirc.service.importexport.imp;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.eirc.persistence.Consumer;

public class ClassToTypeRegistryEirc implements ClassToTypeRegistry {

	public static final int MODULE_BASE = 0x5000;

    @Override
    public int getType(Class<? extends DomainObject> clazz) {
        if (Consumer.class.isAssignableFrom(clazz)) {
			return MODULE_BASE + 0x0101;
		}

		throw new IllegalArgumentException("Class " + clazz + " has no assigned type");
    }
}
