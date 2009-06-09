package org.flexpay.payments.service.importexport.imp;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.payments.persistence.Cashbox;
import org.flexpay.payments.persistence.Service;

public class ClassToTypeRegistry extends org.flexpay.common.service.importexport.ClassToTypeRegistry {

	private static final int MODULE_BASE = 0x3000;
	private static final int ERROR_CODE = 0x0000;

	@Override
	protected int getModuleType(Class<? extends DomainObject> clazz) {
		if (Service.class.isAssignableFrom(clazz)) {
			return MODULE_BASE + 0x0201;
		}
		if (Cashbox.class.isAssignableFrom(clazz)) {
			return MODULE_BASE + 0x006;
		}

		return super.getModuleType(clazz);
	}

	protected int getErrorCode() {
		return ERROR_CODE;
	}
}
