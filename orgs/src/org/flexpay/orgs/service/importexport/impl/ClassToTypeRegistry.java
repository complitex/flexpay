package org.flexpay.orgs.service.importexport.impl;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.orgs.persistence.*;

public class ClassToTypeRegistry extends org.flexpay.common.service.importexport.ClassToTypeRegistry {

	private static final int MODULE_BASE = 0x4000;
	private static final int ERROR_CODE = 0x0000;

	@Override
	protected int getModuleType(Class<? extends DomainObject> clazz) {
		if (Organization.class.isAssignableFrom(clazz)) {
			return MODULE_BASE + 0x001;
		}
		if (Bank.class.isAssignableFrom(clazz)) {
			return MODULE_BASE + 0x002;
		}
		if (ServiceProvider.class.isAssignableFrom(clazz)) {
			return MODULE_BASE + 0x003;
		}
		if (ServiceOrganization.class.isAssignableFrom(clazz)) {
			return MODULE_BASE + 0x004;
		}
		if (PaymentsCollector.class.isAssignableFrom(clazz)) {
			return MODULE_BASE + 0x005;
		}
		if (PaymentPoint.class.isAssignableFrom(clazz)) {
			return MODULE_BASE + 0x006;
		}
		return super.getModuleType(clazz);
	}

	protected int getErrorCode() {
		return ERROR_CODE;
	}
}
