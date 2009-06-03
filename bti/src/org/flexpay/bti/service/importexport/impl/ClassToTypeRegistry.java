package org.flexpay.bti.service.importexport.impl;

import org.flexpay.bti.persistence.apartment.BtiApartment;
import org.flexpay.common.persistence.DomainObject;

public class ClassToTypeRegistry extends org.flexpay.common.service.importexport.ClassToTypeRegistry {

	private static final int MODULE_BASE = 0x2000;
    private static final int ERROR_CODE = 0x0000;

    @Override
    protected int getModuleType(Class<? extends DomainObject> clazz) {
        if (BtiApartment.class.isAssignableFrom(clazz)) {
			return MODULE_BASE + 0x0031;
		}

        return super.getModuleType(clazz);
    }

    protected int getErrorCode() {
        return ERROR_CODE;
    }
}
