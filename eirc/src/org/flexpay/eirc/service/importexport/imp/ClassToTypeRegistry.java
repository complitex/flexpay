package org.flexpay.eirc.service.importexport.imp;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.eirc.persistence.Consumer;

public class ClassToTypeRegistry extends org.flexpay.common.service.importexport.ClassToTypeRegistry {

	private static final int MODULE_BASE = 0x5000;
    private static final int ERROR_CODE = 0x0000;

    @Override
    protected int getModuleType(Class<? extends DomainObject> clazz) {
        if (Consumer.class.isAssignableFrom(clazz)) {
			return MODULE_BASE + 0x0101;
		}
        return super.getModuleType(clazz);
    }

    protected int getErrorCode() {
        return ERROR_CODE;
    }
}