package org.flexpay.common.service.importexport.imp;

import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.common.persistence.DomainObject;

public class ClassToTypeRegistryStub extends ClassToTypeRegistry {
    private static final int ERROR_CODE = 0x0000;

    protected int getErrorCode() {
        return ERROR_CODE;
    }
}
